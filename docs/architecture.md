# Architecture

## Technology Stack

- **Runtime:** Java 17
- **Framework:** Spring Boot 3.3.x
- **Build Tool:** Maven
- **API Docs:** springdoc-openapi 2.5.0
- **Container:** Docker (Eclipse Temurin Alpine)

## Project Structure

```
payment-service/
├── src/
│   └── main/
│       ├── java/com/demo/paymentservice/
│       │   ├── Application.java        # Main entry point
│       │   ├── PaymentController.java   # Payment REST API
│       │   └── WebConfig.java           # CORS configuration
│       └── resources/
│           └── application.properties   # Configuration
├── docs/                                # TechDocs documentation
├── Dockerfile                           # Container image
├── pom.xml                              # Maven build
├── catalog-info.yaml                    # Backstage catalog entity
└── mkdocs.yml                           # TechDocs config
```

## Data Model

Payments are stored in-memory using a `ConcurrentHashMap`. Each payment contains:

| Field | Type | Description |
|---|---|---|
| `id` | Long | Auto-generated ID |
| `method` | String | Payment method (credit_card, paypal) |
| `amount` | Double | Payment amount |
| `currency` | String | Currency code (USD, EUR) |
| `status` | String | `completed` or `refunded` |
| `orderId` | String | Associated order reference |
| `customerId` | String | Customer identifier |
| `createdAt` | String | ISO timestamp |
| `refundedAt` | String | ISO timestamp (only if refunded) |

Three demo payments are seeded on startup.

## Service Dependencies

Payment Service has no upstream dependencies — it is the leaf service in the platform.

```
storefront-ui → gateway-service → payment-service (this)
```

## Health & Monitoring

- `/actuator/health` — Liveness/readiness probe
- `/actuator/metrics` — Micrometer metrics
- Structured logging with timestamp, thread, level, and logger
