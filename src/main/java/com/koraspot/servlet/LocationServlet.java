package com.koraspot.servlet;

import com.koraspot.dao.LocationDAO;
import com.koraspot.model.Location;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/locations")
public class LocationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private LocationDAO locationDAO = new LocationDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");

        try {
            if (idStr == null) {
                // Liste toutes les localisations
                List<Location> locations = locationDAO.listAllLocations();
                String json = gson.toJson(locations);
                response.getWriter().write(json);
            } else {
                // Localisation spécifique
                int id = Integer.parseInt(idStr);
                Location location = locationDAO.getLocationById(id);
                if (location != null) {
                    String json = gson.toJson(location);
                    response.getWriter().write(json);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\":\"Localisation non trouvée\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erreur serveur\"}");
        }
    }
}
