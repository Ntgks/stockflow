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
- PostgreSQL
- Spring Data JPA
- Flyway
- Testcontainers

## Run locally

Requires JDK 21 and Docker running.
Maven is provided through the Maven Wrapper.

```bash
git clone https://github.com/Ntgks/stockflow.git
cd stockflow
```

Create a `.env` file in the project root:

```dotenv
DB_PASSWORD=stockflow-local-dev-password
```

Start PostgreSQL:

```bash
docker compose up -d
```

The database is available at localhost:5434.

Start the application on Windows (PowerShell):

```powershell
$env:DB_PASSWORD = "stockflow-local-dev-password"
.\mvnw.cmd spring-boot:run
```

On macOS / Linux:

```bash
export DB_PASSWORD="stockflow-local-dev-password"
sh mvnw spring-boot:run
```

Use the same password in `.env` and the environment variable.
Docker Compose reads `.env`; Spring Boot uses the environment variable.

The API runs at http://localhost:8081.

## Tests

Requires JDK 21 and Docker running.
Integration tests use a temporary PostgreSQL container.

On Windows:

```powershell
.\mvnw.cmd test
```

On macOS / Linux:

```bash
sh mvnw test
```

## API endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/products | List products |
| POST | /api/products | Create a product |

## Storage

Products are stored in PostgreSQL.
Database schema changes are managed with Flyway.

Prices are expressed in EUR and represented with BigDecimal.

## Planned features

- Order creation with atomic stock updates
- Idempotent order requests
- Order cancellation with stock restoration