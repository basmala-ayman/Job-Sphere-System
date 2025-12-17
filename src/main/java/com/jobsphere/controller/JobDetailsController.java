package com.jobsphere.controller;

import com.jobsphere.dao.JobDAO;
import com.jobsphere.dao.ApplicationsDAO;
import com.jobsphere.dao.SavedJobsDAO;
import com.jobsphere.model.Job;
import com.jobsphere.service.auth.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.net.URL;


import java.io.File;

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
    private Button applyButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button uploadResumeButton;
    @FXML
    private Label uploadedFileLabel;
    @FXML
    private File selectedResumeFile;


    private JobDAO jobDAO = JobDAO.getInstance();
    private ApplicationsDAO applicationsDAO = ApplicationsDAO.getInstance();
    private SavedJobsDAO savedJobsDAO = new SavedJobsDAO();

    public void initialize() {
        jobList.setItems(FXCollections.observableList(jobDAO.getAllJobs()));
        jobList.getSelectionModel().selectedItemProperty().addListener((obs, oldJob, newJob) -> {
            if (newJob != null) {
                showJobDetails(newJob);
            }
        });
        uploadResumeButton.setOnAction(e -> selectResumeFile());
        applyButton.setOnAction(e -> applyJob());
        saveButton.setOnAction(e -> saveJob());
    }
    private void selectResumeFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Resume File", "*.pdf"));
        selectedResumeFile = fileChooser.showOpenDialog(uploadResumeButton.getScene().getWindow());

        if (selectedResumeFile != null) {
            uploadedFileLabel.setText(selectedResumeFile.getName());
        }
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

        if (selectedResumeFile == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Resume Uploaded");
            alert.setHeaderText(null);
            alert.setContentText("Please upload your resume before applying.");
            alert.showAndWait();
            return;
        }

        boolean applied = applicationsDAO.applyForJob(applicantId, selectedJob.getId(), selectedResumeFile.getAbsolutePath());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Application Status");
        alert.setHeaderText(null);
        alert.setContentText(applied ? "You have applied successfully!" : "You have already applied for this job.");
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
