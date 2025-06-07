package com.koraspot.model;

import java.util.Date;

public class TeamMember {
    private int teamMemberId;
    private int teamId;
    private int userId;
    private Date joinedAt;

    public TeamMember() {}

    public TeamMember(int teamMemberId, int teamId, int userId, Date joinedAt) {
        this.teamMemberId = teamMemberId;
        this.teamId = teamId;
        this.userId = userId;
        this.joinedAt = joinedAt;
    }

    // Getters et setters
    public int getTeamMemberId() { return teamMemberId; }
    public void setTeamMemberId(int teamMemberId) { this.teamMemberId = teamMemberId; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Date joinedAt) { this.joinedAt = joinedAt; }
}
