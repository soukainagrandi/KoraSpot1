package com.koraspot.dao;

import com.koraspot.model.User;
import com.koraspot.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public boolean checkLogin(String email, String password) throws Exception {
        String sql = "SELECT user_id FROM users WHERE email = ? AND password_hash = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password); // mot de passe en clair
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true si un utilisateur correspond
            }
        }
    }
    
    public boolean registerUser(User user) throws Exception {
        String sql = "INSERT INTO users (name, email, password_hash, phone) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword()); // mot de passe en clair, non sécurisé
            stmt.setString(4, user.getPhone());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }
    
    
    
    
}
