package com.koraspot.dao;

import com.koraspot.model.Payment;
import com.koraspot.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

	 public int createPayment(Payment payment) throws SQLException {
	        String sql = "INSERT INTO payments (match_id, user_id, amount, payment_date, status, payment_method, transaction_reference) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        try (Connection conn = DatabaseUtil.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            stmt.setInt(1, payment.getMatchId());
	            stmt.setInt(2, payment.getUserId());
	            stmt.setBigDecimal(3, payment.getAmount());
	            stmt.setTimestamp(4, new Timestamp(payment.getPaymentDate().getTime()));
	            stmt.setString(5, payment.getStatus());
	            stmt.setString(6, payment.getPaymentMethod());
	            stmt.setString(7, payment.getTransactionReference());

	            int affectedRows = stmt.executeUpdate();
	            if (affectedRows == 0) throw new SQLException("Échec création paiement");

	            try (ResultSet keys = stmt.getGeneratedKeys()) {
	                if (keys.next()) return keys.getInt(1);
	                else throw new SQLException("Aucun ID généré");
	            }
	        }
	    }

    public List<Payment> getPaymentsByUserId(int userId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE user_id = ? ORDER BY payment_date DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("payment_id"));
                    payment.setMatchId(rs.getInt("match_id"));
                    payment.setUserId(rs.getInt("user_id"));
                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setPaymentDate(rs.getTimestamp("payment_date"));
                    payment.setStatus(rs.getString("status"));
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setTransactionReference(rs.getString("transaction_reference"));
                    payments.add(payment);
                }
            }
        }
        return payments;
    }

    // Tu peux ajouter d'autres méthodes (update, delete, getById) selon besoin
}
