package com.koraspot.servlet;

import com.koraspot.dao.UserDAO;
import com.koraspot.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userDAO = new UserDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> jsonResponse = new HashMap<>();

        try (BufferedReader reader = request.getReader()) {
            JsonObject jsonRequest = gson.fromJson(reader, JsonObject.class);

            if (jsonRequest == null
                    || !jsonRequest.has("name")
                    || !jsonRequest.has("email")
                    || !jsonRequest.has("password")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Paramètres manquants");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            String name = jsonRequest.get("name").getAsString();
            String email = jsonRequest.get("email").getAsString();
            String password = jsonRequest.get("password").getAsString();
            String phone = jsonRequest.has("phone") ? jsonRequest.get("phone").getAsString() : null;

            User newUser = new User(name, email, password, phone);

            boolean success = userDAO.registerUser(newUser);

            if (success) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Inscription réussie");
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Erreur lors de l'inscription (email peut-être déjà utilisé)");
            }
            response.getWriter().write(gson.toJson(jsonResponse));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Erreur serveur");
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }
}
