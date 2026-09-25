package com.fashionstore.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class InspectDatabase {

    @Test
    public void executeMigrationAndVerify() {
        String url = "jdbc:mysql://127.0.0.1:3306/fashion_store?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String pass = "Faisal@2003";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();

            System.out.println("=== EXECUTING MIGRATION ON LOCAL MYSQL DATABASE ===");

            // Run ALTER TABLE if columns are missing
            ResultSet rsDescCheck = stmt.executeQuery("DESCRIBE products;");
            boolean hasBrand = false;
            boolean hasIsFeatured = false;
            while (rsDescCheck.next()) {
                String field = rsDescCheck.getString("Field");
                if ("brand".equalsIgnoreCase(field)) hasBrand = true;
                if ("is_featured".equalsIgnoreCase(field)) hasIsFeatured = true;
            }

            if (!hasBrand || !hasIsFeatured) {
                StringBuilder alterSql = new StringBuilder("ALTER TABLE products ");
                List<String> adds = new ArrayList<>();
                if (!hasBrand) {
                    adds.add("ADD COLUMN brand VARCHAR(100) DEFAULT 'Aura' AFTER name");
                }
                if (!hasIsFeatured) {
                    adds.add("ADD COLUMN is_featured TINYINT(1) DEFAULT 1 AFTER category_id");
                }
                alterSql.append(String.join(", ", adds)).append(";");

                System.out.println("Running SQL: " + alterSql);
                stmt.executeUpdate(alterSql.toString());
                System.out.println("MIGRATION_STATUS: ALTER TABLE executed successfully.");
            } else {
                System.out.println("MIGRATION_STATUS: Columns 'brand' and 'is_featured' already exist.");
            }

            System.out.println("\n=== POST-MIGRATION DESCRIBE products; ===");
            ResultSet rsDesc = stmt.executeQuery("DESCRIBE products;");
            while (rsDesc.next()) {
                String field = rsDesc.getString("Field");
                String type = rsDesc.getString("Type");
                String nullability = rsDesc.getString("Null");
                String key = rsDesc.getString("Key");
                String defaultValue = rsDesc.getString("Default");
                String extra = rsDesc.getString("Extra");

                System.out.printf("  %-20s %-20s NULL=%-5s KEY=%-5s DEFAULT=%-10s EXTRA=%s\n",
                        field, type, nullability, key, String.valueOf(defaultValue), extra);
            }

            System.out.println("\n=== VERIFYING PRODUCT ROW COUNT ===");
            ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM products;");
            if (rsCount.next()) {
                int count = rsCount.getInt(1);
                System.out.println("ROW_COUNT_VERIFICATION: SELECT COUNT(*) FROM products = " + count);
            }

            conn.close();
        } catch (Exception e) {
            System.err.println("MIGRATION_FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
