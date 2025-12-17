package com.jobsphere.controller;

import com.jobsphere.service.auth.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicantFeaturesController {

    @FXML
    private void goToSearch(ActionEvent event) {
        navigate(event, "/layouts/JobSearch.fxml", "Search For a Job");
    }

    @FXML
    private void goToJobDetails(ActionEvent event) {
        navigate(event, "/layouts/JobDetails.fxml", "Job Details");
    }

    /**
     * Helper method to handle closing the current window and opening a new one.
     */
    private void navigate(ActionEvent event, String fxmlPath, String title) {
        try {
            // 1. Load the new FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // 2. Create and show the new Stage
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle(title);
            newStage.show();

            // 3. Close the current Stage (Old Window)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading page: " + title);
        }
    }

    @FXML
    private void onLogout(ActionEvent event) {
        SessionManager.getInstance().clearCurrentUser();
        navigate(event, "/layouts/Login.fxml", "Login");
    }

}
