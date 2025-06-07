package com.koraspot.servlet;

import com.koraspot.dao.FieldDAO;
import com.koraspot.dao.MatchDAO;
import com.koraspot.model.Field;
import com.koraspot.model.Match;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/create-match")
public class MatchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private FieldDAO fieldDAO = new FieldDAO();
    private MatchDAO matchDAO = new MatchDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET /api/create-match?fieldId=xx : renvoie les infos du terrain en JSON
        String fieldIdStr = request.getParameter("fieldId");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (fieldIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"fieldId manquant\"}");
            return;
        }

        try {
            int fieldId = Integer.parseInt(fieldIdStr);
            Field field = fieldDAO.getField(fieldId);
            if (field == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Terrain introuvable\"}");
                return;
            }
            String json = gson.toJson(field);
            response.getWriter().write(json);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erreur serveur\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST /api/create-match avec JSON : { "fieldId":..., "scheduledDate":"yyyy-MM-dd'T'HH:mm" }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Map<String, Object> jsonResponse = new HashMap<>();

        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Utilisateur non authentifié");
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }

        try (BufferedReader reader = request.getReader()) {
            JsonObject jsonRequest = gson.fromJson(reader, JsonObject.class);

            if (jsonRequest == null || !jsonRequest.has("fieldId") || !jsonRequest.has("scheduledDate")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Paramètres manquants");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            int fieldId = jsonRequest.get("fieldId").getAsInt();
            String dateStr = jsonRequest.get("scheduledDate").getAsString();

            Field field = fieldDAO.getField(fieldId);
            if (field == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Terrain introuvable");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date scheduledDate = sdf.parse(dateStr);

            Match match = new Match();
            match.setFieldId(fieldId);
            match.setCreatorId((Integer) session.getAttribute("userId"));
            match.setSportId(field.getSportId());
            match.setScheduledDate(scheduledDate);
            match.setStatus("pending");

            int matchId = matchDAO.createMatch(match);

            jsonResponse.put("success", true);
            jsonResponse.put("message", "Match créé avec succès");
            jsonResponse.put("matchId", matchId);
            response.getWriter().write(gson.toJson(jsonResponse));

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Erreur base de données");
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Format de date invalide");
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }
}
