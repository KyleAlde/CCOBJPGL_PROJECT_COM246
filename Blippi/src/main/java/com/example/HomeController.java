package com.example;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void addCardButtonController(ActionEvent event) throws IOException{
        // Load AddCard.fxml when add card button is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCard.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void blippitixController(ActionEvent event) throws IOException {
        // Load QrTicket.fxml when qr ticket button is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("QrTicket.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void blippitransacController(ActionEvent event) throws IOException {
        // Load TransactionHistory.fxml when transaction history button is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionHistory.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
