package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BuyLoadCardController {

    @FXML
    private Label usernamelabel;

    @FXML
    private CheckBox cardCheckBox;

    @FXML
    private Label accnum;

    @FXML
    private TextField cardNumField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private float amount;
    private String cardNum;
    private User currentUser;
    private BlippiCard blippiCard;
    private BlippiCard selectedBlippi;
    public void setCurrentUser(User user, BlippiCard blippi) {
        this.currentUser = user;
        this.blippiCard = blippi;
        String username = currentUser.getUsername();
        usernamelabel.setText(username);
        cardCheckBox.setText(blippi.getLabel());
        accnum.setText(blippi.getCardNumber());
    }

    public void setAmount(float numAmount) {
        this.amount = numAmount;
    }

    @FXML
    public void cardCheckBoxHandler(ActionEvent event) {
        if(cardCheckBox.isSelected()) {
            cardNumField.setText(blippiCard.getCardNumber());
            this.selectedBlippi = blippiCard;
            this.cardNum = selectedBlippi.getCardNumber();
        }
    }

    @FXML
    public boolean nextButtonHandler(ActionEvent event) throws IOException {
        if(!cardCheckBox.isSelected()) {
            cardNum = cardNumField.getText();
        }

        if(!inputValidator(cardNum)) {
            return false;
        }

        selectedBlippi = searchCard(cardNum);

        //Check whether the blippi card exists
        if(selectedBlippi == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("Card cannot be found");
            alert.showAndWait();
            return false;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/LoadConfirmation.fxml"));
        root = loader.load();

        LoadConfirmationController LoadConfirmationController = loader.getController();
        LoadConfirmationController.setCurrentUser(currentUser, blippiCard);
        LoadConfirmationController.setAmount(amount, selectedBlippi);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        return true;
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/BuyLoad.fxml"));
        root = loader.load();

        //Set current user for the blippi card set-up and add card to home page
        BuyLoadController buyLoadController = loader.getController();
        buyLoadController.setCurrentUser(currentUser, blippiCard);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public boolean inputValidator(String cardNum) {
        if(cardNum.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("No card number provided");
            alert.showAndWait();
            return false;
        }
        for (int i = 0; i < cardNum.length(); i++) {
            // Check whether each character is a letter or special character
            if (!Character.isLetterOrDigit(cardNum.charAt(i)) || Character.isLetter(cardNum.charAt(i))) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("Card number must only contain numbers");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    private BlippiCard searchCard(String accNum) {
        File blippiFile = new File("blippicards.txt");

        if(blippiFile.exists()) {
            Scanner filescanner;

            try {
                filescanner = new Scanner(blippiFile);

                //Look for the card with the matching userId
                while(filescanner.hasNextLine()) {
                    String data = filescanner.nextLine();

                    String cardnum_from_file = data.split(";")[0];
                    String balance_from_file = data.split(";")[1];
                    String label_from_file = data.split(";")[2];
                    String exp_from_file = data.split(";")[3];
                    String id_from_file = data.split(";")[4];

                    if(cardnum_from_file.equals(accNum)) {
                        //Create BlippiCard object from database info
                        BlippiCard blippi = new BlippiCard(cardnum_from_file, Integer.valueOf(balance_from_file), label_from_file, exp_from_file, id_from_file, null);
                        return blippi;
                    }
                }
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("blippicards.txt file cannot be read");
        }
        return null;
    }
}
