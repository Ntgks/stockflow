# StockFlow — Inventory Management REST API

StockFlow is a backend REST API for managing product inventory, built with Java, Spring Boot, and PostgreSQL.

The project demonstrates a layered backend architecture, CRUD operations, input validation, centralized exception handling, and automated unit testing.

## Tech Stack

- **Java 21**
- **Spring Boot 4.1.2**
- **Spring Web MVC**
- **Spring Data JPA / Hibernate**
- **PostgreSQL 17**
- **Docker**
- **Maven**
- **JUnit 5 & Mockito**

## Features

- Create products with unique SKUs
- Retrieve all products or find a product by ID
- Update existing products
- Delete products
- Validate incoming requests
- Handle duplicate SKUs with HTTP 409 Conflict
- Handle missing products with HTTP 404 Not Found
- Return validation errors with HTTP 400 Bad Request
- Automated unit tests for service-layer business logic

## Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

**Controller:** Handles HTTP requests and responses.

**Service:** Contains business logic and product operations.

**Repository:** Provides database access using Spring Data JPA.

**DTOs:** Separate API request and response data from persistence entities.

**Exception Handler:** Provides consistent error responses.

## API Endpoints

| Method | Endpoint | Description | Success Status |
|---|---|---|---|
| GET | `/api/products` | Get all products | 200 OK |
| GET | `/api/products/{id}` | Get a product by ID | 200 OK |
| POST | `/api/products` | Create a product | 201 Created |
| PUT | `/api/products/{id}` | Update a product | 200 OK |
| DELETE | `/api/products/{id}` | Delete a product | 204 No Content |

## Example Product Request

```json
{
  "name": "Laptop",
  "sku": "LAP-001",
  "quantity": 10,
  "price": 999.99
}
```

## Getting Started

### Prerequisites

- Java 21
- Docker
- Git

### 1. Clone the repository

```bash
git clone https://github.com/Ntgks/stockflow.git
cd stockflow
```

### 2. Start PostgreSQL using Docker

```bash
docker run --name stockflow-postgres -e POSTGRES_DB=stockflow_db -e POSTGRES_USER=stockflow -e POSTGRES_PASSWORD=stockflow_dev_password -p 5433:5432 -d postgres:17
```

This command creates a PostgreSQL container for local development.

If the container already exists but is stopped, run:

```bash
docker start stockflow-postgres
```

### 3. Configure the database password

The application reads the database password from the `DB_PASSWORD` environment variable.

In PowerShell:

```powershell
$env:DB_PASSWORD="stockflow_dev_password"
```

The password shown here is for local development only. Use a different, securely managed password in other environments.

### 4. Run the application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will be available at:

`http://localhost:8080/api/products`

## Running Tests

Run the service unit tests:

```powershell
.\mvnw.cmd "-Dtest=ProductServiceTest" test
```

The project currently includes **11 passing service unit tests**, covering successful operations and error scenarios.

Run the complete test suite:

```powershell
.\mvnw.cmd test
```

The complete suite requires the configured PostgreSQL database and `DB_PASSWORD` environment variable to be available.

## Error Handling

| HTTP Status | Scenario |
|---|---|
| 400 Bad Request | Invalid product data |
| 404 Not Found | Product does not exist |
| 409 Conflict | SKU already belongs to another product |

## Future Improvements

- Controller tests with MockMvc
- Integration tests
- Pagination and sorting
- Product search and filtering
- API documentation with OpenAPI / Swagger
- Docker Compose configuration
- CI pipeline with GitHub Actions

## Author

Developed as a backend development learning project.

GitHub: https://github.com/Ntgks