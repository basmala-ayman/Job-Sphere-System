//package com.jobsphere.controller;
//
//import com.jobsphere.model.CompanyJobBuilder;
//import com.jobsphere.model.Job;
//import javafx.fxml.FXML;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.control.Alert;
//
//public class JobFormController {
//
//    @FXML
//    private TextField titleField;
//
//    @FXML
//    private TextArea descriptionArea;
//
//    @FXML
//    private TextArea requirementsArea;
//
//    @FXML
//    private ComboBox<String> careerLevelCombo;
//
//    @FXML
//    private ComboBox<String> jobTypeCombo;
//
//    @FXML
//    private ComboBox<String> workplaceCombo;
//
//    @FXML
//    private TextField countryField;
//
//    @FXML
//    private TextField cityField;
//
//    @FXML
//    private ComboBox<String> jobCategoryCombo;
//
//    // This method is called automatically after the FXML loads
//    @FXML
//    public void initialize() {
//        // Populate ComboBoxes
//        careerLevelCombo.getItems().addAll("Entry Level", "Mid Level", "Senior Level", "Manager");
//        jobTypeCombo.getItems().addAll("Full-Time", "Part-Time", "Internship");
//        workplaceCombo.getItems().addAll("Onsite", "Remote", "Hybrid");
//        jobCategoryCombo.getItems().addAll(
//                "IT/Software Development", "Engineering", "Design/Art",
//                "Marketing", "Finance", "HR", "Sales"
//        );
//    }
//
//    @FXML
//    private void handleSubmit() {
//        // Basic validation
//        if (titleField.getText().isEmpty() || descriptionArea.getText().isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Validation Error");
//            alert.setHeaderText("Missing Fields");
//            alert.setContentText("Please fill in the title and description!");
//            alert.showAndWait();
//            return;
//        }
//
//        // Build Job object
//        Job job = new CompanyJobBuilder()
//                .setBasicInfo(titleField.getText(), descriptionArea.getText(), requirementsArea.getText())
//                .setJobDetails(careerLevelCombo.getValue(), jobTypeCombo.getValue(), workplaceCombo.getValue())
//                .setLocation(countryField.getText(), cityField.getText())
//                .setCategory(jobCategoryCombo.getValue())
//                .build();
//
//        // For testing: just print the job
//        System.out.println("Job created: " + job);
//
//        // Clear the form (optional)
//        clearForm();
//    }
//
//    @FXML
//    private void handleCancel() {
//        clearForm();
//    }
//
//    private void clearForm() {
//        titleField.clear();
//        descriptionArea.clear();
//        requirementsArea.clear();
//        careerLevelCombo.getSelectionModel().clearSelection();
//        jobTypeCombo.getSelectionModel().clearSelection();
//        workplaceCombo.getSelectionModel().clearSelection();
//        countryField.clear();
//        cityField.clear();
//        jobCategoryCombo.getSelectionModel().clearSelection();
//    }
//}
