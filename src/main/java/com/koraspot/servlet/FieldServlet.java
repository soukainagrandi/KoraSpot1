package com.koraspot.servlet;

import com.koraspot.dao.FieldDAO;
import com.koraspot.model.Field;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/fields")
public class FieldServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private FieldDAO fieldDAO = new FieldDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET /api/fields ou /api/fields?id=xx
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        try {
            if (idStr == null) {
                // Liste tous les terrains
                List<Field> fields = fieldDAO.listAllFields();
                String json = gson.toJson(fields);
                response.getWriter().write(json);
            } else {
                // Terrain spécifique
                int id = Integer.parseInt(idStr);
                Field field = fieldDAO.getField(id);
                if (field != null) {
                    String json = gson.toJson(field);
                    response.getWriter().write(json);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\":\"Terrain non trouvé\"}");
                }
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erreur serveur\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST /api/fields : création d’un terrain
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = request.getReader()) {
            Field field = gson.fromJson(reader, Field.class);
            if (field == null || field.getLocationId() == 0 || field.getSportId() == 0 || field.getName() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Paramètres invalides\"}");
                return;
            }
            fieldDAO.insertField(field);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"message\":\"Terrain créé avec succès\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erreur base de données\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // PUT /api/fields : mise à jour d’un terrain
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = request.getReader()) {
            Field field = gson.fromJson(reader, Field.class);
            if (field == null || field.getFieldId() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"ID du terrain requis\"}");
                return;
            }
            fieldDAO.updateField(field);
            response.getWriter().write("{\"message\":\"Terrain mis à jour avec succès\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erreur base de données\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // DELETE /api/fields?id=xx : suppression d’un terrain
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID du terrain requis\"}");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            fieldDAO.deleteField(id);
            response.getWriter().write("{\"message\":\"Terrain supprimé avec succès\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erreur base de données\"}");
        }
    }
}
