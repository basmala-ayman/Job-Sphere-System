package com.jobsphere.controller;

import com.jobsphere.model.CompanyJobBuilder;
import com.jobsphere.model.Job;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

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

    @FXML
    private void onSubmit() {
        // check that main field are not missing
        if (titleField.getText().isEmpty() || descriptionArea.getText().isEmpty() || requirementsArea.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Fields");
            alert.setContentText("Please fill in the title , description and requirements!");
            alert.showAndWait();
            return;
        }

        // Build Job object
        Job job = new CompanyJobBuilder()
                .setMainInfo(titleField.getText(), descriptionArea.getText(), requirementsArea.getText())
                .setJobDetails(careerLevelCombo.getValue(), jobTypeCombo.getValue(), workplaceCombo.getValue())
                .setLocation(countryField.getText(), cityField.getText())
                .setCategory(jobCategoryCombo.getValue())
                .build();

        clearForm();
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
    }
}
