package com.example.azmoonet.database.mappers;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // remote db
        ds.setUrl("jdbc:mysql:// 194.62.43.26:3306/azmoonet?allowPublicKeyRetrieval=true");
        ds.setUsername("hamidkhd");
        ds.setPassword("@Hamid78");
        ds.setMinIdle(1);
        ds.setMaxIdle(5);
        ds.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private ConnectionPool() {
    }

}
