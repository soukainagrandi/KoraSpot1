package com.koraspot.dao;

import com.koraspot.model.Field;
import com.koraspot.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FieldDAO {

    public List<Field> listAllFields() throws SQLException {
        List<Field> list = new ArrayList<>();
        String sql = "SELECT * FROM fields";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Field field = new Field();
                field.setFieldId(rs.getInt("field_id"));
                field.setLocationId(rs.getInt("location_id"));
                field.setSportId(rs.getInt("sport_id"));
                field.setName(rs.getString("name"));
                field.setDescription(rs.getString("description"));
                field.setPricePerHour(rs.getDouble("price_per_hour"));
                list.add(field);
            }
        }
        return list;
    }

    public void insertField(Field field) throws SQLException {
        String sql = "INSERT INTO fields (location_id, sport_id, name, description, price_per_hour) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, field.getLocationId());
            stmt.setInt(2, field.getSportId());
            stmt.setString(3, field.getName());
            stmt.setString(4, field.getDescription());
            stmt.setDouble(5, field.getPricePerHour());
            stmt.executeUpdate();
        }
    }

    public Field getField(int fieldId) throws SQLException {
        Field field = null;
        String sql = "SELECT * FROM fields WHERE field_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fieldId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    field = new Field();
                    field.setFieldId(rs.getInt("field_id"));
                    field.setLocationId(rs.getInt("location_id"));
                    field.setSportId(rs.getInt("sport_id"));
                    field.setName(rs.getString("name"));
                    field.setDescription(rs.getString("description"));
                    field.setPricePerHour(rs.getDouble("price_per_hour"));
                }
            }
        }
        return field;
    }

    public void updateField(Field field) throws SQLException {
        String sql = "UPDATE fields SET location_id = ?, sport_id = ?, name = ?, description = ?, price_per_hour = ? WHERE field_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, field.getLocationId());
            stmt.setInt(2, field.getSportId());
            stmt.setString(3, field.getName());
            stmt.setString(4, field.getDescription());
            stmt.setDouble(5, field.getPricePerHour());
            stmt.setInt(6, field.getFieldId());
            stmt.executeUpdate();
        }
    }

    public void deleteField(int fieldId) throws SQLException {
        String sql = "DELETE FROM fields WHERE field_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fieldId);
            stmt.executeUpdate();
        }
    }
}
