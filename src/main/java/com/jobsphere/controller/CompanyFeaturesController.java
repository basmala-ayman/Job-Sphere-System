package com.jobsphere.controller;

import com.jobsphere.service.auth.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class CompanyFeaturesController {

    @FXML Button addJobButton;
    @FXML Button manageJobsButton;
    @FXML Button candidateSearch;
    @FXML Button appManagement;

    @FXML
    private void onClickAddJob(ActionEvent event) {
        navigate(event, "/layouts/JobForm.fxml", "Add New Job");
    }

    @FXML
    private void onClickManageJobs(ActionEvent event) {
        navigate(event, "/layouts/ManageJobs.fxml", "Manage Jobs");
    }

    @FXML
    private void toCandidateSearch(ActionEvent event) {
        navigate(event, "/layouts/SearchCompany.fxml", "Candidate Search");
    }

    @FXML
    private void toAppManagement(ActionEvent event) {
        navigate(event, "/layouts/ApplicationsView.fxml", "Applications Management");
    }
    @FXML
    private void onLogout(ActionEvent event) {
        SessionManager.getInstance().clearCurrentUser();
        navigate(event, "/layouts/Login.fxml", "Login");
    }


    /**
     * Helper method to handle closing the current window and opening a new one.
     */
    private void navigate(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle(title);
            newStage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading page: " + title);
        }
    }
}