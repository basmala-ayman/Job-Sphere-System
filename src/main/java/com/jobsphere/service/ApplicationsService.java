package com.jobsphere.service;


import com.jobsphere.dao.*;

import java.sql.SQLException;
import java.util.ArrayList;
import com.jobsphere.model.*;

import java.util.List;
import java.util.ArrayList;

//in this part we use facade pattern 
public class ApplicationsService {

  private ApplicationsDAO applicationsDAO = ApplicationsDAO.getInstance();
  private ApplicantDAO applicantDAO = ApplicantDAO.getInstance();

  public List<ApplicationWithApplicant> getApplicationsForCompany(int companyId) throws SQLException {
      List<Application> applications = applicationsDAO.getApplicationsForCompany(companyId);
      List<ApplicationWithApplicant> list = new ArrayList<>();
      for(Application app : applications) {
          Applicant applicant = applicantDAO.getProfile(app.getApplicantId());
          list.add(new ApplicationWithApplicant(app, applicant));
      }
      return list;
  }
//we will use this temporary class to enhance the performance cause we know that the application having the applicant id and 
//by using that we can get the applicant and it will be worked correctly but the performance will not be the best option cause that means more queries to db
//rather than here we just make few database queries and then store all infor needed in the same thing
  public static class ApplicationWithApplicant {
      public Application application;
      public Applicant applicant;

      public ApplicationWithApplicant(Application application, Applicant applicant) {
          this.application = application;
          this.applicant = applicant;
      }
  }
}
