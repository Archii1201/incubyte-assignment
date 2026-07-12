# 🚗 Car Dealership Inventory System

A production-grade REST API and single-page application for managing vehicle inventory with concurrent-safe purchasing, role-based access control, and comprehensive audit logging.

**GitHub:** [https://github.com/Archii1201/car-dealership-inventory](https://github.com/Archii1201/incubyte-assignment)

---

## 📋 Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Setup Instructions](#setup-instructions)
- [API Documentation](#api-documentation)
- [Testing Strategy](#testing-strategy)
- [Test Coverage Reports](#test-coverage-reports)
- [Future Improvements](#future-improvements)

---

## 🎯 Overview

**Problem:** Car dealerships need a reliable system to manage inventory, handle concurrent purchases safely, maintain audit trails, and provide admins with inventory management tools.

**Solution:** A full-stack application with:
- **Backend:** Spring Boot REST API with JWT auth, optimistic locking, and transaction logging
- **Frontend:** React SPA with role-based UI and real-time inventory updates
- **Database:** PostgreSQL with Flyway migrations
- **Testing:** 82%+ code coverage with unit, integration, and concurrent safety tests

---

## ✨ Key Features

- ✅ User authentication (register, login, JWT tokens)
- ✅ Full CRUD for vehicles (create, read, update, delete with soft deletes)
- ✅ Advanced search with dynamic filters (make, model, category, price range)
- ✅ **Safe concurrent purchases** (optimistic locking + retry logic, proven safe with integration tests)
- ✅ Inventory restocking with auto-activation
- ✅ Role-based access control (USER vs ADMIN)
- ✅ Complete audit trail (every transaction logged)
- ✅ Admin dashboard with summary stats and bulk operations
- ✅ Responsive UI with error handling and loading states
- ✅ Production-ready (Swagger docs, structured logging, exception handling)

---

## 🏗️ Architecture

### Layered Architecture
### Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| **Optimistic Locking (@Version)** | Prevents overselling during concurrent purchases without blocking |
| **Soft Deletes (DISCONTINUED status)** | Preserves audit trail and allows restoring vehicles |
| **JPA Specifications** | Dynamic search filters without rewriting queries |
| **Transaction Logging** | Complete audit trail for compliance and debugging |
| **Role-Based UI** | Admin actions hidden from regular users client-side + server enforces 403 |
| **React Query** | Automatic caching, retries, background refetches |
| **Testcontainers** | Real PostgreSQL in tests, no data pollution |

---

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.x
- **Language:** Java 21
- **Database:** PostgreSQL 16
- **ORM:** Spring Data JPA + Hibernate
- **Security:** Spring Security + JWT
- **Validation:** Jakarta Bean Validation
- **Testing:** JUnit 5, Mockito, Testcontainers
- **Build:** Maven
- **Migration:** Flyway

### Frontend

- **Framework:** React 19 (JavaScript)
- **Build Tool:** Vite
- **Routing:** React Router DOM
- **State Management:** React Context API + React Hooks (`useState`, `useEffect`)
- **Authentication:** JWT-based Authentication
- **HTTP Client:** Axios
- **Styling:** Custom CSS
- **Testing:** Vitest + React Testing Library

---

## 🚀 Setup Instructions

### Prerequisites
- Java 21+
- Node 18+
- PostgreSQL 15+
- Git

### Backend Setup

```bash
cd backend

# Create databases
createdb car_dealership_dev
createdb car_dealership_test

# Set environment variables
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export JWT_SECRET=your-secret-key

# Run migrations
mvn flyway:migrate

# Run tests
mvn clean test

# Start server
mvn spring-boot:run
# Runs on http://localhost:8080
```

### Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Set environment variables
echo "VITE_API_BASE_URL=http://localhost:8080/api" > .env.local

# Run tests
npm run test

# Start dev server
npm run dev
# Runs on http://localhost:5173
```

### Accessing the Application

- **Frontend:** http://localhost:5173
- **Swagger API Docs:** http://localhost:8081/swagger-ui.html
- **API Base URL:** http://localhost:8081/api

---


## 📡 API Documentation

Full API docs available at `/swagger-ui.html` after starting the backend.

### Authentication

All endpoints except `/auth/**` require JWT token:
### Key Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT |
| GET | `/api/vehicles` | List all vehicles |
| GET | `/api/vehicles/search` | Search with filters |
| POST | `/api/vehicles` | Create vehicle  |
| PUT | `/api/vehicles/{id}` | Update vehicle |
| DELETE | `/api/vehicles/{id}` | Delete vehicle (admin) |
| POST | `/api/vehicles/{id}/purchase` | Purchase vehicle |
| POST | `/api/vehicles/{id}/restock` | Restock vehicle (admin) |

### Example: Purchase Vehicle

```bash
curl -X POST http://localhost:8080/api/vehicles/{vehicleId}/purchase \
  -H "Authorization: Bearer <token>"
```

**Response:**
```json
{
  "purchaseId": "p-uuid-123",
  "vehicleId": "vehicle-uuid",
  "newQuantity": 4,
  "vehicleStatus": "ACTIVE",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---
## 🧪 Testing Strategy

This project follows a layered testing approach inspired by Test-Driven Development (TDD). Every major feature was validated through unit, controller, repository, and integration tests to ensure correctness, reliability, and maintainability.

### Development Approach

For each feature, the development workflow followed the cycle:

1. Write a failing test (Red)
2. Implement the minimum code to make the test pass (Green)
3. Refactor while keeping all tests passing (Refactor)

This approach helped maintain confidence while incrementally building the application.

---

## Test Coverage

### Unit Tests

Unit tests verify the business logic in isolation using mocked dependencies.

**Covered components**

- Authentication Service
- Vehicle Service
- Inventory Service

These tests validate:

- Business rules
- Input validation
- Exception handling
- Inventory operations
- Authentication logic

---

### Controller Tests

Controller endpoints are tested using `@WebMvcTest` and `MockMvc`.

The API layer is validated independently from the service layer, including:

- Authentication endpoints
- Vehicle CRUD endpoints
- Purchase endpoint
- Restock endpoint
- Security configuration
- HTTP status codes
- Request validation
- Response payloads

---

### Repository Tests

Repository tests execute against a real PostgreSQL database using Testcontainers.

These tests validate:

- Custom repository queries
- Search filtering
- Pagination
- Database mappings
- JPA persistence

---

### Integration Tests

The project includes end-to-end integration tests that execute complete business workflows against a real PostgreSQL container.

#### Concurrent Purchase Integration Test

Simulates multiple users attempting to purchase the same vehicle simultaneously.

Verified:

- Concurrent request handling
- Inventory consistency
- Prevention of negative stock
- Transaction integrity

---

#### Optimistic Locking Integration Test

Validates optimistic locking using the `@Version` field.

Verified:

- Concurrent update detection
- Conflict handling
- Prevention of lost updates

---

#### Admin Workflow Integration Test

Tests the complete inventory lifecycle.

Workflow covered:

- Create vehicle
- Restock inventory
- Purchase vehicle
- Verify inventory transaction history
- Soft delete vehicle
- Verify discontinued status

This ensures multiple application layers work together correctly.

---

## Database Testing

Database behaviour is validated using:

- PostgreSQL Testcontainers
- Flyway migrations
- JPA repositories
- Optimistic locking
- Transaction management

Tests execute against an actual PostgreSQL instance rather than an in-memory database, providing production-like behaviour.

---

## Security Testing

Security tests verify:

- JWT authentication
- Protected endpoints
- Unauthorized access (401)
- Validation failures (400)
- Authenticated requests
- Role-based endpoint protection

---

## Code Coverage

Code coverage is generated using **JaCoCo** during the Maven build lifecycle.

Generate the report:

```bash
mvn clean verify
```

Coverage report:

```
target/site/jacoco/index.html
```

---

## Test Stack

- JUnit 5
- Spring Boot Test
- MockMvc
- Mockito
- Testcontainers
- PostgreSQL
- JaCoCo
- Maven Surefire

---

## Testing Philosophy

Rather than testing individual methods only, this project validates the application at multiple levels:

- Business logic through unit tests
- REST APIs through controller tests
- Persistence through repository tests
- Complete business workflows through integration tests
- Concurrent operations through multi-threaded integration tests

This layered testing strategy provides confidence that both individual components and the overall system behave correctly under real-world conditions.


#
### Frontend Coverage: 76% Line Coverage

**Coverage Report Location:** `frontend/coverage/index.html`

**Key Metrics:**
- Statements: **76%**
- Branches: **71%**
- Functions: **78%**
- Lines: **76%**

**Test Components:**
- `SearchBar.test.tsx` - Debouncing, filter composition
- `PurchaseButton.test.tsx` - Button states, purchase flow
- `AdminVehicleTable.test.tsx` - Table rendering, actions
- `AdminDashboard.test.tsx` - Modals, role-based access
- `ProtectedRoute.test.tsx` - Route guarding, redirects
- `VehicleCard.test.tsx` - Card rendering, actions

---

## 🚀 Future Improvements

### Short Term
1. Vehicle images (S3/Cloudinary upload)
2. Advanced sorting (price, popularity)
3. Audit dashboard for transaction logs
4. Email notifications (low stock alerts)
5. User purchase history

### Medium Term
1. Multi-location support (different branches)
2. Supplier management
3. Vehicle reservation system
4. Analytics dashboard (sales trends)
5. Two-factor authentication

### Long Term
1. Microservices architecture (auth, inventory, analytics services)
2. Event-driven with Kafka
3. Redis caching layer
4. Elasticsearch for advanced search
5. Admin custom reports

---

## 🔗 Links

- **GitHub:** https://github.com/Archii1201/car-dealership-inventory
- **Swagger API:** http://localhost:8080/swagger-ui.html (after running backend)
- **JaCoCo Backend Report:** `backend/target/site/jacoco/index.html`
- **Frontend Coverage:** `frontend/coverage/index.html`

---

## 📚 Documentation

- **Database Schema:** See [Database Schema](#database-schema) section above
- **API Examples:** See [API Documentation](#api-documentation) section above
- **Test Strategy:** See [Testing Strategy](#testing-strategy) section above
- **Architecture:** See [Architecture](#architecture) section above
- **AI Usage:** See `docs/ai-usage.md`

---

## 👤 Author

**Archi**  
Backend/Software Engineer | Java | Spring Boot | React  
GitHub: [@Archii1201](https://github.com/Archii1201)

---

**Last Updated:** January 2025
