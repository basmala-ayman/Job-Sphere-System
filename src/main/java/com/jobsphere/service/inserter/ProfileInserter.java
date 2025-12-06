package com.jobsphere.service.inserter;

import java.sql.Connection;
import java.sql.SQLException;

public interface ProfileInserter<T> {
    void insertProfile(Connection conn, T user) throws SQLException;
}
