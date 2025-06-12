package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    //Change username label
    public void initializeUsername() {
        String username = currentUser.getUsername();
        usernamelabel.setText(username);
    }

    public void addCardButtonController(ActionEvent event) throws IOException{
        // Load AddCard.fxml when add card button is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/AddCard.fxml"));
        root = loader.load();

        //Set current user for the blippi card set-up
        AddCardController setUser = loader.getController();
        setUser.setCurrentUser(currentUser);

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public boolean blippiloadController(ActionEvent event) throws IOException {
        if(searchCard(currentUser) == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("You currently do not have a blippi card");
            alert.showAndWait();
            return false;
        }

        // Load BuyLoad.fxml when qr ticket button is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/BuyLoad.fxml"));
        root = loader.load();

        //Set currentUser
        BuyLoadController buyLoadController = loader.getController();
        buyLoadController.setCurrentUser(currentUser);

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        return true;
    }

    public void blippitixController(ActionEvent event) throws IOException {
        // Load QrTicket.fxml when qr ticket button is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/QrTicket.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void blippitransacController(ActionEvent event) throws IOException {
        // Load TransactionHistory.fxml when transaction history button is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/TransactionHistory.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addCard(BlippiCard blippi) {
        // Clear existing components
        container.getChildren().clear();

        container.setLayoutX(476.0);
        container.setLayoutY(170.0);
        container.setPrefWidth(405.0);
        container.setPrefHeight(214.0);

        // Create the background rectangle
        Rectangle cardBackground = new Rectangle(401.0, 212.0);
        cardBackground.setArcWidth(40);
        cardBackground.setArcHeight(40);
        cardBackground.setFill(Color.web("#00135e"));
        cardBackground.setStroke(Color.web("#00135e"));
        cardBackground.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        cardBackground.setLayoutX(2.0);
        cardBackground.setLayoutY(1.0);
        container.getChildren().add(cardBackground);

        // Load the logo image
        InputStream imageStream = getClass().getResourceAsStream("/com/example/images/blippilogo.png");
        if (imageStream == null) {
            System.out.println("Image not found!");
        } else {
            System.out.println("Image loaded successfully!");
            ImageView logoImageView = new ImageView(new Image(imageStream));
            logoImageView.setFitHeight(185.0);
            logoImageView.setFitWidth(189.0);
            logoImageView.setLayoutX(229.0);
            logoImageView.setLayoutY(15.0);
            logoImageView.setPreserveRatio(true);
            logoImageView.setSmooth(true);
            container.getChildren().add(logoImageView);
        }

        // Account number label
        Label accNumLabel = new Label(blippi.getCardNumber());
        accNumLabel.setTextFill(Color.WHITE);
        accNumLabel.setFont(Font.font("Arial", 17));
        accNumLabel.setLayoutX(19.0);
        accNumLabel.setLayoutY(26.0);
        container.getChildren().add(accNumLabel);

        // Valid Until label
        Label validUntilLabel = new Label("Valid Until");
        validUntilLabel.setTextFill(Color.WHITE);
        validUntilLabel.setFont(Font.font("Arial", 10));
        validUntilLabel.setLayoutX(21.0);
        validUntilLabel.setLayoutY(46.0);
        container.getChildren().add(validUntilLabel);

        // Expiration date label
        Label expDateLabel = new Label(blippi.getExpDate());
        expDateLabel.setTextFill(Color.WHITE);
        expDateLabel.setFont(Font.font("Arial", 10));
        expDateLabel.setLayoutX(69.0);
        expDateLabel.setLayoutY(46.0);
        container.getChildren().add(expDateLabel);

        // Card label
        Label cardLabel = new Label(blippi.getLabel());
        cardLabel.setTextFill(Color.WHITE);
        cardLabel.setFont(Font.font("Arial", 10));
        cardLabel.setLayoutX(19.0);
        cardLabel.setLayoutY(129.0);
        container.getChildren().add(cardLabel);

        // Available Balance label
        Label availableBalanceLabel = new Label("Available Balance as of");
        availableBalanceLabel.setTextFill(Color.WHITE);
        availableBalanceLabel.setFont(Font.font("Arial", 9));
        availableBalanceLabel.setLayoutX(19.0);
        availableBalanceLabel.setLayoutY(153.0);
        container.getChildren().add(availableBalanceLabel);

        // Generate current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a");
        String formattedDate = now.format(format);

        // Balance date label
        Label balDateLabel = new Label(formattedDate);
        balDateLabel.setTextFill(Color.WHITE);
        balDateLabel.setFont(Font.font("Arial", 9));
        balDateLabel.setLayoutX(115.0);
        balDateLabel.setLayoutY(153.0);
        container.getChildren().add(balDateLabel);

        // Currency symbol label
        Label currencyLabel = new Label("₱");
        currencyLabel.setTextFill(Color.WHITE);
        currencyLabel.setFont(Font.font("Arial Bold", 17));
        currencyLabel.setLayoutX(19.0);
        currencyLabel.setLayoutY(167.0);
        container.getChildren().add(currencyLabel);

        // Balance amount label
        Label balanceAmtLabel = new Label(Integer.toString(blippi.getBalance()));
        balanceAmtLabel.setTextFill(Color.WHITE);
        balanceAmtLabel.setFont(Font.font("Arial Bold", 17));
        balanceAmtLabel.setLayoutX(31.0);
        balanceAmtLabel.setLayoutY(167.0);
        container.getChildren().add(balanceAmtLabel);
    }

    public BlippiCard searchCard(User user) {
        String userId = user.getId();

        File blippiFile = new File("blippicards.txt");

        if(blippiFile.exists()) {
            Scanner filescanner;

            try {
                filescanner = new Scanner(blippiFile);

                //Look for the card with the matching userId
                while(filescanner.hasNextLine()) {
                    String data = filescanner.nextLine();

                    String cardnum_from_file = data.split(";")[0];
                    String balance_from_file = data.split(";")[1];
                    String label_from_file = data.split(";")[2];
                    String exp_from_file = data.split(";")[3];
                    String id_from_file = data.split(";")[4];

                    if(id_from_file.equals(userId)) {
                        //Create BlippiCard object from database info
                        BlippiCard blippi = new BlippiCard(cardnum_from_file, Integer.valueOf(balance_from_file), label_from_file, exp_from_file, id_from_file);
                        return blippi;
                    }
                }
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("blippicards.txt file cannot be read");
        }
        return null;
    }
}