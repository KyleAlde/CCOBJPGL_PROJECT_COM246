package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddCardController {
    @FXML
    TextField cardNumField;

    @FXML
    TextField cardLabelField;
    
    @FXML
    private Label usernamelabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    //Method for setting current user from LogIn/SignUp
    private User currentUser;
    public void setCurrentUser(User user) {
        this.currentUser = user;
        String username = currentUser.getUsername();
        usernamelabel.setText(username);
    }

    @FXML
    public boolean saveCardButtonHandler(ActionEvent event) {
        String cardNum = cardNumField.getText();
        String cardLabel = cardLabelField.getText();

        //Get the id attribute from the User object
        String userId = currentUser.getId();

        cardNum = cardNum.trim();
        cardLabel = cardLabel.trim();

        //Generate expiration date
        String expDate = ExpDateGenerator();

        ArrayList<Transaction> transacList = new ArrayList<>();
        BlippiCard blippi = new BlippiCard(cardNum, 0, cardLabel, expDate, 0, userId, transacList);

        //Handle invalid input (special characters, empty field, etc.)
        if(!inputValidator(blippi)) {
            return false;
        }

        try {
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("blippicards.txt", true));

            myWriter.newLine();
            myWriter.write(blippi.getCardNumber() + ";" + String.format("%.0f", blippi.getBalance()) + ";" + blippi.getLabel() + ";" + blippi.getExpDate() + ";" + "0" + ";" + blippi.getUserId());
            myWriter.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/HomeV2.fxml"));
            root = loader.load();

            HomeController homeController = loader.getController();
            currentUser.setBlippi(blippi);
            homeController.setCurrentUser(currentUser);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch(IOException e) {
            System.out.println("An error occurred.");
            return false;
        }

        return true;
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/HomeV2.fxml"));
            root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setCurrentUser(currentUser);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String ExpDateGenerator() {
        GregorianCalendar gc = new GregorianCalendar();
        Random random = new Random();
        int year = randBetween(2027, 2030, random);
        gc.set(gc.YEAR, year);
        int dayOfYear = gc.getActualMaximum(gc.DAY_OF_YEAR);
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        // Convert GregorianCalendar to LocalDateTime
        LocalDateTime randomDate = LocalDateTime.of(
            gc.get(gc.YEAR),
            gc.get(gc.MONTH) + 1,
            gc.get(gc.DAY_OF_MONTH),
            gc.get(gc.HOUR_OF_DAY),
            gc.get(gc.MINUTE),
            gc.get(gc.SECOND)
        );
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = randomDate.format(format);
        return formattedDate;
    }

    public static int randBetween(int start, int end, Random random) {
        return start + random.nextInt(end - start + 1);
    }

    public boolean inputValidator(BlippiCard blippi) {
        String cardNum = blippi.getCardNumber();
        String cardLabel = blippi.getLabel();

        if(cardNum.length() == 0 && cardLabel.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("No card number and label provided");
            alert.showAndWait();
            return false;
        }

        if(cardNum.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("No card number provided");
            alert.showAndWait();
            return false;
        }

        if(cardLabel.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("No card label provided");
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

        SearchData search = new SearchData();
        if (search.searchByCardNum(cardNum) != null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("The card you entered is already registered to another account");
            alert.showAndWait();
            return false;
        }
        if(cardNum.length() != 16) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("The card you entered does not exist");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}