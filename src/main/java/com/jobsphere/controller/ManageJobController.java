package com.jobsphere.controller;
import com.jobsphere.dao.JobDAO;
import com.jobsphere.model.Job;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageJobController {

    @FXML private TableView<Job> jobTable;
    @FXML private TableColumn<Job, String> colTitle;
    @FXML private TableColumn<Job, String> colCity;
    @FXML private TableColumn<Job, String> colType;
    @FXML private TableColumn<Job, String> colStatus;

    @FXML private Button editButton;
    @FXML private Button pauseButton;
    @FXML private Button deleteButton;

    private ObservableList<Job> jobList;
    private JobDAO jobDAO = JobDAO.getInstance();

    private void setupColumns() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colType.setCellValueFactory(new PropertyValueFactory<>("jobType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    private void loadJobs() {
        jobList = FXCollections.observableArrayList(jobDAO.getAllJobs());
        jobTable.setItems(jobList);
    }
    @FXML
    public void initialize() {
        setupColumns();
        loadJobs();
    }
    @FXML
    private void onEditJob() {
        Job selectedJob = jobTable.getSelectionModel().getSelectedItem();
        if ( selectedJob == null ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Please Select a Job");
            alert.showAndWait();
        }
        try{
            //open new window to edit job details
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/JobForm.fxml"));
            Parent root = loader.load();
            JobFormController jobFormController = loader.getController();
            //call setEditMode function in the controller and give it the selected job
            jobFormController.setEditMode(selectedJob);
            Stage stage = new Stage();
            stage.setTitle("Edit Job");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.print("failed to load FXML and edit job");
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void onPauseJob() {
        Job selected = jobTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Please Select a Job");
            alert.showAndWait();
            return;
        }

        String newStatus = selected.getStatus().equalsIgnoreCase("active")
                ? "paused"
                : "active";

        boolean success = jobDAO.updateJobStatus(selected.getId(), newStatus);

        if (success) {
            selected.setStatus(newStatus);
            jobTable.refresh();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Status Changed");
            alert.setHeaderText("Status changed to: " + newStatus);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to update status");
            alert.showAndWait();
        }
    }
    @FXML
    private void onDeleteJob() {
        Job selected = jobTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a job to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this job?\nTitle: " + selected.getTitle(),
                ButtonType.YES, ButtonType.NO);

        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            boolean success = jobDAO.deleteJob(selected.getId());

            if (success) {
                jobList.remove(selected); // remove from table
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Job deleted successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to delete job.");
                alert.showAndWait();
            }
        }
    }


}
