package com.koraspot.dao;

import com.koraspot.model.Match;
import com.koraspot.util.DatabaseUtil;

import java.sql.*;

public class MatchDAO {

    public int createMatch(Match match) throws SQLException {
        String sql = "INSERT INTO matches (field_id, creator_id, sport_id, scheduled_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, match.getFieldId());
            stmt.setInt(2, match.getCreatorId());
            stmt.setInt(3, match.getSportId());
            stmt.setTimestamp(4, new Timestamp(match.getScheduledDate().getTime()));
            stmt.setString(5, match.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Créer match a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Créer match a échoué, aucun ID obtenu.");
                }
            }
        }
    }
}
