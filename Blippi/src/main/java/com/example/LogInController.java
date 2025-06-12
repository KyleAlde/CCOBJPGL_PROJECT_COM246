package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class LogInController {
    @FXML
    TextField usernameField;

    @FXML
    TextField numberField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button loginButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public User user;

    @FXML
    public void loginButtonController(ActionEvent event) throws IOException {
        String phoneOrEmail = numberField.getText();
        String password = passwordField.getText();
        boolean accountExists = false;

        if(phoneOrEmail.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("No email or phone number provided");
            alert.showAndWait();
        }

        if(password.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("No password provided");
            alert.showAndWait();
        }

        File accountsfile = new File("accounts.txt");

        if(accountsfile.exists()) {
            Scanner filescanner;

            try {
                filescanner = new Scanner(accountsfile);

                while (filescanner.hasNextLine()) {
                    String data = filescanner.nextLine();

                    String username_from_file = data.split(";")[0];
                    String password_from_file = data.split(";")[1];
                    String number_from_file = data.split(";")[2];
                    String id_from_file = data.split(";")[3];

                    String username = username_from_file;
                    String id = id_from_file;
                    
                    user = new User(username, password, phoneOrEmail, id);

                    //Check if the login info entered matches any record in the accounts.txt file
                    if(number_from_file.equals(user.getContact()) && password_from_file.equals(user.getPassword())) {
                        // Load Home.fxml when login button is clicked
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Home.fxml"));
                        root = loader.load();

                        //Set current user for the blippi card set-up
                        HomeController homeController = loader.getController();
                        homeController.setCurrentUser(user);
                        homeController.initializeUsername();

                        //Check if user already has blippi card
                        if(searchCard(user) != null) {
                            homeController.addCard(searchCard(user));
                        }

                        // Load stage and scene
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        accountExists = true;
                        break;
                    }
                }

                if (!accountExists && (phoneOrEmail.length() > 0 && password.length() > 0)) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setContentText("Incorrect email or password");  
                    alert.showAndWait();
                }
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("accounts.txt file cannot be read");
        }
    }

    @FXML
    public void signUpLinkController(ActionEvent event) throws IOException {
        // Load Singup.fxml when signup link is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Signup.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
