package com.jobsphere.dao;

import com.jobsphere.model.Application;
import com.jobsphere.model.ApplicationFullInfo;
import com.jobsphere.model.SearchCompany;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationsDAO {

    //we will use singleton here too to jsut dealing with just one object from it
    private static ApplicationsDAO instance;

    private ApplicationsDAO() {
    }

    public static ApplicationsDAO getInstance() {
        if (instance == null) {
            instance = new ApplicationsDAO();
        }
        return instance;
    }

    // Apply for a job and it needs this 3 things to be passed to go to make a record for it in the database
    public boolean applyForJob(int applicantId, int jobId, String resumeUrl) {
        String sql = "INSERT INTO applications (job_id, applicant_id, resume_url) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jobId);
            stmt.setInt(2, applicantId);
            stmt.setString(3, resumeUrl);

            stmt.executeUpdate();
            return true; // Successfully applied

        } catch (SQLException e) {
            //and it will return false even if the application is already existed before cause if the record was existed before db will throw an error
            return false;

        }
    }


    //when i create the table in the database i enforce it to ensure that applicantid with the jobid unique and not duplicated
    //so that will make us not need another function for checking if it is already applied before or no

    // get all applications for a specific job and here i will return list of Applications objects and there just use what i want
    public List<Application> getApplicationsByJob(int jobId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE job_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setJobId(rs.getInt("job_id"));
                app.setApplicantId(rs.getInt("applicant_id"));
                app.setResumeUrl(rs.getString("resume_url"));
                app.setStatus(rs.getString("status"));
                app.setAppliedAt(rs.getTimestamp("applied_at").toLocalDateTime());

                applications.add(app);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applications;
    }

    // get the status of an application to can track the application status
    //and this will help in the last module that it will return all the applications objects and then in the code we can for each
    //application object we can use the get profile function from the applicants to get the applicant profile and that will be the view and then we can making the search in the same code
    public String getApplicationStatus(int applicationId) {
        String status = null;
        String sql = "SELECT status FROM applications WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                status = rs.getString("status");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }


    // Get all applications for a specific company
    public List<Application> getApplicationsForCompany(int companyId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT a.* FROM applications a " +
                "JOIN jobs j ON a.job_id = j.id " +
                "WHERE j.company_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setJobId(rs.getInt("job_id"));
                app.setApplicantId(rs.getInt("applicant_id"));
                app.setResumeUrl(rs.getString("resume_url"));
                app.setStatus(rs.getString("status"));
                app.setAppliedAt(rs.getTimestamp("applied_at").toLocalDateTime());
                applications.add(app);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applications;
    }

    //this is for updating the status of the application based on what recuiter say
    public boolean updateStatus(int applicationId, String newStatus) {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, applicationId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<SearchCompany> searchCandidatesByExperienceAndSkill(int jobId, int experienceYears, String skill) {
        List<SearchCompany> results = new ArrayList<>();

        // Use @> operator to check if the JSON array contains the skill
        String sql = "SELECT \n" +
                "    u.name,\n" +
                "    u.email,\n" +
                "    j.title,\n" +
                "    ap.skills,\n" +
                "    a.status,\n" +
                "    a.resume_url,\n" +
                "    ap.experience_years\n" +
                "FROM applications a\n" +
                "JOIN jobs j ON a.job_id = j.id\n" +
                "JOIN users u ON a.applicant_id = u.id\n" +
                "JOIN applicants ap ON u.id = ap.user_id\n" +
                "WHERE j.id = ?\n" +
                "AND ( ? = 0 OR ap.experience_years >= ? )\n" +
                "AND ( ? = '' OR ap.skills::jsonb @> ?::jsonb );\n";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jobId);
            stmt.setInt(2, experienceYears);
            stmt.setInt(3, experienceYears);
            // Convert skill string to JSON array format: '["Java"]'
            stmt.setString(4, skill);
            stmt.setString(5, "[\"" + skill + "\"]");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(new SearchCompany(
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getString("skills"),
                        rs.getInt("experience_years"),
                        rs.getString("status"),
                        rs.getString("email"),
                        rs.getString("resume_url")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }


    // big query to fetch all the application info in one shot for application management rather than converting it into the n+1
    //problem and this will hit the database just once and will hit the disk in more less times which will lead to more better performance
    public List<ApplicationFullInfo> getApplicationsFullInfoForCompany(int companyId) {
        List<ApplicationFullInfo> list = new ArrayList<>();

        String sql = "SELECT a.id AS application_id, a.status, a.resume_url, a.applied_at, " +
                "u.id AS user_id, u.name AS user_name, u.email AS user_email, " +
                "ap.phone AS phone, ap.skills AS skills, ap.experience_years AS experience, " +
                "j.id AS job_id, j.title AS job_title " +
                "FROM applications a " +
                "JOIN applicants ap ON a.applicant_id = ap.user_id " +
                "JOIN users u ON a.applicant_id = u.id " +
                "JOIN jobs j ON a.job_id = j.id " +
                "WHERE j.company_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ApplicationFullInfo info = new ApplicationFullInfo(
                        rs.getInt("application_id"),
                        rs.getString("status"),
                        rs.getString("resume_url"),
                        rs.getTimestamp("applied_at").toString(),
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("user_email"),
                        rs.getString("phone"),
                        rs.getString("skills"),
                        rs.getInt("experience"),
                        rs.getInt("job_id"),
                        rs.getString("job_title")
                );
                list.add(info);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
