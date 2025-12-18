package com.jobsphere.dao;

import com.jobsphere.model.Company;

import java.sql.*;

public class CompanyDAO {

    // Insert a new company profile record into the company table
    public void insertCompanyProfile(Connection conn, Company company) throws SQLException {
        String sql = "INSERT INTO company_profiles (user_id, website, description, industry) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, company.getId());
            stmt.setString(2, company.getWebsite());
            stmt.setString(3, company.getDescription());
            stmt.setString(4, company.getIndustry());

            stmt.executeUpdate();
        }
    }

}
