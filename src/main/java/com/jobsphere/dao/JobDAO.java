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
        String sql = "SELECT DISTINCT country FROM jobs WHERE status='active'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                countries.add(rs.getString("country"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }

    //and this is the same the previous one but for the job types 
    public List<String> getDistinctJobTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT job_type FROM jobs WHERE status='active'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                types.add(rs.getString("job_type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }


    //and this function will return the active current jobs based on just one filter or both or even if the user not choose any filter it will return all the jobs without any restriction 
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
    private Job mapRowToJob(ResultSet rs) throws SQLException {
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
        job.setPostedAt(rs.getTimestamp("posted_at").toLocalDateTime());
        return job;
    }

    // this is for inserting new record for a job in the jobs table
public boolean insertJob(Job job) {
  String sql = "INSERT INTO jobs (company_id, title, description, responsibilities, requirements, career_level, job_type, workplace, country, city, job_category, salary, status, posted_at) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  try (Connection conn = DBConnection.getConnection();
       PreparedStatement stmt = conn.prepareStatement(sql)) {

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
      stmt.setTimestamp(14, Timestamp.valueOf(job.getPostedAt()));

      stmt.executeUpdate();
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
  String sql = "DELETE FROM jobs WHERE id=?";
  try (Connection conn = DBConnection.getConnection();
       PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, jobId);
      stmt.executeUpdate();
      return true;
  } catch (SQLException e) {
      e.printStackTrace();
      return false;
  }
}


  }
