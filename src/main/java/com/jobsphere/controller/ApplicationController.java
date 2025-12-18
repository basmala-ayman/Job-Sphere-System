package com.jobsphere.controller;

import com.jobsphere.model.ApplicationFullInfo;
import com.jobsphere.service.ApplicationsService;
import com.jobsphere.service.auth.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationController {

    @FXML
    private TableView<ApplicationFullInfo> applicationsTable;
    @FXML
    private TableColumn<ApplicationFullInfo, String> userNameColumn;
    @FXML
    private TableColumn<ApplicationFullInfo, String> emailColumn;
    @FXML
    private TableColumn<ApplicationFullInfo, String> jobTitleColumn;
    @FXML
    private TableColumn<ApplicationFullInfo, String> skillsColumn;
    @FXML
    private TableColumn<ApplicationFullInfo, String> statusColumn;
    @FXML
    private TableColumn<ApplicationFullInfo, Void> saveColumn;

    private ApplicationsService applicationService;
    private int companyId;

    @FXML
    public void initialize() {

        applicationService = new ApplicationsService();

        userNameColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        jobTitleColumn.setCellValueFactory(cellData -> cellData.getValue().jobTitleProperty());
        skillsColumn.setCellValueFactory(cellData -> cellData.getValue().skillsProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        setupStatusComboBox();

        setupSaveButtonColumn();

        setCompanyId();
    }

    //this is just taking the data based on the dynamic id i will take after log in 
    public void setCompanyId() {
        this.companyId = SessionManager.getInstance().getCurrentUserId();
        loadApplications();
    }

    private void loadApplications() {
        applicationsTable.setItems(FXCollections.observableArrayList(
                applicationService.getApplicationsForCompany(companyId)
        ));
    }

    private void setupStatusComboBox() {
        statusColumn.setCellFactory(col -> new TableCell<>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            {
                comboBox.getItems().addAll("pending", "accepted", "rejected");
                comboBox.setOnAction(event -> {
                    ApplicationFullInfo app = getTableView().getItems().get(getIndex());
                    if (app != null) {
                        app.setStatus(comboBox.getValue());
                    }
                });
            }

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    comboBox.setValue(status);
                    setGraphic(comboBox);
                }
            }
        });
    }

    private void setupSaveButtonColumn() {
        saveColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Save");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                btn.setOnAction(event -> {
                    ApplicationFullInfo app = getTableView().getItems().get(getIndex());
                    if (app != null) {
                        boolean success = applicationService.updateApplicationStatus(
                                app.getApplicationId(),
                                app.getStatus()
                        );
                        showAlert(success ? "Status updated" : "Update failed");
                    }
                });

                setGraphic(btn);
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/CompanyFeatures.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Could not load the previous screen.");
            alert.show();
        }
    }
}
