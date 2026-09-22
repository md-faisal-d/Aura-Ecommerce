package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fashionstore.dao.interfaces.UserDAO;
import com.fashionstore.model.User;
import com.fashionstore.util.DBConnection;

public class UserDAOImpl implements UserDAO {

    public UserDAOImpl() {
        // Connection management handled per-method
    }

    private Connection openConnection() {
        return DBConnection.getConnection();
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));

        try {
            user.setPhone(rs.getString("phone"));
        } catch (SQLException e) {
            user.setPhone("");
        }

        try {
            user.setPassword(rs.getString("password_hash"));
        } catch (SQLException e) {
            user.setPassword("");
        }

        try {
            user.setCreatedAt(rs.getTimestamp("created_at"));
        } catch (SQLException e) {
            user.setCreatedAt(null);
        }

        return user;
    }

    @Override
    public boolean registerUser(User user) {
        Connection conn = null;
        boolean status = false;

        try {
            conn = openConnection();
            if (conn == null) return false;

            String query = "INSERT INTO users (name, email, password_hash, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone() != null ? user.getPhone() : "");

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                status = true;
                ResultSet rs = ps.getGeneratedKeys();
                int newUserId = 0;
                if (rs.next()) {
                    newUserId = rs.getInt(1);
                    user.setId(newUserId);
                }

                if (newUserId > 0 && user.getAddress() != null && !user.getAddress().isBlank()) {
                    try {
                        String addrQuery = "INSERT INTO addresses (user_id, full_name, phone, address_line1, city, state, postal_code, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement addrPs = conn.prepareStatement(addrQuery);
                        addrPs.setInt(1, newUserId);
                        addrPs.setString(2, user.getName());
                        addrPs.setString(3, user.getPhone() != null ? user.getPhone() : "");
                        addrPs.setString(4, user.getAddress());
                        addrPs.setString(5, user.getCity() != null ? user.getCity() : "");
                        addrPs.setString(6, user.getState() != null ? user.getState() : "");
                        addrPs.setString(7, user.getPincode() != null ? user.getPincode() : "");
                        addrPs.setString(8, "India");
                        addrPs.executeUpdate();
                    } catch (Exception addrEx) {
                        System.err.println("[Aura DAO Log] UserDAO.registerUser (Address): " + addrEx.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Aura DAO Log] UserDAO.registerUser: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return status;
    }

    @Override
    public User loginUser(String email, String password) {
        Connection conn = null;
        User user = null;

        try {
            conn = openConnection();
            if (conn == null) return null;

            String query = "SELECT * FROM users WHERE email = ? AND password_hash = ?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = mapUser(rs);
            }
        } catch (Exception e) {
            System.err.println("[Aura DAO Log] UserDAO.loginUser: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return user;
    }

    @Override
    public User getUserById(int userId) {
        Connection conn = null;
        User user = null;

        try {
            conn = openConnection();
            if (conn == null) return null;

            String query = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = mapUser(rs);
            }
        } catch (Exception e) {
            System.err.println("[Aura DAO Log] UserDAO.getUserById: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        Connection conn = null;
        User user = null;

        try {
            conn = openConnection();
            if (conn == null) return null;

            String query = "SELECT * FROM users WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = mapUser(rs);
            }
        } catch (Exception e) {
            System.err.println("[Aura DAO Log] UserDAO.getUserByEmail: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return user;
    }

    @Override
    public boolean updateUser(User user) {
        Connection conn = null;
        boolean status = false;

        try {
            conn = openConnection();
            if (conn == null) return false;

            String query = "UPDATE users SET name=?, phone=? WHERE user_id=?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, user.getName());
            ps.setString(2, user.getPhone());
            ps.setInt(3, user.getId());

            status = ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("[Aura DAO Log] UserDAO.updateUser: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return status;
    }

    @Override
    public boolean changePassword(int userId, String newPassword) {
        Connection conn = null;
        boolean status = false;

        try {
            conn = openConnection();
            if (conn == null) return false;

            String query = "UPDATE users SET password_hash=? WHERE user_id=?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            status = ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("[Aura DAO Log] UserDAO.changePassword: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return status;
    }

    @Override
    public boolean deleteUser(int userId) {
        Connection conn = null;
        boolean status = false;

        try {
            conn = openConnection();
            if (conn == null) return false;

            String query = "DELETE FROM users WHERE user_id=?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, userId);

            status = ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("[Aura DAO Log] UserDAO.deleteUser: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }

        return status;
    }
}