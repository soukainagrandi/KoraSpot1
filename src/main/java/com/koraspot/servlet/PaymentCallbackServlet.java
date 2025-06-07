package com.koraspot.servlet;

import com.koraspot.dao.AuthorizationDAO;
import com.koraspot.dao.PaymentDAO;
import com.koraspot.model.Authorization;
import com.koraspot.model.Payment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Date;

@WebServlet("/api/payments/callback")
public class PaymentCallbackServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PaymentDAO paymentDAO = new PaymentDAO();

	@SuppressWarnings("unused")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String status = req.getParameter("status");
	    String orderId = req.getParameter("orderId");
	    // Récupérer aussi matchId, userId (par exemple via paramètres ou en base via orderId)
	    String matchIdStr = req.getParameter("matchId");
	    String userIdStr = req.getParameter("userId");

	    resp.setContentType("text/plain; charset=UTF-8");

	    if ("success".equals(status)) {
	        try {
	            int matchId = Integer.parseInt(matchIdStr);
	            int userId = Integer.parseInt(userIdStr);

	            Payment payment = new Payment();
	            payment.setMatchId(matchId);
	            payment.setUserId(userId);
	            payment.setPaymentDate(new Date());
	            payment.setStatus("completed");
	            payment.setTransactionReference(orderId);

	            int newPaymentId = paymentDAO.createPayment(payment);

	            // Création de l’autorisation
	            AuthorizationDAO authDao = new AuthorizationDAO();
	            Authorization auth = new Authorization();
	            auth.setMatchId(matchId);
	            auth.setAuthorizedBy(userId);
	            auth.setAuthorizedAt(new Date());
	            authDao.createAuthorization(auth);

	            resp.getWriter().write("Paiement réussi et accès autorisé");

	        } catch (NumberFormatException e) {
	            resp.getWriter().write("Paramètres matchId ou userId invalides");
	        } catch (Exception e) {
	            resp.getWriter().write("Erreur lors du paiement ou de l'autorisation : " + e.getMessage());
	        }
	    } else {
	        resp.getWriter().write("Paiement échoué ou annulé");
	    }
	}

    
}
