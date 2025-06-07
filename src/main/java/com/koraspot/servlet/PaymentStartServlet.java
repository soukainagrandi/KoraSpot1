package com.koraspot.servlet;

import com.google.gson.Gson;
import com.koraspot.model.Payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/payments/start")
public class PaymentStartServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String STORE_KEY = "VOTRE_STOREKEY";
    private static final String CLIENT_ID = "VOTRE_CLIENTID";
    private static final String CURRENCY = "504"; // Code devise (ex: 504 pour MAD)
    private static final String LANG = "fr";

    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("application/json");
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            Payment paymentRequest = gson.fromJson(req.getReader(), Payment.class);

            if (paymentRequest == null
                    || paymentRequest.getMatchId() == 0
                    || paymentRequest.getUserId() == 0
                    || paymentRequest.getAmount() == null
                    || paymentRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Paramètres invalides");
                resp.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            String orderId = "CMD" + System.currentTimeMillis();
            String amount  = paymentRequest.getAmount().setScale(2, RoundingMode.HALF_UP).toPlainString();
            String okUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/api/payments/callback?status=success&orderId=" + orderId;
            String failUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/api/payments/callback?status=fail&orderId=" + orderId;

            // Paramètres obligatoires pour CMI
            Map<String, String> params = new HashMap<>();
            params.put("clientid", CLIENT_ID);
            params.put("amount", amount);
            params.put("oid", orderId);
            params.put("okUrl", okUrl);
            params.put("failUrl", failUrl);
            params.put("islemtipi", "Auth"); // type de transaction
            params.put("storetype", "3d_pay"); // type de paiement 3D Secure
            params.put("lang", LANG);
            params.put("currency", CURRENCY);

            // Calcul de la signature (hash)
            String hashStr = CLIENT_ID + orderId + amount + okUrl + failUrl + STORE_KEY;
            String hash = base64Sha1(hashStr);

            params.put("hash", hash);

            // On renvoie ces données au frontend pour générer le formulaire HTML de paiement
            jsonResponse.put("paymentParams", params);

            resp.getWriter().write(gson.toJson(jsonResponse));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", "Erreur serveur");
            resp.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    private String base64Sha1(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(digest);
    }
}
