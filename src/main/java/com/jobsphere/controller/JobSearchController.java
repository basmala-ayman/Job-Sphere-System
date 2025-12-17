package com.jobsphere.controller;

import com.jobsphere.dao.JobDAO;
import com.jobsphere.model.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JobSearchController {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> locationBox;
    @FXML
    private ComboBox<String> typeBox;
    @FXML
    private TableView<Job> jobTable;

    @FXML
    private TableColumn<Job, String> titleColumn;
    @FXML
    private TableColumn<Job, String> locationColumn;
    @FXML
    private TableColumn<Job, String> typeColumn;
    @FXML
    private TableColumn<Job, String> companyColumn;

    private JobDAO jobDAO;
    private List<Job> allJobs;

    @FXML
    public void initialize() throws SQLException {

        Connection conn = com.jobsphere.dao.DBConnection.getConnection();
        jobDAO = JobDAO.getInstance(conn);

        allJobs = jobDAO.getAllJobs();

        // Countries dropdown with ignore case and remove duplicates
        List<String> countries = jobDAO.getDistinctCountries().stream()
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .toList();
        countries.add(0, "All");
        locationBox.setItems(FXCollections.observableArrayList(countries));
        locationBox.getSelectionModel().selectFirst();

        // Job Types dropdown with ignore case and remove duplicates
        List<String> jobTypes = jobDAO.getDistinctJobTypes().stream()
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .toList();
        jobTypes.add(0, "All");
        typeBox.setItems(FXCollections.observableArrayList(jobTypes));
        typeBox.getSelectionModel().selectFirst();

        // Table columns
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        locationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCountry()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJobType()));

        jobTable.setItems(FXCollections.observableArrayList(allJobs));
    }

    @FXML
    public void searchJobs() {
        String keyword = searchField.getText();
        String country = locationBox.getValue();
        String jobType = typeBox.getValue();

        JobSearchContext context = new JobSearchContext();

        boolean isKeywordEmpty = keyword == null || keyword.isEmpty();
        boolean isCountryAll = country == null || country.equalsIgnoreCase("All");
        boolean isJobTypeAll = jobType == null || jobType.equalsIgnoreCase("All");

        if (isKeywordEmpty && isCountryAll && isJobTypeAll) {
            jobTable.setItems(FXCollections.observableArrayList(allJobs));
            return;
        }

        if (!isKeywordEmpty && isCountryAll && isJobTypeAll) {
            context.setStrategy(new KeywordSearchStrategy(keyword.toLowerCase()));
        } else if (isKeywordEmpty) {
            context.setStrategy(new FilterSearchStrategy(
                    isCountryAll ? null : country.toLowerCase(),
                    isJobTypeAll ? null : jobType.toLowerCase()
            ));
        } else {
            context.setStrategy(new CombinedSearchStrategy(
                    keyword.toLowerCase(),
                    isCountryAll ? null : country.toLowerCase(),
                    isJobTypeAll ? null : jobType.toLowerCase()
            ));
        }

        List<Job> results = context.execute(jobDAO);
        jobTable.setItems(FXCollections.observableArrayList(results));
    }

}
