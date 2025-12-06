package com.jobsphere.controller;

import com.jobsphere.dao.ApplicantDAO;
import com.jobsphere.dao.ApplicationsDAO;
import com.jobsphere.dao.JobDAO;
import com.jobsphere.model.Job;
import com.jobsphere.model.SearchCompany; // The new DTO
import com.jobsphere.service.auth.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.util.List;
import java.util.ArrayList;

public class SearchCompanyController {

    @FXML private ComboBox<Job> jobComboBox;
    @FXML private ComboBox<String> skillComboBox;
    @FXML private ComboBox<Integer> expComboBox;

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

    @FXML
    public void initialize() {
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


    private void loadFilterData() {
    int currentCompanyId = SessionManager.getInstance().getCurrentUserId();
        //Observer pattern
        ObservableList<Job> myJobs = FXCollections.observableArrayList();

        //get all jobs for the current company for specific company id
        List<Job> myJobsList = jobDAO.getJobsByCompanyId(currentCompanyId);
        myJobs.addAll(myJobsList);

        jobComboBox.setItems(myJobs);

        jobComboBox.setConverter(new StringConverter<Job>() {
            @Override
            public String toString(Job job) { return (job == null) ? null : job.getTitle(); }
            @Override
            public Job fromString(String string) { return null; }
        });

        //Load Distinct Skills
        List<String> skills = applicantDAO.getDistinctSkills();
        System.out.println("DEBUG: Number of skills found = " + skills.size());
        skillComboBox.setItems(FXCollections.observableArrayList(skills));

        //Load Distinct Experience Years
        List<Integer> years = applicantDAO.getDistinctExperienceYears();
        expComboBox.setItems(FXCollections.observableArrayList(years));
    }


    @FXML
    private void handleSearch() {
        tableData.clear();
        Job selectedJob = jobComboBox.getValue();
        String selectedSkill = skillComboBox.getValue();
        Integer selectedExp = expComboBox.getValue();

        // Validation
        if (selectedJob == null || selectedSkill == null || selectedExp == null) {
            showAlert("Warning", "Please select Job, Skill, and Experience Year.");
            return;
        }

        List<SearchCompany> rawResults = appDAO.searchCandidatesByExperienceAndSkill(
                selectedJob.getId(),
                selectedExp,
                selectedSkill
        );

        if (rawResults.isEmpty()) {
            showAlert("Info", "No candidates found matching criteria.");
        } else {
            // Add results directly to the table (Observer Pattern)
            tableData.setAll(rawResults);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}