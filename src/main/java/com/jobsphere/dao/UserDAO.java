package com.jobsphere.dao;

import com.jobsphere.model.User;

import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;


public class UserDAO {

    //this function is taking User object and add this new user into our database 
    public User registerUser(Connection conn, User user) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            String hashedPass = hashPassword(user.getPassword());

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashedPass);
            stmt.setString(4, user.getRole());

            stmt.executeUpdate();
            // to return the created user
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    // update user object to set the generated id for this user
                    user.setId(rs.getInt(1));
                    user.setPassword(hashedPass);
                    return user;
                }
            }
            return null;
        } catch (SQLException e) {
            // 23505 = UNIQUE constraint violated (email already exists)
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Email already exists in the database.");
                return null;
            }
            //this is for the other errors may happened like network or from sql anything
            e.printStackTrace();
            return null;
        }
    }

    //and this function is for using in the login and it take the email and the password and return the user object itself if the user 
    //existed or just returned null if it isnot existed 
    public User login(String email, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE email = ?";//the ? is placeholder for sql injection
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            //if the user is existed
            if (rs.next()) {
                String storedHash = rs.getString("password");//get its stored hashed pass from the db
                if (checkPassword(password, storedHash)) {
                    User user = new User();//creating object to return it 
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("name"));
                    user.setEmail(email);
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
            }
        }
        return null; // that means there is wrong on the entered log in information
    }

    // this is for storing the passwords in the database hashed
    //and we give it the plain text password and it will gerate salt for it
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    //and here this function to check you give it the original hashed password and the plain text one and it check with the built in function
    private boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

}
