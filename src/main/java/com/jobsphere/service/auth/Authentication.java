package com.jobsphere.service.auth;

import com.jobsphere.dao.UserDAO;
import com.jobsphere.model.User;
import com.jobsphere.dao.DBConnection;
import com.jobsphere.service.inserter.ProfileFactory;
import com.jobsphere.service.inserter.ProfileInserter;

import java.sql.Connection;
import java.sql.SQLException;

public class Authentication {
    private final UserDAO userDao = UserDAO.getInstance();

    @SuppressWarnings("unchecked")
    public <T> RegistrationResult registerUserAuth(T user, String role) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            // make AutoCommit -> false to create (user record + user account record) and then save them to avoid partial user creation
            conn.setAutoCommit(false);

            // register applicant or company user
            User userIsCreated = userDao.registerUser(conn, (User) user);
            if (userIsCreated == null) {
                conn.rollback();
                return RegistrationResult.fail("Registration failed: email may already exist");
            }
            // create profile according to user's role using Factory Pattern
            ProfileInserter<T> inserter = (ProfileInserter<T>) ProfileFactory.getInserter(role);
            inserter.insertProfile(conn, user);

            // commit -> save to database
            conn.commit();
            return RegistrationResult.success(((User) user).getId(), "Registration successful");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return RegistrationResult.fail("Registration failed" + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception i) {
            }
        }
    }

}
