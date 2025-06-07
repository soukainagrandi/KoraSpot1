package com.koraspot.model;

public class Team {
    private int teamId;
    private int matchId;
    private String name;
    private boolean isHomeTeam;

    public Team() {}

    public Team(int teamId, int matchId, String name, boolean isHomeTeam) {
        this.teamId = teamId;
        this.matchId = matchId;
        this.name = name;
        this.isHomeTeam = isHomeTeam;
    }

    // Getters et setters
    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isHomeTeam() { return isHomeTeam; }
    public void setHomeTeam(boolean homeTeam) { isHomeTeam = homeTeam; }
}
