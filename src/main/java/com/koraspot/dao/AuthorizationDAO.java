package com.koraspot.dao;

import com.koraspot.model.Authorization;
import com.koraspot.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationDAO {

    public void createAuthorization(Authorization authorization) throws SQLException {
        String sql = "INSERT INTO authorizations (match_id, authorized_at, authorized_by) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorization.getMatchId());
            stmt.setTimestamp(2, new Timestamp(authorization.getAuthorizedAt().getTime()));
            stmt.setInt(3, authorization.getAuthorizedBy());
            stmt.executeUpdate();
        }
    }

    public List<Authorization> getAuthorizationsByMatch(int matchId) throws SQLException {
        List<Authorization> list = new ArrayList<>();
        String sql = "SELECT * FROM authorizations WHERE match_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Authorization auth = new Authorization();
                    auth.setAuthorizationId(rs.getInt("authorization_id"));
                    auth.setMatchId(rs.getInt("match_id"));
                    auth.setAuthorizedAt(rs.getTimestamp("authorized_at"));
                    auth.setAuthorizedBy(rs.getInt("authorized_by"));
                    list.add(auth);
                }
            }
        }
        return list;
    }
}
