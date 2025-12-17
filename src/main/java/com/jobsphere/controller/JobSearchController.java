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
import java.util.*;

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
        if (allJobs == null) {
            allJobs = new ArrayList<>();
        }

        // Use Set for countries to remove duplicates automatically
        Set<String> countriesSet = new HashSet<>();
        List<String> countriesFromDB = jobDAO.getDistinctCountries();
        if (countriesFromDB != null) {
            for (String c : countriesFromDB) {
                if (c != null && !c.isBlank()) {
                    countriesSet.add(c.toLowerCase()); // ignore case
                }
            }
        }
        List<String> countries = new ArrayList<>(countriesSet);
        Collections.sort(countries);
        countries.add(0, "All");  // add "All" at the top
        locationBox.setItems(FXCollections.observableArrayList(countries));
        locationBox.getSelectionModel().selectFirst();

        // Same for job types
        Set<String> jobTypesSet = new HashSet<>();
        List<String> typesFromDB = jobDAO.getDistinctJobTypes();
        if (typesFromDB != null) {
            for (String t : typesFromDB) {
                if (t != null && !t.isBlank()) {
                    jobTypesSet.add(t.toLowerCase());
                }
            }
        }
        List<String> jobTypes = new ArrayList<>(jobTypesSet);
        Collections.sort(jobTypes);
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
            context.setStrategy(new KeywordSearchStrategy(keyword));
        } else if (isKeywordEmpty) {
            context.setStrategy(new FilterSearchStrategy(
                    isCountryAll ? null : country,
                    isJobTypeAll ? null : jobType
            ));
        } else {
            context.setStrategy(new CombinedSearchStrategy(
                    keyword,
                    isCountryAll ? null : country,
                    isJobTypeAll ? null : jobType
            ));
        }

        List<Job> results = context.execute(jobDAO);
        if (results == null) results = new ArrayList<>();
        jobTable.setItems(FXCollections.observableArrayList(results));
    }
}
