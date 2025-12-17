package com.jobsphere.controller;

import com.jobsphere.service.auth.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;


public class CompanyFeaturesController {

    @FXML
    Button addJobButton;
    @FXML
    Button manageJobsButton;
    @FXML
    Button candidateSearch;
    @FXML
    Button appManagement;

    @FXML
    private void onClickAddJob() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/JobForm.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Add New Job");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading Add Job page!");
        }
    }

    @FXML
    private void onClickManageJobs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ManageJobs.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Manage Jobs");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Manage Jobs page!");
        }
    }

    @FXML
    private void toCandidateSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/SearchCompany.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Candidate Search");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Candidate Search page!");
        }
    }

    @FXML
    private void toAppManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ApplicationsView.fxml"));

            int loggedInCompanyId = SessionManager.getInstance().getCurrentUserId();
            ApplicationController controller = loader.getController();
            controller.setCompanyId(loggedInCompanyId); // dynamic value

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            stage.setTitle("Applications Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Applications Management page!");
        }
    }


}
