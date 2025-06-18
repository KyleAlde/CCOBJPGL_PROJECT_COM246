package com.example;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class QrTicketController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ChoiceBox<String> operatorChoice;

    @FXML
    private ChoiceBox<String> routeChoice;

    private User currentUser;
    private BlippiCard blippiCard;
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
    }

    @FXML
    public void initialize(){
        operatorChoice.getItems().addAll("BGC Bus", "TAS Trans");
    }

    @FXML
    public void initializeRoutes(ActionEvent event) {
        if(operatorChoice.getValue() == "BGC Bus") {
            routeChoice.getItems().clear();
            routeChoice.getItems().addAll("ARCA SOUTH", "BGC LOOP");
        }

        if(operatorChoice.getValue() == "TAS Trans") {
            routeChoice.getItems().clear();
            routeChoice.getItems().addAll("OneAyala to Circuit Makati", "OneAyala to Nuvali");
        }
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
    
}

