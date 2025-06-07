package com.koraspot.model;

import java.util.Date;

public class Authorization {
    private int authorizationId;
    private int matchId;
    private Date authorizedAt;
    private int authorizedBy;

    // Getters & Setters
    public int getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(int authorizationId) {
        this.authorizationId = authorizationId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public Date getAuthorizedAt() {
        return authorizedAt;
    }

    public void setAuthorizedAt(Date authorizedAt) {
        this.authorizedAt = authorizedAt;
    }

    public int getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(int authorizedBy) {
        this.authorizedBy = authorizedBy;
    }
}
