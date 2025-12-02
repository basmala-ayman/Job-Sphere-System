package com.jobsphere.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class DashboardController {
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
}
