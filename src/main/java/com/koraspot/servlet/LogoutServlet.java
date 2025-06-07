package com.koraspot.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        Map<String, Object> jsonResponse = new HashMap<>();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (session != null) {
            session.invalidate();
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Déconnexion réussie");
        } else {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Aucune session active");
        }

        String json = gson.toJson(jsonResponse);
        response.getWriter().write(json);
    }
}
