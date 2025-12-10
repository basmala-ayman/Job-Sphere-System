package com.jobsphere.service.inserter;

import com.jobsphere.dao.CompanyDAO;
import com.jobsphere.model.Company;

import java.sql.Connection;
import java.sql.SQLException;

public class CompanyInserter implements ProfileInserter<Company> {

    private final CompanyDAO companyDao = new CompanyDAO();

    @Override
    public void insertProfile(Connection conn, Company company) throws SQLException {
        companyDao.insertCompanyProfile(conn, company);
    }
}
