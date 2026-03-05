# API Reference

## Base URL

```
http://localhost:8080
```

## REST Endpoints

### GET /api/payments

List payments, optionally filtered by status.

**Query Parameters:**

| Parameter | Default | Description |
|---|---|---|
| `status` | `completed` | Filter by status (`completed`, `refunded`, `all`) |

**Response:**

```json
{
  "payments": [
    {
      "id": 1,
      "method": "credit_card",
      "amount": 49.99,
      "currency": "USD",
      "status": "completed",
      "orderId": "order-1001",
      "customerId": "customer-1",
      "createdAt": "2026-03-05T18:00:00Z"
    }
  ],
  "total": 1
}
```

### POST /api/payments

Create a new payment.

**Request Body:**

```json
{
  "method": "credit_card",
  "amount": 99.99,
  "currency": "USD",
  "orderId": "order-2001",
  "customerId": "customer-5"
}
```

**Response (201):**

```json
{
  "id": 4,
  "method": "credit_card",
  "amount": 99.99,
  "currency": "USD",
  "status": "completed",
  "orderId": "order-2001",
  "customerId": "customer-5",
  "createdAt": "2026-03-05T18:10:00Z"
}
```

### GET /api/payments/{id}

Retrieve a single payment by ID.

**Response:**

```json
{
  "id": 1,
  "method": "credit_card",
  "amount": 49.99,
  "currency": "USD",
  "status": "completed",
  "orderId": "order-1001",
  "customerId": "customer-1",
  "createdAt": "2026-03-05T18:00:00Z"
}
```

**Error (404):**

```json
{
  "error": "Payment not found",
  "id": 999
}
```

### POST /api/payments/{id}/refund

Refund a completed payment.

**Response:**

```json
{
  "id": 1,
  "method": "credit_card",
  "amount": 49.99,
  "currency": "USD",
  "status": "refunded",
  "orderId": "order-1001",
  "customerId": "customer-1",
  "createdAt": "2026-03-05T18:00:00Z",
  "refundedAt": "2026-03-05T19:00:00Z"
}
```

### GET /api/payments/stats

Aggregated payment statistics.

**Response:**

```json
{
  "totalPayments": 3,
  "completed": 2,
  "refunded": 1,
  "totalAmount": 179.49,
  "currency": "USD"
}
```

### GET /actuator/health

Spring Boot Actuator health endpoint.

**Response:**

```json
{
  "status": "UP"
}
```

## OpenAPI / Swagger

- **OpenAPI JSON:** `GET /v3/api-docs`
- **OpenAPI YAML:** `GET /v3/api-docs.yaml`
- **Swagger UI:** `GET /swagger-ui.html`
