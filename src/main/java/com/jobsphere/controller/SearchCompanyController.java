package com.jobsphere.controller;

import com.jobsphere.dao.ApplicantDAO;
import com.jobsphere.dao.ApplicationsDAO;
import com.jobsphere.dao.JobDAO;
import com.jobsphere.model.Job;
import com.jobsphere.model.SearchCompany; // The new DTO
import com.jobsphere.service.auth.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
public class SearchCompanyController {

    @FXML private ComboBox<Job> jobComboBox;
    @FXML private ComboBox<String> skillComboBox;
    @FXML private ComboBox<Integer> expComboBox;
    @FXML private Button backBtn;
    @FXML private TableView<SearchCompany> resultsTable;


    @FXML private TableColumn<SearchCompany, String> colJob;
    @FXML private TableColumn<SearchCompany, String> colStatus;
    @FXML private TableColumn<SearchCompany, String> colName;
    @FXML private TableColumn<SearchCompany, String> colSkills;
    @FXML private TableColumn<SearchCompany, Integer> colExp;
    @FXML private TableColumn<SearchCompany, String> colEmail;
    @FXML private TableColumn<SearchCompany, String> colResume;

    // DAOs
    private ApplicationsDAO appDAO = ApplicationsDAO.getInstance();
    private ApplicantDAO applicantDAO = ApplicantDAO.getInstance();
    private JobDAO jobDAO = JobDAO.getInstance();

    private ObservableList<SearchCompany> tableData = FXCollections.observableArrayList();
    private int companyId;

   @FXML
    public void initialize() {

       jobComboBox.valueProperty().addListener((obs, oldJob, newJob) -> {
           if (newJob != null) {
               skillComboBox.setDisable(false);
               expComboBox.setDisable(false);

               skillComboBox.getSelectionModel().clearSelection();
               expComboBox.getSelectionModel().clearSelection();

               loadSkillsForJob(newJob.getId());
               loadAllCandidatesForJob(newJob.getId());
           }
       });



       // 1. Setup Table Columns
        colJob.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSkills.setCellValueFactory(new PropertyValueFactory<>("skills"));
        colExp.setCellValueFactory(new PropertyValueFactory<>("experienceYears"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colResume.setCellValueFactory(new PropertyValueFactory<>("resumeUrl"));

        resultsTable.setItems(tableData);

        // 2. Load Filter Data
        loadFilterData();
    }

    private void loadSkillsForJob(int jobId) {
        Job job = jobDAO.getJobById(jobId);

        if (job == null || job.getRequirements() == null || job.getRequirements().isBlank()) {
            skillComboBox.getItems().clear();
            return;
        }

        List<String> skills = List.of(job.getRequirements().split(",\\s*"));
        skillComboBox.setItems(FXCollections.observableArrayList(skills));
    }

    private void loadAllCandidatesForJob(int jobId) {
        tableData.clear();

        List<SearchCompany> results =
                appDAO.searchCandidatesByExperienceAndSkill(
                        jobId,
                        0,        // minimum experience
                        ""        // ignore skill
                );

        tableData.setAll(results);
    }


    private void loadFilterData() {
        this.companyId = SessionManager.getInstance().getCurrentUserId();

        ObservableList<Job> myJobs = FXCollections.observableArrayList();
        List<Job> myJobsList = jobDAO.getJobsByCompanyId(companyId);
        myJobs.addAll(myJobsList);

        jobComboBox.setItems(myJobs);

        jobComboBox.setConverter(new StringConverter<Job>() {
            @Override
            public String toString(Job job) {
                return job == null ? null : job.getTitle();
            }

            @Override
            public Job fromString(String string) {
                return null;
            }
        });

        // 🔹 Disable filters until job selected
        skillComboBox.setDisable(true);
        expComboBox.setDisable(true);

        // 🔹 Placeholders
        jobComboBox.setPromptText("Select Job");
        skillComboBox.setPromptText("Select Skill");
        expComboBox.setPromptText("Experience");

        // Load Distinct Experience Years
        List<Integer> years = applicantDAO.getDistinctExperienceYears();
        expComboBox.setItems(FXCollections.observableArrayList(years));
    }



    @FXML
    private void handleSearch() {
        Job selectedJob = jobComboBox.getValue();
        String selectedSkill = skillComboBox.getValue();
        Integer selectedExp = expComboBox.getValue();

        if (selectedJob == null) {
            showAlert("Warning", "Please select a Job first.");
            return;
        }

        List<SearchCompany> results;

        if (selectedSkill == null && selectedExp == null) {
            loadAllCandidatesForJob(selectedJob.getId());
            return;
        }

        results = appDAO.searchCandidatesByExperienceAndSkill(
                selectedJob.getId(),
                selectedExp != null ? selectedExp : 0,
                selectedSkill != null ? selectedSkill : ""
        );

        tableData.setAll(results);
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/CompanyFeatures.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load CompanyFeatures screen.");
        }
    }
}