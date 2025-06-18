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

    @FXML
    private ChoiceBox<String> startChoice;

    @FXML
    private ChoiceBox<String> destinationChoice;
    
    private String startTerminal;
    private String destinationTerminal;

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
            startChoice.getItems().clear();
            destinationChoice.getItems().clear();
        }

        if(operatorChoice.getValue() == "TAS Trans") {
            routeChoice.getItems().clear();
            routeChoice.getItems().addAll("OneAyala to Circuit Makati", "OneAyala to Nuvali");
            startChoice.getItems().clear();
            destinationChoice.getItems().clear();
        }
    }

    @FXML
    public void initializeStart(ActionEvent event) {
        if(routeChoice.getValue() == "ARCA SOUTH") {
            startChoice.getItems().clear();
            startChoice.getItems().addAll("BGC");
            destinationChoice.getItems().clear();
        }

        if(routeChoice.getValue() == "BGC LOOP") {
            startChoice.getItems().clear();
            startChoice.getItems().addAll("BGC LOOP 1");
            destinationChoice.getItems().clear();
        }

        if (routeChoice.getValue() == "OneAyala to Circuit Makati") {
            startChoice.getItems().clear();
            startChoice.getItems().addAll("TAS Circuit-OneAyala Loop");
            destinationChoice.getItems().clear();
        }

        if (routeChoice.getValue() == "OneAyala to Nuvali") {
            startChoice.getItems().clear();
            startChoice.getItems().addAll("OneAyala / Nuvali");
            destinationChoice.getItems().clear();
        }
    }

    @FXML
    public void initializeDestination(ActionEvent event) {
        if(startChoice.getValue() == "BGC") {
            destinationChoice.getItems().clear();
            destinationChoice.getItems().addAll("Arca South");
        }

        if(startChoice.getValue() == "BGC LOOP 1") {
            destinationChoice.getItems().clear();
            destinationChoice.getItems().addAll("BGC LOOP 2");
        }

        if(startChoice.getValue() == "TAS Circuit-OneAyala Loop") {
            destinationChoice.getItems().clear();
            destinationChoice.getItems().addAll("TAS Circuit-OneAyala Loop");
        }

        if(startChoice.getValue() == "OneAyala / Nuvali") {
            destinationChoice.getItems().clear();
            destinationChoice.getItems().addAll("Nuvali / OneAyala");
        }
    }

    @FXML
    public void buyTicketHandler(ActionEvent event) throws IOException {
        startTerminal = startChoice.getValue();
        destinationTerminal = destinationChoice.getValue();

        
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

