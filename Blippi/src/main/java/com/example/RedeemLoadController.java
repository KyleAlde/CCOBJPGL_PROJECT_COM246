package com.example;

import java.io.IOException;
import java.text.NumberFormat;

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

public class RedeemLoadController {
    @FXML
    private Label usernamelabel;

    @FXML
    private Label cardlabel;

    @FXML
    private Label points;

    @FXML
    TextField amountTextField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;
    private BlippiCard blippiCard;
    private float pointsAsLoad;
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
        String username = currentUser.getUsername();
        usernamelabel.setText(username);
        cardlabel.setText(blippiCard.getLabel());

        String formattedNumber = NumberFormat.getInstance().format(blippiCard.getRewards());
        points.setText(formattedNumber);

        this.pointsAsLoad = blippiCard.getRewards() / 200;
        System.out.println(pointsAsLoad);
    }

    @FXML
    public boolean redeemButtonHandler(ActionEvent event) throws IOException {
        float numAmount = 0;

        try {
            numAmount = Float.parseFloat(amountTextField.getText());
            if(numAmount < 1 || numAmount > 5000) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("Please enter a valid amount");
                alert.showAndWait();
                return false;
            }

            if(numAmount > pointsAsLoad) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("You do not have enough points");
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

        float reqPoints = numAmount * 200;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/LoadRewardDetailsV2.fxml"));
            root = loader.load();

            //Set current user and amount
            LoadRewardDetailsController loadRewardDetailsController = loader.getController();
            loadRewardDetailsController.setCurrentUser(currentUser);
            loadRewardDetailsController.setAmount(numAmount, reqPoints);
            System.out.println(numAmount);

            // Load stage and scene
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/RedeemRewardsV2.fxml"));
            root = loader.load();

            RedeemRewardsController redeem = loader.getController();
            redeem.setCurrentUser(currentUser);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
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
