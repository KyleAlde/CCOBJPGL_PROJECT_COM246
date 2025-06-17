package com.example;

import java.io.IOException;
import java.text.NumberFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RedeemRewardsController {
    @FXML
    private Label usernamelabel;

    @FXML
    private Label accnum;

    @FXML
    private Label expdate;

    @FXML
    private Label cardlabel;

    @FXML
    private Label points;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;
    private BlippiCard blippiCard;
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
        String username = currentUser.getUsername();
        usernamelabel.setText(username);

        accnum.setText(blippiCard.getCardNumber());
        expdate.setText(blippiCard.getExpDate());
        cardlabel.setText(blippiCard.getLabel());

        String formattedNumber = NumberFormat.getInstance().format(blippiCard.getRewards());
        points.setText(formattedNumber);
    }

    @FXML
    public void redvialoadHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/RedeemLoad.fxml"));
            root = loader.load();

            RedeemLoadController redeemLoadController = loader.getController();
            redeemLoadController.setCurrentUser(currentUser);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Home.fxml"));
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
}
