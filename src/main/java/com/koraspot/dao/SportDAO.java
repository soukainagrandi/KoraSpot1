package com.koraspot.dao;

import com.koraspot.model.Sport;
import com.koraspot.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SportDAO {

    public List<Sport> listAllSports() throws Exception {
        List<Sport> sports = new ArrayList<>();
        String sql = "SELECT sportid, name FROM sports";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sport sport = new Sport();
                sport.setSportId(rs.getInt("sportid"));
                sport.setName(rs.getString("name"));
                sports.add(sport);
            }
        }
        return sports;
    }
}
