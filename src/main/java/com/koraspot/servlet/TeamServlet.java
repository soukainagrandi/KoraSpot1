package com.koraspot.servlet;

import com.koraspot.dao.TeamDAO;
import com.koraspot.model.Team;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/teams")
public class TeamServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TeamDAO teamDAO = new TeamDAO();
    private Gson gson = new Gson();

    // Récupérer les équipes d’un match : GET /api/teams?matchId=xx
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String matchIdStr = request.getParameter("matchId");
        if (matchIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Paramètre matchId requis\"}");
            return;
        }

        try {
            int matchId = Integer.parseInt(matchIdStr);
            List<Team> teams = teamDAO.getTeamsByMatchId(matchId);
            String json = gson.toJson(teams);
            response.getWriter().write(json);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erreur serveur\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"matchId invalide\"}");
        }
    }

    // Créer une équipe : POST /api/teams avec JSON { matchId, name, isHomeTeam }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = new HashMap<>();

        try (BufferedReader reader = request.getReader()) {
            Team team = gson.fromJson(reader, Team.class);

            if (team == null || team.getMatchId() == 0 || team.getName() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Paramètres invalides");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            int newTeamId = teamDAO.createTeam(team);
            jsonResponse.put("success", true);
            jsonResponse.put("teamId", newTeamId);
            jsonResponse.put("message", "Équipe créée avec succès");
            response.getWriter().write(gson.toJson(jsonResponse));

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Erreur base de données");
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }
}
