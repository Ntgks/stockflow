# StockFlow

An order and inventory REST API built with Spring Boot.
Currently under development.

## Current features

- List products
- Create products
- Validate product details, prices and stock quantities
- Return validation errors by field
- Reject duplicate product SKUs with HTTP 409

## Tech stack

- Java 21
- Spring Boot 4.1.2
- Maven

## Run locally

Requires JDK 21.

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS / Linux:

```bash
sh mvnw spring-boot:run
```

The API runs at http://localhost:8081.

## API endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/products | List products |
| POST | /api/products | Create a product |

## Storage

Products are currently stored in memory and reset when the application restarts.

Prices are expressed in EUR and represented with BigDecimal.

## Planned features

- PostgreSQL persistence
- Order creation with atomic stock updates
- Idempotent order requests
- Order cancellation with stock restoration