package com.jobsphere.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicantFeaturesController {

    @FXML
    private void goToSearch() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/JobSearch.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Search For a Job");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Search For Jobs page!");
        }
    }

    @FXML
    private void goToJobDetails() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/JobDetails.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Job Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Job Details page!");
        }
    }
}
