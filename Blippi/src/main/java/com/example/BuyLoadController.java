package com.example;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

public class BuyLoadController {
    @FXML
    private Label usernamelabel;

    @FXML
    private Label accnum;

    @FXML
    private Label expdate;

    @FXML
    private Label cardlabel;

    @FXML
    private Label baldate;

    @FXML
    private Label balanceamt;

    @FXML
    private TextField amountTextField;
    
    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;
    private BlippiCard blippiCard;
    public void setCurrentUser(User user, BlippiCard blippi) {
        this.currentUser = user;
        this.blippiCard = blippi;
        String username = currentUser.getUsername();
        usernamelabel.setText(username);

        accnum.setText(blippi.getCardNumber());
        expdate.setText(blippi.getExpDate());
        cardlabel.setText(blippi.getLabel());
        String balance = String.format("%.2f", blippi.getBalance());
        balanceamt.setText(balance);

        // Generate current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a");
        String formattedDate = now.format(format);

        baldate.setText(formattedDate);
    }

    @FXML
    public boolean nextButtonHandler(ActionEvent event) throws IOException {
        float numAmount = 0;

        try {
            numAmount = Float.parseFloat(amountTextField.getText());
            if(numAmount < 5 || numAmount > 5000) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("Please enter a valid amount");
                alert.showAndWait();
                return false;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("Please enter a valid amount");
            alert.showAndWait();
            return false;
        }

        //Load BuyLoadCard.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/BuyLoadCard.fxml"));
        root = loader.load();

        //Set current user
        BuyLoadCardController buyLoadCardController = loader.getController();
        buyLoadCardController.setCurrentUser(currentUser, blippiCard);
        buyLoadCardController.setAmount(numAmount);

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        return true;
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Home.fxml"));
        root = loader.load();

        HomeController homeController = loader.getController();
        homeController.setCurrentUser(currentUser, blippiCard);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void tenHandler(ActionEvent event){
        amountTextField.setText("10");
    }

    @FXML
    public void fifteenHandler(ActionEvent event){
        amountTextField.setText("15");
    }

    @FXML
    public void twentyHandler(ActionEvent event){
        amountTextField.setText("20");
    }

    @FXML
    public void thirtyHandler(ActionEvent event){
        amountTextField.setText("30");
    }

    @FXML
    public void fiftyHandler(ActionEvent event){
        amountTextField.setText("50");
    }

    @FXML
    public void oneHundredHandler(ActionEvent event){
        amountTextField.setText("100");
    }

    @FXML
    public void twoHundredHandler(ActionEvent event){
        amountTextField.setText("200");
    }

    @FXML
    public void fiveHundredHandler(ActionEvent event){
        amountTextField.setText("500");
    }
}