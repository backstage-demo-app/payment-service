# Payment Service

Core payment processing microservice handling transactions, refunds, and payment statistics.

## Overview

Payment Service is a Spring Boot microservice that provides a REST API for managing payments. It supports creating payments, listing/filtering by status, individual lookups, refunds, and aggregated statistics. Data is stored in-memory with seeded demo records.

## Architecture

```
storefront-ui (3001) → gateway-service (8081) → payment-service (8080)
```

Payment Service is the downstream backend that gateway-service proxies requests to.

## Quick Start

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run

# Test
mvn test
```

The service starts on **port 8080** by default.

## API Endpoints

| Endpoint | Method | Description |
|---|---|---|
| `/api/payments` | GET | List payments (filter by `?status=`) |
| `/api/payments` | POST | Create a new payment |
| `/api/payments/{id}` | GET | Get a payment by ID |
| `/api/payments/{id}/refund` | POST | Refund a payment |
| `/api/payments/stats` | GET | Aggregated payment statistics |
| `/actuator/health` | GET | Spring Actuator health check |
| `/actuator/info` | GET | Service info |
| `/actuator/metrics` | GET | Prometheus metrics |
| `/v3/api-docs` | GET | OpenAPI 3 spec (JSON) |
| `/swagger-ui.html` | GET | Swagger UI |

## Configuration

| Property | Default | Description |
|---|---|---|
| `server.port` | `8080` | Server listen port |

## Owner

This service is owned by **group:default/paybusters** and is part of the **demo-platform** system.
