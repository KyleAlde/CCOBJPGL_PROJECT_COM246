package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SignUpController {
    @FXML
    TextField usernameField;

    @FXML
    TextField numberField;

    @FXML
    PasswordField passwordField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public boolean signUpButtonHandler(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String phoneOrEmail = numberField.getText();
        

        username = username.trim();
        password = password.trim();
        phoneOrEmail = phoneOrEmail.trim();

        //Generate user id number
        Random r = new Random();
        int r1 = r.nextInt(1000);
        String id = "25-" + r1;
        System.out.println(id);
        

        User user = new User(username, password, phoneOrEmail, id, null);


        if(!inputValidator(username, password, phoneOrEmail)) {
            return false;
        }

        try{
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("accounts.txt", true));

            myWriter.newLine();
            myWriter.write(user.getUsername() + ";" + user.getPassword() + ";" + user.getContact() + ";" + user.getId());
            myWriter.close();

            // Load Home.fxml when signup is successful
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Home.fxml"));
            root = loader.load();

            //Set current user for the blippi card set-up
            HomeController homeController = loader.getController();
            homeController.setCurrentUser(user);

            // Load stage and scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
            System.out.println("An error occurred.");
            return false;
        }
        return true;
    }

    @FXML
    public void loginLinkHandler(ActionEvent event) throws IOException{
        // Load Login.fxml when login link is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Login.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public boolean inputValidator(String username, String password, String phoneOrEmail) {
        if(username.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("No name provided");
            alert.showAndWait();
            return false;
        }

        if(phoneOrEmail.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("No email or phone number provided");
            alert.showAndWait();
            return false;
        }

        if(password.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("No password provided");
            alert.showAndWait();
            return false;
        }
        
        for (int i = 0; i < phoneOrEmail.length(); i++) {
            // Check whether each character is a letter or special character
            if (Character.isLetterOrDigit(phoneOrEmail.charAt(i)) && !Character.isLetter(phoneOrEmail.charAt(i))) {
                if(phoneOrEmail.length() != 11) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText("Input not valid");
                    alert.setContentText("The phone number you entered is invalid");
                    alert.showAndWait();
                    return false;
                }
            } else {
                if((!phoneOrEmail.contains("@") && !phoneOrEmail.contains(".")) || phoneOrEmail.length() > 50) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText("Input not valid");
                    alert.setContentText("Please enter a valid email address or phone number");
                    alert.showAndWait();
                    return false;
                }
            }
        }

        if(password.length() > 24) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("Password must not exceed 24 characters");
            alert.showAndWait();
            return false;
        }

        if(password.length() > 24) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("Password must have at least 3 characters");
            alert.showAndWait();
            return false;
        }

        for (int i = 0; i < username.length(); i++) {
            // Check whether each character is a special character
            if (!Character.isLetter(username.charAt(i)) &&  !username.contains(".") &&  !username.contains("-") && !username.contains("'") && !username.contains(" ") ) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Input not valid");
                alert.setContentText("Name must not contain special characters");
                alert.showAndWait();
                return false;
            }
        }

        SearchData searchData = new SearchData();
        if(searchData.searchUser(phoneOrEmail) != null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("The phone or email address you entered is already in use");
            alert.showAndWait();
            return false;
        }

        return true;
    }
}
