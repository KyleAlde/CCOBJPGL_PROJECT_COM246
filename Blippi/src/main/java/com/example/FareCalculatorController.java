package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;

import javafx.event.ActionEvent;

public class FareCalculatorController {
    @FXML
    private ChoiceBox<String> fareChoice;
    
    @FXML
    private ChoiceBox<String> boardingChoice;

    @FXML
    private ChoiceBox<String> alightingChoice;

    @FXML
    private Label fareLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Label boardLabel;

    @FXML
    private Label alightLabel;

    @FXML
    private Label discountLabel;

    @FXML
    private Label lrtLabel;

    private String boardingStation;
    private String alightingStation;
    private String stationChoice;

    private String[] lrt1Stations = {"Dr. Santos", "Ninoy Aquino Ave", "PITX", "MIA Road", "Redemptorist", "Baclaran", "EDSA", "Libertad", "Gil Puyat", "Vito Cruz", "Quirino", "Pedro Gil", "UN Ave", "Central", "Carriedo", "Doroteo Jose", "Bambang", "Tayuman", "Blumentritt", "Abad Santos", "R. Papa", "5th Avenue", "Monumento", "Balintawak", "Fernando Poe Jr"};

    private String[] lrt2Stations = {"Recto", "Legarda", "Pureza", "V. Mapa", "J. Ruiz", "Gilmore", "Betty Go", "Cubao", "Anonas", "Katipunan", "Santolan", "Marikina", "Antipolo"};

    private String[] mrt3Stations = {"North Ave", "Quezon Ave", "GMA Kamuning", "Araneta-Cubao", "Santolan-Anapolis", "Ortigas", "Shaw Blvd", "Boni", "Guadalupe", "Buendia", "Ayala", "Magallanes", "Taft"};

    private float[][] lrt1Matrix= {
        {16, 19, 20, 22, 23, 26, 27, 28, 29, 31, 32, 33, 34, 36, 37, 38, 39, 40, 41, 42, 43, 45, 46, 49, 52},
        {19, 16, 18, 20, 21, 23, 24, 26, 27, 28, 29, 31, 32, 33, 35, 36, 36, 37, 38, 40, 41, 42, 44, 47, 50},
        {20, 18, 16, 18, 19, 22, 22, 24, 25, 27, 28, 29, 30, 32, 33, 34, 35, 36, 37, 38, 39, 40, 42, 45, 48},
        {22, 20, 18, 16, 17, 20, 20, 22, 23, 25, 26, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 40, 43, 46},
        {23, 21, 19, 17, 16, 18, 19, 21, 22, 23, 25, 26, 27, 29, 30, 31, 32, 33, 34, 35, 36, 37, 39, 42, 45},
        {26, 23, 22, 20, 18, 16, 17, 19, 20, 21, 22, 24, 25, 27, 28, 29, 30, 30, 31, 33, 34, 35, 37, 40, 43},
        {27, 24, 22, 20, 19, 17, 16, 18, 19, 20, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 34, 36, 39, 42},
        {28, 26, 24, 22, 21, 19, 18, 16, 17, 19, 20, 22, 23, 24, 26, 26, 27, 28, 29, 30, 31, 33, 34, 38, 40},
        {29, 27, 25, 23, 22, 20, 19, 17, 16, 18, 19, 20, 21, 23, 24, 25, 26, 27, 28, 29, 30, 32, 33, 37, 39},
        {31, 28, 27, 25, 23, 21, 20, 19, 18, 16, 17, 19, 20, 22, 23, 24, 25, 26, 27, 28, 29, 30, 32, 35, 38},
        {32, 29, 28, 26, 25, 22, 22, 20, 19, 17, 16, 17, 19, 20, 21, 22, 23, 24, 25, 27, 28, 29, 31, 34, 37},
        {33, 31, 29, 27, 24, 23, 22, 21, 20, 19, 17, 16, 17, 19, 20, 21, 22, 23, 24, 25, 26, 28, 29, 33, 35},
        {34, 32, 30, 29, 27, 25, 24, 22, 21, 20, 19, 17, 16, 18, 19, 20, 21, 22, 23, 24, 25, 27, 28, 32, 34},
        {36, 33, 32, 30, 29, 27, 26, 24, 23, 22, 20, 19, 18, 16, 17, 18, 19, 20, 21, 22, 23, 25, 27, 30, 33},
        {37, 35, 33, 31, 30, 28, 27, 25, 24, 23, 21, 20, 19, 17, 16, 17, 18, 19, 20, 21, 22, 24, 25, 29, 31},
        {38, 36, 34, 32, 31, 29, 28, 26, 25, 24, 22, 21, 20, 18, 17, 16, 17, 18, 19, 20, 21, 23, 24, 28, 30},
        {39, 36, 35, 33, 32, 30, 29, 27, 26, 25, 23, 22, 21, 19, 18, 17, 16, 17, 19, 20, 21, 22, 23, 27, 30},
        {40, 37, 36, 34, 33, 30, 30, 28, 27, 25, 24, 23, 22, 20, 19, 18, 17, 16, 17, 19, 20, 21, 23, 26, 29},
        {41, 38, 37, 35, 34, 31, 31, 29, 28, 26, 25, 24, 23, 21, 20, 19, 18, 17, 16, 18, 19, 20, 22, 25, 28},
        {42, 40, 39, 36, 35, 33, 32, 30, 29, 28, 26, 25, 24, 23, 21, 20, 19, 18, 17, 16, 17, 19, 20, 24, 26},
        {43, 41, 39, 37, 36, 34, 33, 31, 30, 29, 28, 26, 25, 23, 22, 21, 20, 19, 18, 17, 16, 17, 19, 23, 25},
        {45, 42, 40, 38, 37, 35, 34, 32, 31, 30, 29, 27, 26, 24, 23, 22, 21, 20, 19, 18, 17, 16, 18, 21, 24},
        {46, 44, 42, 40, 39, 37, 36, 34, 33, 32, 31, 29, 28, 27, 25, 24, 23, 22, 21, 20, 19, 18, 16, 20, 22},
        {48, 47, 45, 43, 42, 41, 40, 39, 37, 36, 35, 34, 33, 32, 30, 29, 28, 27, 26, 24, 23, 21, 20, 16, 19},
        {52, 59, 48, 46, 45, 43, 42, 40, 39, 38, 37, 35, 34, 33, 31, 30, 30, 29, 28, 26, 25, 24, 22, 19, 16}
    };

    private float[][] lrt2Matrix = {
        {13, 15, 16, 18, 19, 21, 22, 23, 25, 26, 28, 31, 33},
        {15, 13, 15, 17, 18, 19, 21, 22, 24, 25, 27, 29, 32},
        {16, 15, 13, 15, 16, 18, 19, 20, 22, 23, 26, 28, 30},
        {18, 17, 15, 13, 15, 16, 17, 19, 20, 22, 24, 26, 29},
        {19, 18, 16, 15, 13, 14, 16, 17, 19, 20, 22, 24, 27},
        {21, 19, 18, 16, 14, 13, 15, 16, 18, 19, 21, 23, 26},
        {22, 21, 19, 17, 16, 15, 13, 15, 16, 18, 20, 22, 25},
        {23, 22, 20, 19, 17, 16, 15, 13, 15, 16, 19, 21, 23},
        {25, 24, 22, 20, 19, 18, 16, 15, 13, 14, 17, 19, 22}, 
        {26, 25, 23, 22, 20, 19, 18, 16, 14, 13, 16, 18, 21},
        {28, 27, 26, 24, 22, 21, 20, 19, 17, 16, 13, 15, 18},
        {31, 29, 28, 26, 24, 23, 22, 21, 19, 18, 15, 13, 16},
        {33, 32, 30, 29, 27, 26, 25, 23, 22, 21, 18, 16, 15}
    };

    private float[][] mrt3Matrix = {
        {10, 13, 13, 16, 16, 20, 20, 20, 24, 24, 24, 28, 28},
        {13, 10, 13, 13, 16, 16, 20, 20, 20, 24, 24, 24, 28},
        {13, 13, 10, 13, 13, 16, 16, 20, 20, 20, 24, 24, 24},
        {16, 13, 13, 10, 13, 13, 16, 16, 20, 20, 20, 24, 24},
        {16, 16, 13, 13, 10, 13, 13, 16, 16, 20, 20, 20, 24},
        {20, 16, 16, 13, 13, 10, 13, 13, 16, 16, 20, 20, 20},
        {20, 20, 16, 16, 13, 13, 10, 13, 13, 16, 16, 20, 20},
        {20, 20, 20, 16, 16, 13, 13, 10, 13, 13, 16, 16, 20},
        {24, 20, 20, 20, 16, 16, 13, 13, 10, 13, 13, 16, 16},
        {24, 24, 20, 20, 20, 16, 16, 13, 13, 10, 13, 13, 16},
        {24, 24, 24, 20, 20, 20, 16, 16, 13, 13, 10, 13, 13},
        {28, 24, 24, 24, 20, 20, 20, 16, 16, 13, 13, 10, 13},
        {28, 28, 24, 24, 24, 20, 20, 20, 16, 16, 13, 13, 10},
    };

    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;
    private BlippiCard blippiCard;
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
    }

    @FXML
    public void initialize() {
        stationChoice = "MRT 3";
        boardingChoice.getItems().clear();
        boardingChoice.getItems().addAll(mrt3Stations);
        alightingChoice.getItems().clear();
        alightingChoice.getItems().addAll(mrt3Stations);

        fareChoice.getItems().addAll("Regular", "Discounted");
        fareChoice.setValue("Regular");
    }

    @FXML
    public void mrt3ButtonHandler(ActionEvent event) {
        stationChoice = "MRT 3";
        System.out.println("MRT3 button pressed");
        boardingChoice.getItems().clear();
        boardingChoice.getItems().addAll(mrt3Stations);
        alightingChoice.getItems().clear();
        alightingChoice.getItems().addAll(mrt3Stations);
        lrtLabel.setText(stationChoice);
    }
    @FXML
    public void lrt2ButtonHandler(ActionEvent event) {
        stationChoice = "LRT 2";
        System.out.println("LRT2 button pressed");
        boardingChoice.getItems().clear();
        boardingChoice.getItems().addAll(lrt2Stations);
        alightingChoice.getItems().clear();
        alightingChoice.getItems().addAll(lrt2Stations);
        lrtLabel.setText(stationChoice);
    }
    @FXML
    public void lrt1ButtonHandler(ActionEvent event) {
        stationChoice = "LRT 1";
        System.out.println("LRT1 button pressed");
        boardingChoice.getItems().clear();
        boardingChoice.getItems().addAll(lrt1Stations);
        alightingChoice.getItems().clear();
        alightingChoice.getItems().addAll(lrt1Stations);
        lrtLabel.setText(stationChoice);
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

    @FXML
    public boolean calcButtonHandler(ActionEvent event) {
        float totalFare = 0;
        boardingStation = boardingChoice.getValue();
        alightingStation = alightingChoice.getValue();
        float fare = calculateFare();

        if(boardingStation.equals(alightingStation)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("Boarding and Alighting station must be different");
            alert.showAndWait();
            return false;
        }

        if (boardingStation == null || alightingStation == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("Boarding or Alighting station not selected");
            alert.showAndWait();
            return false;
        }

        if(fareChoice.getValue().equals("Discounted")) {
            totalFare = fare - (fare * .2f);
            discountLabel.setText("20.00%");
        } else {
            totalFare = fare;
            discountLabel.setText("0.00%");
        }
        String formattedFare = String.format("₱%.2f", fare);
        fareLabel.setText(formattedFare);
        boardLabel.setText(boardingStation);
        alightLabel.setText(alightingStation);
        
        String formattedTotal = String.format("₱%.2f", totalFare);
        totalLabel.setText(formattedTotal);
        return true;
    }
    public float calculateFare() {
        boardingStation = boardingChoice.getValue();
        alightingStation = alightingChoice.getValue();

        if (boardingStation == null || alightingStation == null) {
            System.out.println("Error: boarding or alighting station not selected");
            return -1; // Return an error value
        }

        int boardIndex = -1;
        int alightIndex = -1;

        // Determine which station choice is selected and get indices
        if (stationChoice.equals("LRT 1")) {
            boardIndex = indexOf(lrt1Stations, boardingStation);
            alightIndex = indexOf(lrt1Stations, alightingStation);
            if (boardIndex == -1 || alightIndex == -1) {
                System.out.println("Error: Invalid station selection for LRT1");
                return -1; // Return an error value
            }
            return lrt1Matrix[boardIndex][alightIndex]; // Return fare from matrix
        } else if (stationChoice.equals("LRT 2")) {
            boardIndex = indexOf(lrt2Stations, boardingStation);
            alightIndex = indexOf(lrt2Stations, alightingStation);
            if (boardIndex == -1 || alightIndex == -1) {
                System.out.println("Error: Invalid station selection for LRT2");
                return -1; // Return an error value
            }
            return lrt2Matrix[boardIndex][alightIndex]; // Return fare from matrix
        } else if (stationChoice.equals("MRT 3")) {
            boardIndex = indexOf(mrt3Stations, boardingStation);
            alightIndex = indexOf(mrt3Stations, alightingStation);
            if (boardIndex == -1 || alightIndex == -1) {
                System.out.println("Error: Invalid station selection for MRT3");
                return -1; // Return an error value
            }
            return mrt3Matrix[boardIndex][alightIndex]; // Return fare from matrix
        } else {
            System.out.println("Error: station not selected");
            return -1; // Return an error value
        }
    }

    // Helper method to find the index of a station in an array
    private int indexOf(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if not found
    }

}
