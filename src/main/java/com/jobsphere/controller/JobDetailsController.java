package com.jobsphere.controller;


import com.jobsphere.dao.JobDAO;
import com.jobsphere.dao.ApplicationsDAO;
import com.jobsphere.dao.SavedJobsDAO;
import com.jobsphere.model.Job;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
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

    private JobDAO jobDAO = JobDAO.getInstance();
    // private ApplicationsDAO applicationsDAO = new ApplicationsDAO();
    private ApplicationsDAO applicationsDAO = ApplicationsDAO.getInstance();
    private SavedJobsDAO savedJobsDAO = new SavedJobsDAO();
    private int currentApplicantnId;


    public void initialize() {
        jobList.setItems(FXCollections.observableList(jobDAO.getAllJobs()));
        jobList.getSelectionModel().selectedItemProperty().addListener((obs, oldJob, newJob) -> {
            if(newJob != null) {
                showJobDetails(newJob);
            }
        });
        applyButton.setOnAction(e -> {applyJob();});
        saveButton.setOnAction(e -> {saveJob();});
    }
    @FXML
    private void applyJob() {
        Job selectedJob = jobList.getSelectionModel().getSelectedItem();
        if(selectedJob == null) return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Resume");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Resume File", "*.pdf"));
        File resumeFile = fileChooser.showOpenDialog(applyButton.getScene().getWindow());
        if(resumeFile != null) {
         boolean applied = applicationsDAO.applyForJob(currentApplicantnId , selectedJob.getId() , resumeFile.getAbsolutePath());

         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Application Applied");
         alert.setHeaderText(null);
         alert.setContentText(applied ? "You have applied successfully!" : "You have already applied for this job.");
         alert.showAndWait();

        }
    }
    @FXML
    private void saveJob() {
        Job selectedJob = jobList.getSelectionModel().getSelectedItem();
        if(selectedJob == null) return;
        savedJobsDAO.saveJob(currentApplicantnId , selectedJob.getId());
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
        if(jobResponsibilities != null) {
            jobResponsibilities.setText(job.getResponsibilities());
        }
        if(jobSalary != null) {
            jobSalary.setText(job.getSalary());
        }
    }

    public void setCurrentApplicantnId(int applicantnId) {
        this.currentApplicantnId = applicantnId;
    }
}
