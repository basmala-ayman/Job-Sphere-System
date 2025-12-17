package com.jobsphere.controller;

import com.jobsphere.service.auth.SessionManager;
import com.jobsphere.dao.JobDAO;
import com.jobsphere.model.CompanyJobBuilder;
import com.jobsphere.model.Job;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class JobFormController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextArea requirementsArea;

    @FXML
    private ComboBox<String> careerLevelCombo;

    @FXML
    private ComboBox<String> jobTypeCombo;

    @FXML
    private ComboBox<String> workplaceCombo;

    @FXML
    private TextField countryField;

    @FXML
    private TextField cityField;

    @FXML
    private ComboBox<String> jobCategoryCombo;
    @FXML
    private TextArea responsibilitiesArea;

    @FXML
    private TextField salaryField;

    private Job toBeEditedJob=null;
    private JobDAO jobDAO=JobDAO.getInstance();

    @FXML
    public void initialize() {
        //initialize the combo box by values on loading
        jobCategoryCombo.getItems().addAll(
                "IT/Software Development", "Engineering", "Design/Art",
                "Marketing", "Finance", "HR", "Sales"
        );
        careerLevelCombo.getItems().addAll("Entry Level", "Mid Level", "Senior Level", "Manager");
        jobTypeCombo.getItems().addAll("Full-Time", "Part-Time", "Internship");
        workplaceCombo.getItems().addAll("Onsite", "Remote", "Hybrid");

    }

    //load the form with all the job fields already set
    public void setEditMode(Job job) {
        if(job!=null){
            toBeEditedJob=job;
            titleField.setText(job.getTitle());
            responsibilitiesArea.setText(job.getResponsibilities());
            if(job.getDescription()!=null){
                descriptionArea.setText(job.getDescription());
            }
            if(job.getRequirements()!=null){requirementsArea.setText(job.getRequirements());}
            if (job.getCareerLevel() != null) careerLevelCombo.setValue(job.getCareerLevel());
            if (job.getJobType() != null) jobTypeCombo.setValue(job.getJobType());
            if (job.getWorkplace() != null) workplaceCombo.setValue(job.getWorkplace());
            if (job.getJobCategory() != null) jobCategoryCombo.setValue(job.getJobCategory());
            if(job.getCountry() != null) countryField.setText(job.getCountry());
            if(job.getCity() != null) cityField.setText(job.getCity());
            if(job.getSalary() != null) salaryField.setText(job.getSalary());

        }
    }
    @FXML
    private void onSubmit() {
        // check that main fields are not missing
        if (titleField.getText().isEmpty()  || responsibilitiesArea.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Fields");
            alert.setContentText("Please fill in the title and responsibilities!");
            alert.showAndWait();
            return;
        }

        if(toBeEditedJob==null) {
            // insert new job not editing
            Job job = new CompanyJobBuilder()
                    .setMainInfo(titleField.getText(), descriptionArea.getText(), requirementsArea.getText())
                    .setJobDetails(careerLevelCombo.getValue(), jobTypeCombo.getValue(), workplaceCombo.getValue())
                    .setLocation(countryField.getText(), cityField.getText())
                    .setCategory(jobCategoryCombo.getValue())
                    .setResponsibilities(responsibilitiesArea.getText())
                    .setSalary(salaryField.getText())
                    .build();

            // set the company ID dynamically
            //job.setCompanyId(loggedCompanyId);
            job.setCompanyId(SessionManager.getInstance().getCurrentUserId());
            boolean check = jobDAO.insertJob(job);


            if ( check ) {
                System.out.println("Job inserted with ID: " + job.getId());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Successfully inserted");
                alert.setContentText("New Job Added!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to insert job!");
                alert.showAndWait();
            }
        }
        else{
            //update existing job
            toBeEditedJob.setTitle(titleField.getText());
            toBeEditedJob.setDescription(descriptionArea.getText());
            toBeEditedJob.setRequirements(requirementsArea.getText());
            toBeEditedJob.setResponsibilities(responsibilitiesArea.getText());
            toBeEditedJob.setCareerLevel(careerLevelCombo.getValue());
            toBeEditedJob.setJobType(jobTypeCombo.getValue());
            toBeEditedJob.setWorkplace(workplaceCombo.getValue());
            toBeEditedJob.setCountry(countryField.getText());
            toBeEditedJob.setCity(cityField.getText());
            toBeEditedJob.setJobCategory(jobCategoryCombo.getValue());
            toBeEditedJob.setSalary(salaryField.getText());

            boolean isUpdated=jobDAO.updateJob(toBeEditedJob);

            if(isUpdated){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully inserted");
                alert.setHeaderText("Job Updated!");
                alert.showAndWait();
                closeWindow();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to insert job!");
                alert.showAndWait();
            }
        }
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
    @FXML
    private void onCancel() {
        clearForm();
    }

    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        requirementsArea.clear();
        careerLevelCombo.getSelectionModel().clearSelection();
        jobTypeCombo.getSelectionModel().clearSelection();
        workplaceCombo.getSelectionModel().clearSelection();
        countryField.clear();
        cityField.clear();
        jobCategoryCombo.getSelectionModel().clearSelection();
        responsibilitiesArea.clear();
        salaryField.clear();
        toBeEditedJob=null;
    }
    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}
