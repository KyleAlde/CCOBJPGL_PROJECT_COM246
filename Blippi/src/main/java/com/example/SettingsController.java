package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SettingsController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label usernamelabel;

    //Method for setting current user from LogIn/SignUp
    private User currentUser;
    private BlippiCard blippicard;
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippicard = user.getBlippi();
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
}
