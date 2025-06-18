package com.example;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MayaPaymentController {
    @FXML
    private Label usernamelabel;

    @FXML
    private TextField numberField;

    @FXML
    private TextField passwordField;
    
    private Stage stage;
    private Scene scene;
    private Parent root;

    private float loadAmount;
    private User currentUser;
    private BlippiCard blippiCard;
    private BlippiCard selectedBlippi;

    public void setAmount(float inputAmount, BlippiCard blippi) {
        this.loadAmount = inputAmount;
        this.selectedBlippi = blippi;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
        String username = currentUser.getUsername();
        usernamelabel.setText(username);
    }

    @FXML
    public boolean proceedButtonHandler(ActionEvent event) {
        if(!inputValidator(numberField.getText(), passwordField.getText())) {
            return false;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/LoadConfirmationV2.fxml"));
            root = loader.load();

            LoadConfirmationController loadConfirmationController = loader.getController();
            loadConfirmationController.setCurrentUser(currentUser);
            loadConfirmationController.setAmount(loadAmount, selectedBlippi, "Maya");

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/BuyLoadCardV2.fxml"));
            root = loader.load();

            BuyLoadCardController buyLoadCardController = loader.getController();
            buyLoadCardController.setCurrentUser(currentUser);
            buyLoadCardController.setAmount(loadAmount);

            // Load stage and scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean inputValidator(String num, String pin) {
        if(num.length() != 11) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("The phone number you entered is invalid");
            alert.showAndWait();
            return false;
        }
        
        if(pin.length() != 4) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("The pin you entered is invalid");
            alert.showAndWait();
            return false;
        }

        for (int i = 0; i < num.length(); i++) {
            // Check whether each character is a letter or special character
            if (!Character.isDigit(num.charAt(i))) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("The phone number you entered is invalid");
                alert.showAndWait();
                return false;
            }
        }

        for (int i = 0; i < pin.length(); i++) {
            // Check whether each character is a letter or special character
            if (!Character.isDigit(pin.charAt(i))) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("The phone number you entered is invalid");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }
}
