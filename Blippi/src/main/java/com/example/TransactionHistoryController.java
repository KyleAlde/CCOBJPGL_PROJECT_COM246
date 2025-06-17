package com.example;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class TransactionHistoryController {
    ObservableList<Transaction> transacList = FXCollections.observableArrayList();

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, Transaction> transactionCol;

    @FXML
    private TableColumn<Transaction, String> typeCol;

    @FXML
    private TableColumn<Transaction, Float> amountCol;

    @FXML
    private Label usernamelabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;
    private BlippiCard blippiCard;
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
        System.out.println(blippiCard.getCardNumber());
        // Set column headers to be invisible
        transactionCol.setText(""); // Set header text to empty
        typeCol.setText(""); // Set header text to empty
        amountCol.setText(""); // Set header text to empty
        usernamelabel.setText(user.getUsername());
        loadData();
    }

    @FXML
    public void initialize() {
        System.out.println("transactionCol initialized? " + (transactionCol != null));
    }

    private void loadData() {
        transacList.clear();
        transactionTable.setVisible(true);
        if (transactionCol == null) {
            System.out.println("transactionCol is null! Check your FXML.");
        }
        System.out.println("Loading data...");

        try {
            File transacFile = new File("transactions.txt");
            
            if(transacFile.exists()) {
                Scanner filescanner = new Scanner(transacFile);

                while(filescanner.hasNextLine()) {
                    String data = filescanner.nextLine();

                    String id_from_file = data.split(";")[0];
                    String cardnum_from_file = data.split(";")[1];
                    String type_from_file = data.split(";")[2];
                    String amount_from_file = data.split(";")[3];
                    String date_from_file = data.split(";")[4];

                    if(cardnum_from_file.equals(blippiCard.getCardNumber())) {
                        Transaction latestTransac = new Transaction(id_from_file, cardnum_from_file, type_from_file, Float.valueOf(amount_from_file), date_from_file);
                        transacList.add(latestTransac);
                        System.out.println("Transaction added");
                    } else {
                        System.out.println("Transaction not added");
                    }
                }
                filescanner.close();

            } else {
                System.out.println("File can't be read");
            }

            // For the transaction column
            transactionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
            transactionCol.setCellFactory(col -> new TableCell<Transaction, Transaction>() {
                @Override
                protected void updateItem(Transaction tx, boolean empty) {
                    super.updateItem(tx, empty);
                    if (empty || tx == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        VBox vbox = new VBox(2);
                        Label mainLabel = new Label(tx.getType());
                        Label dateLabel = new Label(tx.getDate());
                        mainLabel.setStyle("-fx-font-size: 18px;");
                        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
                        vbox.getChildren().addAll(mainLabel, dateLabel);
                        setGraphic(vbox);
                        setText(null);
                    }
                    setStyle("-fx-background-color: transparent;"); // Make background transparent
                }
            });

            // For the type column
            typeCol.setCellValueFactory(cellData -> {
                Transaction tx = cellData.getValue();
                String typeStr = tx.getType();
                String display = ("blippi Buy Load".equals(typeStr) || "blippi Rewards".equals(typeStr)) ? "-" : "LRT/MRT Service";
                return new SimpleStringProperty(display);
            });
            typeCol.setCellFactory(col -> new TableCell<Transaction, String>() {
                @Override
                protected void updateItem(String type, boolean empty) {
                    super.updateItem(type, empty);
                    if (empty || type == null) {
                        setText(null);
                        setStyle("-fx-background-color: transparent;"); // Make background transparent
                    } else {
                        HBox hbox = new HBox();
                        hbox.setAlignment(Pos.CENTER); // Center the content
                        Label label = new Label(type);
                        hbox.getChildren().add(label);
                        label.setStyle("-fx-font-size: 17px;");
                        setGraphic(hbox);
                        setText(null);
                    }
                }
            });

            // For the amount column
            amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
            amountCol.setCellFactory(col -> new TableCell<Transaction, Float>() {
                @Override
                protected void updateItem(Float amount, boolean empty) {
                    super.updateItem(amount, empty);
                    if (empty || amount == null) {
                        setText(null);
                        setStyle("-fx-background-color: transparent;"); // Make background transparent
                    } else {
                        String formatted = (amount >= 0 ? "+" : "") + String.format("%.2f", amount);
                        setText(formatted);
                        setStyle(amount >= 0 ? "-fx-font-size: 17px; -fx-text-fill: green; -fx-background-color: transparent;" : "-fx-font-size: 17px; -fx-text-fill: red; -fx-background-color: transparent;"); // Make background transparent
                    }
                }
            });

            System.out.println("Transactions count: " + transacList.size());
            transactionTable.setItems(transacList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void allFilterHandler(ActionEvent event) {
        // Show all transactions
        transactionTable.setItems(transacList);
    }

    @FXML
    public void trainServiceHandler(ActionEvent event) {
        // Filter for LRT/MRT Service transactions
        ObservableList<Transaction> filteredList = FXCollections.observableArrayList();
        for (Transaction transaction : transacList) {
            if (!("blippi Buy Load".equals(transaction.getType()) || "blippi Rewards".equals(transaction.getType()))) {
                filteredList.add(transaction);
            }
        }
        transactionTable.setItems(filteredList);
    }

    @FXML
    public void loadFilterHandler(ActionEvent event) {
        // Filter for blippi Buy Load transactions
        ObservableList<Transaction> filteredList = FXCollections.observableArrayList();
        for (Transaction transaction : transacList) {
            if ("blippi Buy Load".equals(transaction.getType())) {
                filteredList.add(transaction);
            }
        }
        transactionTable.setItems(filteredList);
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

