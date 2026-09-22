// UserDAO.java

package com.fashionstore.dao.interfaces;

import com.fashionstore.model.User;

public interface UserDAO {

    boolean registerUser(User user);

    User loginUser(String email, String password);

    User getUserById(int userId);

    User getUserByEmail(String email);

    boolean updateUser(User user);

    boolean changePassword(int userId, String newPassword);

    boolean deleteUser(int userId);
}