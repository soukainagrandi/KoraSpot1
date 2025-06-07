package com.koraspot.servlet;

import com.koraspot.dao.SportDAO;
import com.koraspot.model.Sport;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/sports")
public class SportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private SportDAO sportDAO = new SportDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Sport> sports = sportDAO.listAllSports();
            String json = gson.toJson(sports);
            response.getWriter().write(json);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erreur serveur lors de la récupération des sports\"}");
        }
    }
}
