package com.jobsphere.service.creator;

import com.jobsphere.controller.RegisterController;
import com.jobsphere.model.User;

public interface UserCreator {
    User createUser(RegisterController controller);
}
