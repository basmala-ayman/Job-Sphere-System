package com.jobsphere.controller;

import com.jobsphere.dao.ApplicationsDAO;
import com.jobsphere.model.Application;
import com.jobsphere.model.SearchCompany;
import com.jobsphere.model.Job;
import com.jobsphere.service.auth.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

//creating controller for search company
//creates a table view for displaying company information

public class SearchCompanyController {


    @FXML
    private ComboBox<Job> jobCombo;
    @FXML
    private TableView<SearchCompany> companyTable;


    @FXML
    private TableColumn<SearchCompany, String> jobTitle;
    @FXML
    private TableColumn<SearchCompany, String> statusCol;
    @FXML
    private TableColumn<SearchCompany, String> ApplicantnameCol;
    @FXML
    private TableColumn<SearchCompany, String> skillsCol;
    @FXML
    private TableColumn<SearchCompany, String> expYearsCol;
    @FXML
    private TableColumn<SearchCompany, String> emailCol;
    @FXML
    private TableColumn<SearchCompany, String> resumeURLCol;

    //data objects
    private ApplicationsDAO applicationsDAO = ApplicationsDAO.getInstance();

   //Observer pattern to update the list automatically
   private ObservableList<SearchCompany> companyList = FXCollections.observableArrayList();

   @FXML
    public void initialize() {
       jobTitle.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
       ApplicantnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
       skillsCol.setCellValueFactory(new PropertyValueFactory<>("skills"));
       expYearsCol.setCellValueFactory(new PropertyValueFactory<>("expYears"));
       statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
       emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
       resumeURLCol.setCellValueFactory(new PropertyValueFactory<>("resumeUrl"));

        companyTable.setItems(companyList);

    }

}
