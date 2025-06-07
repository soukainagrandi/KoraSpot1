package com.koraspot.dao;

import com.koraspot.model.Team;
import com.koraspot.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    public List<Team> getTeamsByMatchId(int matchId) throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT team_id, match_id, name, is_home_team FROM teams WHERE match_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team();
                    team.setTeamId(rs.getInt("team_id"));
                    team.setMatchId(rs.getInt("match_id"));
                    team.setName(rs.getString("name"));
                    team.setHomeTeam(rs.getBoolean("is_home_team"));
                    teams.add(team);
                }
            }
        }
        return teams;
    }

    public int createTeam(Team team) throws SQLException {
        String sql = "INSERT INTO teams (match_id, name, is_home_team) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, team.getMatchId());
            stmt.setString(2, team.getName());
            stmt.setBoolean(3, team.isHomeTeam());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Créer équipe a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Créer équipe a échoué, aucun ID obtenu.");
                }
            }
        }
    }

    // Méthodes supplémentaires (update, delete) peuvent être ajoutées selon besoin
    
    public void updateTeam(Team team) throws SQLException {
        String sql = "UPDATE teams SET match_id = ?, name = ?, is_home_team = ? WHERE team_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, team.getMatchId());
            stmt.setString(2, team.getName());
            stmt.setBoolean(3, team.isHomeTeam());
            stmt.setInt(4, team.getTeamId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Mise à jour équipe échouée, aucune ligne affectée.");
            }
        }
    }

    public void deleteTeam(int teamId) throws SQLException {
        String sql = "DELETE FROM teams WHERE team_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Suppression équipe échouée, aucune ligne affectée.");
            }
        }
    }

}
