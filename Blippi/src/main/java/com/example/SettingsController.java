package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SettingsController {
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
    public void returnHomeButtonHandler(ActionEvent event) throws IOException {
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
    public void settingsButtonHandler(ActionEvent event) throws IOException {
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
    public void logOutHandler(ActionEvent event) {
        currentUser = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/LogInV2.fxml"));
            root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public boolean unregisterHandler(ActionEvent event) throws IOException {
        if(blippicard == null) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setContentText("You currently do not have a blippi card");
            errorAlert.showAndWait();
            return false;
        }
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirm Card Deletion");
        alert.setContentText("You are about to unregister blippicard number " + blippicard.getCardNumber() + " from your account");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("User chose Ok");

            //delete card from txt file
            String cardToDelete = blippicard.getCardNumber();
            System.out.println(cardToDelete);

            List<String> updatedLines = new ArrayList<>();

            try(BufferedReader reader = new BufferedReader(new FileReader("blippicards.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) { // skip empty lines
                        String[] parts = line.split(";");
                        if (!parts[0].equalsIgnoreCase(cardToDelete)) {
                            updatedLines.add(line);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("blippicards.txt"))) {
                for (int i = 0; i < updatedLines.size(); i++) {
                    writer.write(updatedLines.get(i));
                    if (i < updatedLines.size() - 1) {
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            // delete associated transactions from txt file
            String transacToDelete = blippicard.getCardNumber();
            System.out.println(transacToDelete);

            List<String> updatedTransacLines = new ArrayList<>();
            try(BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) { // skip empty lines
                        String[] parts = line.split(";");
                        if (!parts[1].equalsIgnoreCase(transacToDelete)) {
                            updatedTransacLines.add(line);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt"))) {
                for (int i = 0; i < updatedTransacLines.size(); i++) {
                    writer.write(updatedTransacLines.get(i));
                    if (i < updatedTransacLines.size() - 1) {
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            //delete objects
            blippicard.getTransac().clear();
            currentUser.setBlippi(null);

            Alert deleteAlert = new Alert(AlertType.INFORMATION);
            deleteAlert.setTitle("Information Dialog");
            deleteAlert.setHeaderText("Card Deleted!");
            deleteAlert.setContentText("Blippi card " + cardToDelete + "' has been deleted.");
            deleteAlert.showAndWait();

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
            return true;
        } else {
            System.out.println("User chose Cancel");
            return false;
        }
    }

    @FXML
    public boolean deleteAccHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirm Account Deletion");
        alert.setContentText("You are about to delete your account");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("User chose OK");
            //delete accounts from txt file
            String accToDelete = currentUser.getId();
            System.out.println(accToDelete);
            
            List<String> updatedAccLines = new ArrayList<>();

            try(BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) { // skip empty lines
                        String[] parts = line.split(";");
                        if (!parts[3].equalsIgnoreCase(accToDelete)) {
                            updatedAccLines.add(line);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"))) {
                for (int i = 0; i < updatedAccLines.size(); i++) {
                    writer.write(updatedAccLines.get(i));
                    if (i < updatedAccLines.size() - 1) {
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            if(blippicard != null) {
                //delete card from txt file
                String cardToDelete = blippicard.getCardNumber();
                System.out.println(cardToDelete);

                List<String> updatedLines = new ArrayList<>();

                try(BufferedReader reader = new BufferedReader(new FileReader("blippicards.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty()) { // skip empty lines
                            String[] parts = line.split(";");
                            if (!parts[0].equalsIgnoreCase(cardToDelete)) {
                                updatedLines.add(line);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("blippicards.txt"))) {
                    for (int i = 0; i < updatedLines.size(); i++) {
                        writer.write(updatedLines.get(i));
                        if (i < updatedLines.size() - 1) {
                            writer.newLine();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                // delete associated transactions from txt file
                String transacToDelete = blippicard.getCardNumber();
                System.out.println(transacToDelete);

                List<String> updatedTransacLines = new ArrayList<>();
                try(BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty()) { // skip empty lines
                            String[] parts = line.split(";");
                            if (!parts[1].equalsIgnoreCase(transacToDelete)) {
                                updatedTransacLines.add(line);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt"))) {
                    for (int i = 0; i < updatedTransacLines.size(); i++) {
                        writer.write(updatedTransacLines.get(i));
                        if (i < updatedTransacLines.size() - 1) {
                            writer.newLine();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                blippicard.getTransac().clear();
            }

            //delete objects
            currentUser.setBlippi(null);
            currentUser = null;

            Alert deleteAlert = new Alert(AlertType.INFORMATION);
            deleteAlert.setTitle("Information Dialog");
            deleteAlert.setHeaderText("Account Deleted!");
            deleteAlert.setContentText("Your account has been deleted.");
            deleteAlert.showAndWait();

            try {
                // Load Singup.fxml when signup link is clicked
                FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SignupV2.fxml"));
                root = loader.load();

                // Load stage and scene
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User chose Cancel");
            return false;
        }

        return true;
    }

    @FXML
    public void changePassHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/ChangePasswordV2.fxml"));
            root = loader.load();

            ChangePasswordController changePasswordController = loader.getController();
            changePasswordController.setCurrentUser(currentUser);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
