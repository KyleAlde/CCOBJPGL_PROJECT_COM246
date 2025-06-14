package com.example;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TransactionHistoryController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;
    private BlippiCard blippiCard;
    public void setCurrentUser(User user, BlippiCard blippi) {
        this.currentUser = user;
        this.blippiCard = blippi;
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
    
}

