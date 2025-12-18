package com.jobsphere.dao;

import com.jobsphere.model.Applicant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicantDAO {

    //the applicant its skills is array of strings
    //we will use singleton here too to jsut dealing with just one object from it
    private static ApplicantDAO instance;

    private ApplicantDAO() {
    }

    public static ApplicantDAO getInstance() {
        if (instance == null) {
            instance = new ApplicantDAO();
        }
        return instance;
    }

    // Insert a new applicant profile
    public void insertApplicantProfile(Connection conn, Applicant applicant) throws SQLException {
        String sql = "INSERT INTO applicants (user_id, phone, resume_url, skills, experience_years) " +
                "VALUES (?, ?, ?, ?::json, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicant.getId());
            stmt.setString(2, applicant.getPhone());
            stmt.setString(3, applicant.getResumeUrl());
            stmt.setString(4, applicant.getSkills()); // skills as JSON string
            stmt.setInt(5, applicant.getExperienceYears());

            stmt.executeUpdate();

        }
    }

    public List<Integer> getDistinctExperienceYears() {
        List<Integer> years = new ArrayList<>();
        String sql = "SELECT DISTINCT experience_years FROM applicants ORDER BY experience_years";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                years.add(rs.getInt("experience_years"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return years;
    }
}
