package com.koraspot.servlet;

import com.koraspot.dao.UserDAO;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO = new UserDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lecture des paramètres JSON depuis le corps de la requête
        // Ici on suppose que le frontend envoie un JSON { "email": "...", "password": "..." }
        StringBuilder sb = new StringBuilder();
        String line;
        try (var reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
            	sb.append(line);
            }
        }
             
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> requestData = gson.fromJson(sb.toString(), type);

        String email = requestData.get("email");
        String password = requestData.get("password");

        Map<String, Object> jsonResponse = new HashMap<>();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (userDAO.checkLogin(email, password)) {
                // Authentification réussie : création de session
                HttpSession session = request.getSession();
                session.setAttribute("userEmail", email);

                jsonResponse.put("success", true);
                jsonResponse.put("message", "Connexion réussie");
                // Tu peux aussi renvoyer des infos utilisateur si besoin
            } else {
                // Échec d'authentification
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Email ou mot de passe incorrect");
            }
            String json = gson.toJson(jsonResponse);
            response.getWriter().write(json);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Erreur serveur");
            String json = gson.toJson(jsonResponse);
            response.getWriter().write(json);
        }
    }
}
