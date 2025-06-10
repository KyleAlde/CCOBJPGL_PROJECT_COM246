package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

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
    public boolean signUpButtonController(ActionEvent event) {
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
        

        User user = new User(username, password, phoneOrEmail, id);


        if(username.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No username provided");
            return false;
        }

        if(password.length() == 0)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No password provided");
            return false;
        }

        if(phoneOrEmail.length() == 0)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No phone number or email provided");
            return false;
        }

        try{
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("accounts.txt", true));

            myWriter.newLine();
            myWriter.write(user.getUsername() + ";" + user.getPassword() + ";" + user.getContact() + ";" + user.getId());
            myWriter.close();

            // Load Home.fxml when signup is successful
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
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
    public void loginLinkController(ActionEvent event) throws IOException{
        // Load Login.fxml when login link is clicked
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
