package com.jobsphere.controller;

import com.jobsphere.dao.JobDAO;
import com.jobsphere.dao.ApplicationsDAO;
import com.jobsphere.dao.SavedJobsDAO;
import com.jobsphere.model.Job;
import com.jobsphere.service.auth.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class JobDetailsController {

    @FXML
    private ListView<Job> jobList;
    @FXML
    private Label jobTitle;
    @FXML
    private Label jobCompany;
    @FXML
    private TextArea jobDesc;
    @FXML
    private TextArea jobRequirements;
    @FXML
    private TextArea jobResponsibilities;
    @FXML
    private Label jobSalary;

    @FXML
    private TextField resumeLinkField;

    @FXML
    private Button applyButton;
    @FXML
    private Button saveButton;

    private final JobDAO jobDAO = JobDAO.getInstance();
    private final ApplicationsDAO applicationsDAO = ApplicationsDAO.getInstance();
    private final SavedJobsDAO savedJobsDAO = new SavedJobsDAO();

    public void initialize() {
        jobList.setItems(FXCollections.observableList(jobDAO.getAllJobs()));

        jobList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldJob, newJob) -> {
                    if (newJob != null) {
                        showJobDetails(newJob);
                    }
                }
        );

        applyButton.setOnAction(e -> applyJob());
        saveButton.setOnAction(e -> saveJob());
    }


    @FXML
    private void applyJob() {
        Job selectedJob = jobList.getSelectionModel().getSelectedItem();
        if (selectedJob == null) return;

        int applicantId = SessionManager.getInstance().getCurrentUserId();
        if (applicantId == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not Logged In");
            alert.setHeaderText(null);
            alert.setContentText("Please login before applying for a job!");
            alert.showAndWait();
            return;
        }

        String resumeLink = resumeLinkField.getText();
        if (resumeLink == null || resumeLink.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Resume Required");
            alert.setHeaderText(null);
            alert.setContentText("Please paste your resume link (Google Drive, etc).");
            alert.showAndWait();
            return;
        }

        boolean applied = applicationsDAO.applyForJob(
                applicantId,
                selectedJob.getId(),
                resumeLink
        );

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Application Status");
        alert.setHeaderText(null);
        alert.setContentText(
                applied ? "You have applied successfully!"
                        : "You have already applied for this job."
        );
        alert.showAndWait();
    }

    @FXML
    private void saveJob() {
        Job selectedJob = jobList.getSelectionModel().getSelectedItem();
        if (selectedJob == null) return;

        int applicantId = SessionManager.getInstance().getCurrentUserId();
        if (applicantId == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not Logged In");
            alert.setHeaderText(null);
            alert.setContentText("Please login before saving a job!");
            alert.showAndWait();
            return;
        }

        savedJobsDAO.saveJob(applicantId, selectedJob.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Saved");
        alert.setHeaderText(null);
        alert.setContentText("Job has been saved successfully!");
        alert.showAndWait();
    }


    private void showJobDetails(Job job) {
        jobTitle.setText(job.getTitle());
        jobCompany.setText(String.valueOf(job.getCompanyId()));
        jobDesc.setText(job.getDescription());
        jobRequirements.setText(job.getRequirements());

        if (jobResponsibilities != null) {
            jobResponsibilities.setText(job.getResponsibilities());
        }
        if (jobSalary != null) {
            jobSalary.setText(job.getSalary());
        }
    }

    @FXML
    private void openSavedJobsScreen() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/layouts/SavedJobs.fxml"));

            Stage stage = new Stage();
            stage.setTitle("Saved Jobs");
            stage.setScene(new Scene(loader.load()));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Cannot open Saved Jobs screen");
            alert.showAndWait();
        }
    }
}
