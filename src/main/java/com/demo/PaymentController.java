package com.demo.paymentservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final Map<Long, Map<String, Object>> payments = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public PaymentController() {
        // Seed some demo data
        createPaymentInternal("credit_card", 49.99, "USD", "order-1001", "customer-1");
        createPaymentInternal("credit_card", 129.50, "USD", "order-1002", "customer-2");
        createPaymentInternal("paypal", 25.00, "EUR", "order-1003", "customer-3");
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listPayments(
            @RequestParam(defaultValue = "completed") String status) {
        List<Map<String, Object>> filtered = payments.values().stream()
                .filter(p -> "all".equals(status) || status.equals(p.get("status")))
                .toList();
        return ResponseEntity.ok(Map.of(
                "payments", filtered,
                "total", filtered.size()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPayment(@PathVariable Long id) {
        Map<String, Object> payment = payments.get(id);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Payment not found", "id", id));
        }
        return ResponseEntity.ok(payment);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Map<String, Object> request) {
        String method = (String) request.getOrDefault("method", "credit_card");
        double amount = ((Number) request.getOrDefault("amount", 0)).doubleValue();
        String currency = (String) request.getOrDefault("currency", "USD");
        String orderId = (String) request.getOrDefault("orderId", "order-" + System.currentTimeMillis());
        String customerId = (String) request.getOrDefault("customerId", "anonymous");

        if (amount <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Amount must be greater than 0"));
        }

        Map<String, Object> payment = createPaymentInternal(method, amount, currency, orderId, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<Map<String, Object>> refundPayment(@PathVariable Long id) {
        Map<String, Object> payment = payments.get(id);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Payment not found", "id", id));
        }
        if ("refunded".equals(payment.get("status"))) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Payment already refunded", "id", id));
        }

        Map<String, Object> updated = new HashMap<>(payment);
        updated.put("status", "refunded");
        updated.put("refundedAt", Instant.now().toString());
        payments.put(id, updated);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long total = payments.size();
        long completed = payments.values().stream().filter(p -> "completed".equals(p.get("status"))).count();
        long refunded = payments.values().stream().filter(p -> "refunded".equals(p.get("status"))).count();
        double totalAmount = payments.values().stream()
                .filter(p -> "completed".equals(p.get("status")))
                .mapToDouble(p -> ((Number) p.get("amount")).doubleValue())
                .sum();

        return ResponseEntity.ok(Map.of(
                "totalPayments", total,
                "completed", completed,
                "refunded", refunded,
                "totalAmount", totalAmount,
                "currency", "USD"
        ));
    }

    private Map<String, Object> createPaymentInternal(String method, double amount, String currency,
                                                       String orderId, String customerId) {
        long id = idGenerator.getAndIncrement();
        Map<String, Object> payment = new LinkedHashMap<>();
        payment.put("id", id);
        payment.put("method", method);
        payment.put("amount", amount);
        payment.put("currency", currency);
        payment.put("status", "completed");
        payment.put("orderId", orderId);
        payment.put("customerId", customerId);
        payment.put("createdAt", Instant.now().toString());
        payments.put(id, payment);
        return payment;
    }
}
