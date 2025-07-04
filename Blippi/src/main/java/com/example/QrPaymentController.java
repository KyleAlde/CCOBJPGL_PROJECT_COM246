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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class QrPaymentController {
    @FXML
    private Label usernamelabel;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField cardNumField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField cvvFIeld;
    
    private Stage stage;
    private Scene scene;
    private Parent root;

    private String paymentMethod = "Credit/Debit Card";
    private float loadAmount;
    private float newAmount;
    private User currentUser;
    private BlippiCard blippiCard;
    private BlippiCard selectedBlippi;
    private String startTerminal;
    private String destinationTerminal;
    private String operator;
    private String route;


    public void setAmount(float inputAmount) {
        this.loadAmount = inputAmount;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
        String username = currentUser.getUsername();
        usernamelabel.setText(username);
    }

    public void setRoute(String operator, String route, String startTerminal, String destinationTerminal) {
        this.operator = operator;
        this.route = route;
        this.startTerminal = startTerminal;
        this.destinationTerminal = destinationTerminal;
    }

    @FXML
    public boolean completeOrderButtonHandler(ActionEvent event) throws IOException {

        if(!inputValidator(firstNameField.getText(), lastNameField.getText(), cardNumField.getText(), cvvFIeld.getText())) {
            return false;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/QRConfirmation.fxml"));
            root = loader.load();

            QRConfirmationController qrConfirmationController = loader.getController();
            qrConfirmationController.setCurrentUser(currentUser);
            qrConfirmationController.setAmount(loadAmount, blippiCard, paymentMethod);
            qrConfirmationController.setRoute(operator, route, startTerminal, destinationTerminal);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/QrTicketV2.fxml"));
            root = loader.load();

            //Set current user for the blippi card set-up and add card to home page
            QrTicketController qrTicketController = loader.getController();
            qrTicketController.setCurrentUser(currentUser);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean inputValidator(String fname, String lname, String cardnum, String cvv) {

        if(fname.length() == 0 || lname.length() == 0 || cardnum.length() == 0 || cvv.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("Please fill out every field");
            alert.showAndWait();
            return false;
        }
        
        for (int i = 0; i < fname.length(); i++) {
            // Check whether each character is a special character
            if (!Character.isLetter(fname.charAt(i)) && !fname.contains(".") &&  !fname.contains("-") && !fname.contains("'") && !fname.contains(" ") ) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("Name must not contain special characters or numbers");
                alert.showAndWait();
                return false;
            }
        }

        for (int i = 0; i < lname.length(); i++) {
            // Check whether each character is a special character
            if (!Character.isLetter(lname.charAt(i)) && !lname.contains(".") &&  !lname.contains("-") && !lname.contains("'") && !lname.contains(" ") ) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("Name must not contain special characters or numbers");
                alert.showAndWait();
                return false;
            }
        }

        for (int i = 0; i < cardnum.length(); i++) {
            // Check whether each character is a letter or special character
            if (!Character.isDigit(cardnum.charAt(i))) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("The card number you entered is invalid");
                alert.showAndWait();
                return false;
            }
        }

        if(cardnum.length() != 16) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("The card number you entered is invalid");
            alert.showAndWait();
            return false;
        }

        for (int i = 0; i < cvv.length(); i++) {
            // Check whether each character is a letter or special character
            if (!Character.isDigit(cvv.charAt(i))) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("The CVV you entered is invalid");
                alert.showAndWait();
                return false;
            }
        }

        if(cvv.length() != 3) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("The CVV you entered is invalid");
            alert.showAndWait();
            return false;
        }

        return true;
    }
}