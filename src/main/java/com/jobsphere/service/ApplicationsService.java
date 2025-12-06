package com.jobsphere.service;

import com.jobsphere.dao.ApplicationsDAO;
import com.jobsphere.model.ApplicationFullInfo; // <-- your new model
import java.util.List;

public class ApplicationsService {

    private ApplicationsDAO applicationsDAO = ApplicationsDAO.getInstance();

    // Simply fetch all applications with full info for a company
    public List<ApplicationFullInfo> getApplicationsForCompany(int companyId) {
        return applicationsDAO.getApplicationsFullInfoForCompany(companyId);
    }

    // Update status of an application
    public boolean updateApplicationStatus(int applicationId, String newStatus) {
        return applicationsDAO.updateStatus(applicationId, newStatus);
    }
}
