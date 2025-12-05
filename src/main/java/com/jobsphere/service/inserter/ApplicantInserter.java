package com.jobsphere.service.inserter;

import com.jobsphere.dao.ApplicantDAO;
import com.jobsphere.model.Applicant;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplicantInserter implements ProfileInserter<Applicant> {

    private final ApplicantDAO applicantDao = new ApplicantDAO();

    @Override
    public void insertProfile(Connection conn, Applicant applicant) throws SQLException {
        applicantDao.insertApplicantProfile(conn, applicant);
    }

}
