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

    @FXML private TextField searchField;
    @FXML private ComboBox<String> locationBox;
    @FXML private ComboBox<String> typeBox;
    @FXML private TableView<Job> jobTable;

    @FXML private TableColumn<Job, String> titleColumn;
    @FXML private TableColumn<Job, String> locationColumn;
    @FXML private TableColumn<Job, String> typeColumn;
    @FXML private TableColumn<Job, String> companyColumn;

    private JobDAO jobDAO;
    private List<Job> allJobs;

    @FXML
    public void initialize() throws SQLException {

        Connection conn = com.jobsphere.dao.DBConnection.getConnection();
        jobDAO = JobDAO.getInstance(conn);

        allJobs = jobDAO.getAllJobs();

        locationBox.setItems(FXCollections.observableArrayList(jobDAO.getDistinctCountries()));
        typeBox.setItems(FXCollections.observableArrayList(jobDAO.getDistinctJobTypes()));

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

        if ((keyword == null || keyword.isEmpty()) &&
                (country == null || country.equals("Any")) &&
                (jobType == null || jobType.equals("Any"))) {

            jobTable.setItems(FXCollections.observableArrayList(allJobs));
            return;
        }

        if (keyword != null && !keyword.isEmpty() &&
                (country == null || country.equals("Any")) &&
                (jobType == null || jobType.equals("Any"))) {

            context.setStrategy(new KeywordSearchStrategy(keyword));
        }

        else if ((keyword == null || keyword.isEmpty())) {

            context.setStrategy(new FilterSearchStrategy(country, jobType));
        }

        else {

            context.setStrategy(new CombinedSearchStrategy(keyword, country, jobType));
        }

        List<Job> results = context.execute(jobDAO);
        jobTable.setItems(FXCollections.observableArrayList(results));
    }
}
