package com.jobsphere.service.creator;

public class UserCreatorFactory {

    public static UserCreator getUserCreator(String role) {
        if (role == null)
            throw new IllegalArgumentException("role cannot be null");
        if (role.equalsIgnoreCase("Applicant"))
            return new ApplicantCreator();
        else if (role.equalsIgnoreCase("company"))
            return new CompanyCreator();
        else
            throw new IllegalArgumentException(role + " is not a valid role");
    }
}
