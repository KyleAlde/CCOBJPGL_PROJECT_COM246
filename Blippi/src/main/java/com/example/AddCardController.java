package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddCardController {
    @FXML
    TextField cardNumField;

    @FXML
    TextField cardLabelField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    //Method for setting current user from LogIn/SignUp
    private User currentUser;
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public boolean saveCardButtonController(ActionEvent event) {
        String cardNum = cardNumField.getText();
        String cardLabel = cardLabelField.getText();

        //Get the id attribute from the User class
        String userId = currentUser.getId();

        cardNum = cardNum.trim();
        cardLabel = cardLabel.trim();

        BlippiCard blippi = new BlippiCard(cardNum, 0, cardLabel, userId);

        if(cardNum.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No card number provided");
            return false;
        }

        if(cardLabel.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No card label provided");
            return false;
        }

        try {
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("blippicards.txt", true));

            myWriter.newLine();
            myWriter.write(blippi.getCardNumber() + ";" + blippi.getBalance() + ";" + blippi.getLabel() + ";" + blippi.getUserId()
            );
            myWriter.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            root = loader.load();

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
}
