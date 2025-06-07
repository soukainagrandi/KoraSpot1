package com.koraspot.dao;

import com.koraspot.model.TeamMember;
import com.koraspot.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamMemberDAO {

    public List<TeamMember> getMembersByTeamId(int teamId) throws SQLException {
        List<TeamMember> members = new ArrayList<>();
        String sql = "SELECT team_member_id, team_id, user_id, joined_at FROM team_members WHERE team_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TeamMember member = new TeamMember();
                    member.setTeamMemberId(rs.getInt("team_member_id"));
                    member.setTeamId(rs.getInt("team_id"));
                    member.setUserId(rs.getInt("user_id"));
                    member.setJoinedAt(rs.getTimestamp("joined_at"));
                    members.add(member);
                }
            }
        }
        return members;
    }

    public int addMember(TeamMember member) throws SQLException {
        String sql = "INSERT INTO team_members (team_id, user_id, joined_at) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, member.getTeamId());
            stmt.setInt(2, member.getUserId());
            stmt.setTimestamp(3, new Timestamp(member.getJoinedAt().getTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Ajouter membre équipe échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Ajouter membre équipe échoué, aucun ID obtenu.");
                }
            }
        }
    }

    // Méthodes update et delete peuvent être ajoutées si besoin
    
    public void updateTeamMember(TeamMember member) throws SQLException {
        String sql = "UPDATE team_members SET team_id = ?, user_id = ?, joined_at = ? WHERE team_member_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, member.getTeamId());
            stmt.setInt(2, member.getUserId());
            stmt.setTimestamp(3, new Timestamp(member.getJoinedAt().getTime()));
            stmt.setInt(4, member.getTeamMemberId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Mise à jour membre équipe échouée, aucune ligne affectée.");
            }
        }
    }

    public void deleteTeamMember(int teamMemberId) throws SQLException {
        String sql = "DELETE FROM team_members WHERE team_member_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teamMemberId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Suppression membre équipe échouée, aucune ligne affectée.");
            }
        }
    }

    
    
}
