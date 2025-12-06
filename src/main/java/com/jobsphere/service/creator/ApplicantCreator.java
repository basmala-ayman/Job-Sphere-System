package com.jobsphere.service.creator;

import com.jobsphere.controller.RegisterController;
import com.jobsphere.model.Applicant;

public class ApplicantCreator implements UserCreator {

    @Override
    public Applicant createUser(RegisterController controller) {
        Applicant applicant = new Applicant();

        applicant.setPhone(controller.getPhone());
        applicant.setExperienceYears(controller.getExperience());
        applicant.setSkills(controller.getSkills());

        return applicant;
    }
}
