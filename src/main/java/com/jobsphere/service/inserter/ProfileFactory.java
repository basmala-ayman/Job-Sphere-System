package com.jobsphere.service.inserter;

public class ProfileFactory {

    public static ProfileInserter<?> getInserter(String role) {
        if (role == null)
            throw new IllegalArgumentException("Role cannot be null");
        if (role.equalsIgnoreCase("applicant"))
            return new ApplicantInserter();
        else if (role.equalsIgnoreCase("company"))
            return new CompanyInserter();
        else
            throw new IllegalArgumentException(role + " is not a valid role");
    }
}
