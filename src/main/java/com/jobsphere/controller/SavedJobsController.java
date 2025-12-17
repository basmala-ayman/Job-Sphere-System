package com.jobsphere.controller;

import com.jobsphere.dao.SavedJobsDAO;
import com.jobsphere.model.Job;
import com.jobsphere.service.auth.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

public class SavedJobsController {

    @FXML
    private ListView<Job> savedJobList;

    private final SavedJobsDAO savedJobsDAO = new SavedJobsDAO();

    @FXML
    public void initialize() {
        int applicantId = SessionManager.getInstance().getCurrentUserId();

        if (applicantId == 0) {
            showAlert(
                    "Not Logged In",
                    "Please login to view saved jobs.",
                    Alert.AlertType.WARNING
            );
            return;
        }

        // Load saved jobs into list
        savedJobList.setItems(
                FXCollections.observableArrayList(
                        savedJobsDAO.getSavedJobsByApplicant(applicantId)
                )
        );

        // Optional: double-click to show job details
        savedJobList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showJobDetails();
            }
        });
    }

    private void showJobDetails() {
        Job job = savedJobList.getSelectionModel().getSelectedItem();
        if (job == null) return;

        showAlert(
                job.getTitle(),
                "Company ID: " + job.getCompanyId() + "\n\n" +
                        "Description:\n" + job.getDescription() + "\n\n" +
                        "Requirements:\n" + job.getRequirements() + "\n\n" +
                        "Responsibilities:\n" + job.getResponsibilities() + "\n\n" +
                        "Salary: " + job.getSalary(),
                Alert.AlertType.INFORMATION
        );
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
