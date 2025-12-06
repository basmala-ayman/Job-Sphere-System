package com.jobsphere.controller;

import com.jobsphere.service.ApplicationsService;
import com.jobsphere.service.ApplicationsService.ApplicationWithApplicant;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.List;

public class ApplicationManagementController {

    @FXML
    private TableView<ApplicationWithApplicant> applicationsTable;

    @FXML private TableColumn<ApplicationWithApplicant, String> nameCol;
    @FXML private TableColumn<ApplicationWithApplicant, String> skillsCol;
    @FXML private TableColumn<ApplicationWithApplicant, String> resumeCol;
    @FXML private TableColumn<ApplicationWithApplicant, Integer> experienceCol;
    @FXML private TableColumn<ApplicationWithApplicant, String> statusCol;

    private ApplicationsService service = new ApplicationsService();

    @FXML
    public void initialize() throws SQLException {
        
        nameCol.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().applicant.getId() + "")
        );
        skillsCol.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().applicant.getSkills())
        );
        resumeCol.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().applicant.getResumeUrl())
        );
        experienceCol.setCellValueFactory(data ->
            new SimpleIntegerProperty(data.getValue().applicant.getExperienceYears()).asObject()
        );
        statusCol.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().application.getStatus())
        );

        // Load applications for this company (replace 1 with actual companyId)
        loadApplications(3);
    }

    private void loadApplications(int companyId) throws SQLException {
        List<ApplicationWithApplicant> apps = service.getApplicationsForCompany(companyId);
        applicationsTable.getItems().setAll(apps);
    }
}
