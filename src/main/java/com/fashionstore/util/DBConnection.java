package com.fashionstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    /**
     * MySQL Workbench connection "faisal": root @ 127.0.0.1:3306
     * Default schema for this project: faisaldb
     *
     * Credentials are read from environment variables so they never
     * live inside the source code. Set these before running the app:
     *   DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
     * If an environment variable isn't set, a safe local default is used
     * (except for the password, which has no default).
     */
    private static final String HOST = getEnvOrDefault("DB_HOST", "127.0.0.1");
    private static final String PORT = getEnvOrDefault("DB_PORT", "3306");
    private static final String DATABASE = getEnvOrDefault("DB_NAME", "fashion_store");
    private static final String USERNAME = getEnvOrDefault("DB_USER", "root");
    private static final String PASSWORD = getEnvOrDefault("DB_PASSWORD", "");

    // Reads a value from an environment variable, or falls back to a default
    // if that environment variable hasn't been set on this machine.
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private DBConnection() {
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("[Aura] Connected to " + DATABASE + " @ " + HOST + ":" + PORT);
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("[Aura] MySQL driver missing. Add mysql-connector-j.jar to WEB-INF/lib");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[Aura] Database connection failed: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[Aura] Unexpected DB error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void closeQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }
}