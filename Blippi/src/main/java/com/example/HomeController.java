package com.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HomeController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane container;

    @FXML
    private Label usernamelabel;

    //Method for setting current user from LogIn/SignUp
    private User currentUser;
    private BlippiCard blippicard;
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippicard = user.getBlippi();
        if(blippicard != null) {
            setCard(blippicard);
        }
        String username = currentUser.getUsername();
        usernamelabel.setText(username);
    }

    @FXML
    public void addCardButtonHandler(ActionEvent event) throws IOException{
        try {
            // Load AddCard.fxml when add card button is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/AddCardV2.fxml"));
        root = loader.load();

        //Set current user for the blippi card set-up
        AddCardController setUser = loader.getController();
        setUser.setCurrentUser(currentUser);

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public boolean blippiloadHandler(ActionEvent event) throws IOException {
        if(blippicard == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("You currently do not have a blippi card");
            alert.showAndWait();
            return false;
        }

        try {
            // Load BuyLoad.fxml when qr ticket button is clicked
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/BuyLoadV2.fxml"));
            root = loader.load();

            //Set currentUser
            BuyLoadController buyLoadController = loader.getController();
            buyLoadController.setCurrentUser(currentUser);

            // Load stage and scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @FXML
    public void blippitixHandler(ActionEvent event) throws IOException {
        try {
            // Load QrTicket.fxml when qr ticket button is clicked
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/QrTicket.fxml"));
            root = loader.load();

            QrTicketController qrTicketController = loader.getController();
            qrTicketController.setCurrentUser(currentUser);

            // Load stage and scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void blippitransacHandler(ActionEvent event) throws IOException {
        try {
            // Load TransactionHistory.fxml when transaction history button is clicked
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/TransactionHistoryV2.fxml"));
            root = loader.load();

            TransactionHistoryController transactionHistoryController = loader.getController();
            transactionHistoryController.setCurrentUser(currentUser);

            // Load stage and scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public boolean rewardsButtonHandler(ActionEvent event) throws IOException {

        if(blippicard == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("You currently do not have a blippi card");
            alert.showAndWait();
            return false;
        }

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

        return true;
    }

    public void setCard(BlippiCard blippi) {
        // Clear existing components
        container.getChildren().clear();

        // Container position and size for 1920x1080 layout
        container.setLayoutX(696.0);
        container.setLayoutY(234.0);
        container.setPrefWidth(600.0);
        container.setPrefHeight(300.0);

        // Background rectangle (scaled up proportionally)
        Rectangle cardBackground = new Rectangle(596.0, 298.0);
        cardBackground.setArcWidth(40);
        cardBackground.setArcHeight(40);
        cardBackground.setFill(Color.web("#00135e"));
        cardBackground.setStroke(Color.web("#00135e"));
        cardBackground.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        cardBackground.setLayoutX(2.0);
        cardBackground.setLayoutY(1.0);
        container.getChildren().add(cardBackground);

        // Logo ImageView scaled and positioned proportionally
        InputStream imageStream = getClass().getResourceAsStream("/com/example/images/blippilogo.png");
        if (imageStream == null) {
            System.out.println("Image not found!");
        } else {
            ImageView logoImageView = new ImageView(new Image(imageStream));
            logoImageView.setFitHeight(260.0);       // Scaled up from 185
            logoImageView.setFitWidth(265.0);        // Scaled up from 189
            logoImageView.setLayoutX(345.0);         // Scaled up from 229
            logoImageView.setLayoutY(21.0);          // Scaled up from 15
            logoImageView.setPreserveRatio(true);
            logoImageView.setSmooth(true);
            container.getChildren().add(logoImageView);
        }

        // Account number label - font & position scaled proportionally
        Label accNumLabel = new Label(blippi.getCardNumber());
        accNumLabel.setTextFill(Color.WHITE);
        accNumLabel.setFont(Font.font("Arial", 25));  // scaled up from 17
        accNumLabel.setLayoutX(27.0);                  // approx 19 * 1.4
        accNumLabel.setLayoutY(37.0);                  // approx 26 * 1.4
        container.getChildren().add(accNumLabel);

        // "Valid Until" label
        Label validUntilLabel = new Label("Valid Until");
        validUntilLabel.setTextFill(Color.WHITE);
        validUntilLabel.setFont(Font.font("Arial", 14)); // scaled up from 10
        validUntilLabel.setLayoutX(30.0);                 // approx 21 * 1.4
        validUntilLabel.setLayoutY(65.0);                 // approx 46 * 1.4
        container.getChildren().add(validUntilLabel);

        // Expiration date label
        Label expDateLabel = new Label(blippi.getExpDate());
        expDateLabel.setTextFill(Color.WHITE);
        expDateLabel.setFont(Font.font("Arial", 14)); // scaled up from 10
        expDateLabel.setLayoutX(100.0);                 // approx 69 * 1.4
        expDateLabel.setLayoutY(65.0);
        container.getChildren().add(expDateLabel);

        // Card label
        Label cardLabel = new Label(blippi.getLabel());
        cardLabel.setTextFill(Color.WHITE);
        cardLabel.setFont(Font.font("Arial", 14)); // scaled up from 10
        cardLabel.setLayoutX(27.0);                 // approx same as accNumLabel x
        cardLabel.setLayoutY(188.0);                // approx 134 * 1.4
        container.getChildren().add(cardLabel);

        // "Available Balance as of" label
        Label availableBalanceLabel = new Label("Available Balance as of");
        availableBalanceLabel.setTextFill(Color.WHITE);
        availableBalanceLabel.setFont(Font.font("Arial", 13)); // scaled up from 9
        availableBalanceLabel.setLayoutX(27.0);
        availableBalanceLabel.setLayoutY(219.0);             // approx 156 * 1.4
        container.getChildren().add(availableBalanceLabel);

        // Currency symbol label ₱
        Label currencyLabel = new Label("₱");
        currencyLabel.setTextFill(Color.WHITE);
        currencyLabel.setFont(Font.font("Arial Bold", 25)); // scaled up from 17
        currencyLabel.setLayoutX(27.0);
        currencyLabel.setLayoutY(233.0);          // approx 167 * 1.4
        container.getChildren().add(currencyLabel);

        // Balance amount label
        String balance = String.format("%.2f", blippi.getBalance());
        Label balanceAmtLabel = new Label(balance);
        balanceAmtLabel.setTextFill(Color.WHITE);
        balanceAmtLabel.setFont(Font.font("Arial Bold", 25)); // scaled up from 17
        balanceAmtLabel.setLayoutX(44.0);          // approx 31 * 1.4
        balanceAmtLabel.setLayoutY(233.0);
        container.getChildren().add(balanceAmtLabel);

        // Latest transaction date
        String latestDate = null;
        File transacFile = new File("transactions.txt");
        if (transacFile.exists()) {
            try (Scanner filescanner = new Scanner(transacFile)) {
                String lastLine;
                while (filescanner.hasNextLine()) {
                    lastLine = filescanner.nextLine();
                    if (blippi.getCardNumber().equals(lastLine.split(";")[1])) {
                        latestDate = lastLine.split(";")[4];
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (latestDate == null) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a");
            latestDate = now.format(format);
        }

        // Balance date label
        Label balDateLabel = new Label(latestDate);
        balDateLabel.setTextFill(Color.WHITE);
        balDateLabel.setFont(Font.font("Arial", 13)); // scaled up from 9
        balDateLabel.setLayoutX(170.0);               // approx 115 * 1.4
        balDateLabel.setLayoutY(219.0);
        container.getChildren().add(balDateLabel);
    }
}