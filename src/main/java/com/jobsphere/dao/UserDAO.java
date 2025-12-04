package com.jobsphere.dao;

import com.jobsphere.model.User;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDAO {

  //this function is taking User object and add this new user into our database 
    public void registerUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();//using the connection we already define by using singleton
            String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashPassword(user.getPassword())); // تشفير كلمة المرور
            stmt.setString(4, user.getRole());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
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
                if (storedHash.equals(hashPassword(password))) {
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
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return null; // that means there is wrong on the entered log in information
    }

    // this is for storing the passwords in the database hashed
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");//this is to define the type of the hashing 
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
