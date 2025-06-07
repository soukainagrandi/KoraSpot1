package com.koraspot.servlet;

import com.google.gson.Gson;
import com.koraspot.dao.AuthorizationDAO;
import com.koraspot.model.Authorization;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/authorizations")
public class AuthorizationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthorizationDAO dao = new AuthorizationDAO();
    private Gson gson = new Gson();

    // GET ?matchId=...
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String matchIdParam = req.getParameter("matchId");
        if (matchIdParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"matchId manquant\"}");
            return;
        }
        try {
            int matchId = Integer.parseInt(matchIdParam);
            List<Authorization> list = dao.getAuthorizationsByMatch(matchId);
            resp.getWriter().write(gson.toJson(list));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"matchId invalide\"}");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Erreur BD\"}");
        }
    }

    // POST { "matchId":123, "authorizedBy":5 }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try (BufferedReader reader = req.getReader()) {
            Authorization a = gson.fromJson(reader, Authorization.class);
            // champ authorizedAt rempli automatiquement
            a.setAuthorizedAt(new java.util.Date());
            dao.createAuthorization(a);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Autorisation créée\"}");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Impossible de créer\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"JSON invalide\"}");
        }
    }
    
    

}
