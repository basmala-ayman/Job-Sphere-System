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
    @FXML private TableView<SearchCompany> resultsTable; //observer observes the ObservableList

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

    //Observer1
    // ObservableList for the table - any change will notify observers (the TableView)
    private ObservableList<SearchCompany> tableData = FXCollections.observableArrayList();

    private int companyId;

   @FXML
    public void initialize() {
       //observer2
       //observable: it is observable, if changed, observers are changed
       //value property = observable, add listener= add observer, concrete observer= SearchCompanyController
       jobComboBox.valueProperty().addListener((
               //notify is implicit, when the job is selected, javafx notify all the listeners
               obs, oldJob, newJob) -> {
           if (newJob != null) {
               skillComboBox.setDisable(false);
               expComboBox.setDisable(false);

               skillComboBox.getSelectionModel().clearSelection();
               expComboBox.getSelectionModel().clearSelection();

               // Load skills and candidates for the selected job
               loadSkillsForJob(newJob.getId());
               loadAllCandidatesForJob(newJob.getId());
           }
       });



       //Setup Table Columns
        colJob.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSkills.setCellValueFactory(new PropertyValueFactory<>("skills"));
        colExp.setCellValueFactory(new PropertyValueFactory<>("experienceYears"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colResume.setCellValueFactory(new PropertyValueFactory<>("resumeUrl"));

        //link between (observer) table and (observable) tableData
        resultsTable.setItems(tableData);
        //Load Filter Data
        loadFilterData();
    }

    // Load all Filter Data
    private void loadFilterData() {
        this.companyId = SessionManager.getInstance().getCurrentUserId();
        //observer3, observable: my Jobs, observer: jobComboBox
        ObservableList<Job> myJobs = FXCollections.observableArrayList();
        List<Job> myJobsList = jobDAO.getJobsByCompanyId(companyId);
        myJobs.addAll(myJobsList);

        //any change in jobs list, jobComboBox will be notified
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


        skillComboBox.setDisable(true);
        expComboBox.setDisable(true);


        // Load Distinct Experience Years
        List<Integer> years = applicantDAO.getDistinctExperienceYears();
        expComboBox.setItems(FXCollections.observableArrayList(years));
    }


    // Load Skills for the selected Job when the user selects it
    private void loadSkillsForJob(int jobId) {
        Job job = jobDAO.getJobById(jobId);

        if (job == null || job.getRequirements() == null || job.getRequirements().isBlank()) {
            skillComboBox.getItems().clear();
            return;
        }

        List<String> skills = List.of(job.getRequirements().split(",\\s*"));
        skillComboBox.setItems(FXCollections.observableArrayList(skills));
    }

    // Load all Candidates for the selected Job when the user selects it without filters
    private void loadAllCandidatesForJob(int jobId) {
        tableData.clear();

        List<SearchCompany> results =
                appDAO.searchCandidatesByExperienceAndSkill(
                        jobId,
                        0,        // minimum experience
                        ""        // ignore skill
                );

        //link between (observer) table and (observable) tableData
        //any change in tableData will notify the TableView
        //table view now has the new data
        tableData.setAll(results);
    }


// search by filter when user clicks the filter button
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