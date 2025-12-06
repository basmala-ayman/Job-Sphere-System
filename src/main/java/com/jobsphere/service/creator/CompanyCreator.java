package com.jobsphere.service.creator;

import com.jobsphere.controller.RegisterController;
import com.jobsphere.model.Company;

public class CompanyCreator implements UserCreator {

    @Override
    public Company createUser(RegisterController controller) {

        Company company = new Company();

        company.setWebsite(controller.getCompanyWebsite());
        company.setDescription(controller.getCompanyDescription());
        company.setIndustry(controller.getCompanyIndustry());

        return company;
    }
}
