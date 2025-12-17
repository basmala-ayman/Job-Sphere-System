package com.jobsphere.dao;

import com.jobsphere.model.Job;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {
    private static JobDAO instance;
    private Connection conn;

    // private constructor
    private JobDAO(Connection conn) {
        this.conn = conn;
    }

    public static JobDAO getInstance(Connection conn) {
        if (instance == null) {
            instance = new JobDAO(conn);
        }
        return instance;
    }

    private JobDAO() {}

    public static JobDAO getInstance() {
        if (instance == null) {
            instance = new JobDAO();
        }
        return instance;
    }
    //  for getting all the current active jobs  and return list of Job objects
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE status = 'active'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                jobs.add(mapRowToJob(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return jobs;
    }

    // this function for taking the keyword that the user enter it in the search bar and return list of Jobs matching similar or exactly is the keyword 
    public List<Job> searchJobs(String keyword) {
        List<Job> jobs = new ArrayList<>();
        //and using the ILKIE here to handling the difference between the lower and upper case 
        String sql = "SELECT * FROM jobs WHERE status='active' AND title ILIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                jobs.add(mapRowToJob(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    //this function for return list of distinct  country names to display them in the countery filter spinner to give the user options to choose one of them 
      public List<String> getDistinctCountries() {
        List<String> countries = new ArrayList<>();
        String sql = "SELECT DISTINCT country FROM jobs";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String country = rs.getString("country");
                if (country != null) {
                    countries.add(country.toLowerCase());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }

    public List<String> getDistinctJobTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT job_type FROM jobs";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String type = rs.getString("job_type");
                if (type != null) {
                    types.add(type.toLowerCase());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }



// Get all distinct job statuses for active jobs
public List<String> getDistinctJobStatuses() {
  List<String> statuses = new ArrayList<>();
  String sql = "SELECT DISTINCT status FROM jobs";
  try (Connection conn = DBConnection.getConnection();
       Statement stmt = conn.createStatement();
       ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
          statuses.add(rs.getString("status"));
      }
  } catch (SQLException e) {
      e.printStackTrace();
  }
  return statuses;
}

// Get all distinct career levels for active jobs
public List<String> getDistinctCareerLevels() {
  List<String> levels = new ArrayList<>();
  String sql = "SELECT DISTINCT career_level FROM jobs";
  try (Connection conn = DBConnection.getConnection();
       Statement stmt = conn.createStatement();
       ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
          levels.add(rs.getString("career_level"));
      }
  } catch (SQLException e) {
      e.printStackTrace();
  }
  return levels;
}


   public List<Job> filterJobs(String country, String jobType) {
      List<Job> jobs = new ArrayList<>();
  
      StringBuilder sql = new StringBuilder("SELECT * FROM jobs WHERE status='active'");
      List<String> params = new ArrayList<>();

      //ANY here means i do not want to filter by this field 
  
      if (country != null && !country.isEmpty() && !country.equals("Any")) {
          sql.append(" AND country = ?");
          params.add(country);
      }
  
      if (jobType != null && !jobType.isEmpty() && !jobType.equals("Any")) {
          sql.append(" AND job_type = ?");
          params.add(jobType);
      }
  
      try (Connection conn = DBConnection.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

  //cause now the params is dynamic 
          for (int i = 0; i < params.size(); i++) {
              stmt.setString(i + 1, params.get(i));
          }
  
          ResultSet rs = stmt.executeQuery();
          while (rs.next()) {
              jobs.add(mapRowToJob(rs));
          }
  
      } catch (SQLException e) {
          e.printStackTrace();
      }
  
      return jobs;
  }
  
  

    //this function is take the job id input and return the all information about this specified job and it returns Job object 
    public Job getJobById(int jobId) {
      Job job = null;
      String sql = "SELECT * FROM jobs WHERE id = ? AND status = 'active'";
  
      try (Connection conn = DBConnection.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
  
          stmt.setInt(1, jobId);
          ResultSet rs = stmt.executeQuery();
  
          if (rs.next()) {
              job = mapRowToJob(rs);
          }
  
      } catch (SQLException e) {
          e.printStackTrace();
      }
  
      return job;
  }


    //this code cause it is duplicated surint the functions we will put it in just single function and it will map the strings information 
    //that returned from the satabase into the job object 
    public Job mapRowToJob(ResultSet rs) throws SQLException {
        Job job = new Job();
        job.setId(rs.getInt("id"));
        job.setCompanyId(rs.getInt("company_id"));
        job.setTitle(rs.getString("title"));
        job.setDescription(rs.getString("description"));
        job.setResponsibilities(rs.getString("responsibilities"));
        job.setRequirements(rs.getString("requirements"));
        job.setCareerLevel(rs.getString("career_level"));
        job.setJobType(rs.getString("job_type"));
        job.setWorkplace(rs.getString("workplace"));
        job.setCountry(rs.getString("country"));
        job.setCity(rs.getString("city"));
        job.setJobCategory(rs.getString("job_category"));
        job.setSalary(rs.getString("salary"));
        job.setStatus(rs.getString("status"));
        return job;
    }

    // this is for inserting new record for a job in the jobs table
    public boolean insertJob(Job job) {
        String sql = "INSERT INTO jobs (company_id, title, description, responsibilities, requirements, career_level, job_type, workplace, country, city, job_category, salary, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters
            stmt.setInt(1, job.getCompanyId());
            stmt.setString(2, job.getTitle());
            stmt.setString(3, job.getDescription());
            stmt.setString(4, job.getResponsibilities());
            stmt.setString(5, job.getRequirements());
            stmt.setString(6, job.getCareerLevel());
            stmt.setString(7, job.getJobType());
            stmt.setString(8, job.getWorkplace());
            stmt.setString(9, job.getCountry());
            stmt.setString(10, job.getCity());
            stmt.setString(11, job.getJobCategory());
            stmt.setString(12, job.getSalary());
            stmt.setString(13, job.getStatus());

            // Execute insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating job failed, no rows affected.");
            }

            // Retrieve the auto-generated job ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    job.setId(rs.getInt(1)); // set Job.id after insertion
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


// here update all the fields of specific job
public boolean updateJob(Job job) {
  String sql = "UPDATE jobs SET title=?, description=?, responsibilities=?, requirements=?, career_level=?, job_type=?, workplace=?, country=?, city=?, job_category=?, salary=?, status=? " +
               "WHERE id=?";
  try (Connection conn = DBConnection.getConnection();
       PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, job.getTitle());
      stmt.setString(2, job.getDescription());
      stmt.setString(3, job.getResponsibilities());
      stmt.setString(4, job.getRequirements());
      stmt.setString(5, job.getCareerLevel());
      stmt.setString(6, job.getJobType());
      stmt.setString(7, job.getWorkplace());
      stmt.setString(8, job.getCountry());
      stmt.setString(9, job.getCity());
      stmt.setString(10, job.getJobCategory());
      stmt.setString(11, job.getSalary());
      stmt.setString(12, job.getStatus());
      stmt.setInt(13, job.getId());

      stmt.executeUpdate();
      return true;
  } catch (SQLException e) {
      e.printStackTrace();
      return false;
  }
}

// here to update the job status the job hasing 2 status paused or active 
public boolean updateJobStatus(int jobId, String status) {
  String sql = "UPDATE jobs SET status=? WHERE id=?";
  try (Connection conn = DBConnection.getConnection();
       PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, status);
      stmt.setInt(2, jobId);
      stmt.executeUpdate();
      return true;
  } catch (SQLException e) {
      e.printStackTrace();
      return false;
  }
}

// delete a job
public boolean deleteJob(int jobId) {
    String deleteAppsSql = "DELETE FROM applications WHERE job_id=?";
    String deleteSavedSql = "DELETE FROM saved_jobs WHERE job_id=?";
    String deleteJobSql = "DELETE FROM jobs WHERE id=?";

    try (Connection conn = DBConnection.getConnection()) {

      try (PreparedStatement appStmt = conn.prepareStatement(deleteAppsSql)) {
            appStmt.setInt(1, jobId);
            appStmt.executeUpdate();
        }

        try (PreparedStatement savedStmt = conn.prepareStatement(deleteSavedSql)) {
            savedStmt.setInt(1, jobId);
            savedStmt.executeUpdate();
        }

        try (PreparedStatement jobStmt = conn.prepareStatement(deleteJobSql)) {
            jobStmt.setInt(1, jobId);
            jobStmt.executeUpdate();
            return true;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public List<Job> getJobsByCompanyId(int companyId) {
  List<Job> jobs = new ArrayList<>();
  String sql = "SELECT * FROM jobs WHERE company_id = ?";
  try (Connection conn = DBConnection.getConnection();
       PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, companyId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
          jobs.add(mapRowToJob(rs));
      }

  } catch (SQLException e) {
      e.printStackTrace();
  }

  return jobs;

}

  }
