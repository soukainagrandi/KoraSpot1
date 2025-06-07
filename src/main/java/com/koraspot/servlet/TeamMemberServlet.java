package com.koraspot.servlet;

import com.koraspot.dao.TeamMemberDAO;
import com.koraspot.model.TeamMember;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/team-members")
public class TeamMemberServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TeamMemberDAO teamMemberDAO = new TeamMemberDAO();
    private Gson gson = new Gson();

    // Récupérer les membres d’une équipe : GET /api/team-members?teamId=xx
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String teamIdStr = request.getParameter("teamId");
        if (teamIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Paramètre teamId requis\"}");
            return;
        }

        try {
            int teamId = Integer.parseInt(teamIdStr);
            List<TeamMember> members = teamMemberDAO.getMembersByTeamId(teamId);
            String json = gson.toJson(members);
            response.getWriter().write(json);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erreur serveur\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"teamId invalide\"}");
        }
    }

    // Ajouter un membre à une équipe : POST /api/team-members avec JSON { teamId, userId }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = new HashMap<>();

        try (BufferedReader reader = request.getReader()) {
            TeamMember member = gson.fromJson(reader, TeamMember.class);

            if (member == null || member.getTeamId() == 0 || member.getUserId() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Paramètres invalides");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            // Définit la date d'ajout à maintenant
            member.setJoinedAt(new Date());

            int newId = teamMemberDAO.addMember(member);
            jsonResponse.put("success", true);
            jsonResponse.put("teamMemberId", newId);
            jsonResponse.put("message", "Membre ajouté avec succès");
            response.getWriter().write(gson.toJson(jsonResponse));

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Erreur base de données");
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }
}
