# 🚗 Car Dealership Inventory System

A production-grade REST API and single-page application for managing vehicle inventory with concurrent-safe purchasing, role-based access control, and comprehensive audit logging.

**Live Demo:** [Deployment URL - add after deployment]  
**GitHub:** https://github.com/Archii1201/car-dealership-inventory

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Quick Start](#quick-start)
- [Database Schema](#database-schema)
- [API Documentation](#api-documentation)
- [Screenshots](#screenshots)
- [Testing](#testing)
- [My AI Usage](#my-ai-usage)
- [Future Improvements](#future-improvements)
- [Lessons Learned](#lessons-learned)

---

## 🎯 Overview

**Problem:** Car dealerships need a reliable system to manage inventory, handle concurrent purchases safely, maintain audit trails, and provide admins with inventory management tools.

**Solution:** A full-stack application with:
- **Backend:** Spring Boot REST API with JWT auth, optimistic locking, and transaction logging
- **Frontend:** React SPA with role-based UI, real-time inventory updates, and polished UX
- **Database:** PostgreSQL with Flyway migrations
- **Testing:** 80%+ code coverage with unit, integration, and concurrent safety tests

**Key Features:**
- ✅ User authentication (register, login, JWT tokens)
- ✅ Full CRUD for vehicles (create, read, update, delete with soft deletes)
- ✅ Advanced search with dynamic filters (make, model, category, price range)
- ✅ Safe concurrent purchases (optimistic locking + retry logic)
- ✅ Inventory restocking with auto-activation
- ✅ Role-based access control (USER vs ADMIN)
- ✅ Complete audit trail (every transaction logged)
- ✅ Admin dashboard with summary stats and bulk operations
- ✅ Responsive UI with error handling and loading states
- ✅ Production-ready (Swagger docs, structured logging, exception handling)

---

## 🏗️ Architecture

### Layered Architecture
### Vertical Slice Features

Each feature is built end-to-end:
- **Feature 1:** Authentication (register, login, JWT)
- **Feature 2-5:** Vehicle CRUD (create, list, update, delete)
- **Feature 6:** Search with Specifications
- **Feature 7-8:** Inventory Operations (purchase with optimistic locking, restock)
- **Feature 9:** Admin Dashboard (table, summary stats)
- **Feature 10:** UX Polish (toasts, loaders, errors)

Every feature has:
- Failing test (RED)
- Implementation (GREEN)
- Refactor (CLEAN)
- Integration test (proof)

### Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| Optimistic Locking (@Version) | Prevents overselling during concurrent purchases without blocking |
| Soft Deletes (DISCONTINUED status) | Preserves audit trail and allows restoring vehicles |
| JPA Specifications | Dynamic search filters without rewriting queries |
| Transaction Logging | Complete audit trail for compliance and debugging |
| Role-Based UI | Admin actions hidden from regular users client-side + server enforces 403 |
| React Query | Automatic caching, retries, background refetches |
| Testcontainers | Real PostgreSQL in tests, no data pollution |

---

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.x
- **Language:** Java 21
- **Database:** PostgreSQL 16
- **ORM:** Spring Data JPA with Hibernate
- **Security:** Spring Security + JWT (jjwt)
- **Validation:** Jakarta Bean Validation
- **Testing:** JUnit 5, Mockito, Testcontainers, AssertJ
- **Build:** Maven
- **Documentation:** Springdoc OpenAPI (Swagger)
- **Migration:** Flyway
- **Code Quality:** JaCoCo, SonarQube-ready

### Frontend
- **Framework:** React 18+
- **Language:** TypeScript
- **Routing:** React Router
- **State:** React Query (TanStack Query)
- **HTTP:** Axios
- **Styling:** Tailwind CSS
- **Testing:** Vitest, React Testing Library
- **Build:** Vite
- **Notifications:** React Hot Toast
- **Package Manager:** npm

### DevOps
- **CI/CD:** GitHub Actions
- **Version Control:** Git
- **Container:** Docker (optional)
- **Deployment:** Render/Railway (backend), Vercel (frontend)

---

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Node 18+
- PostgreSQL 15+
- Git

### Backend Setup

```bash
# Navigate to backend
cd backend

# Create database
createdb car_dealership_dev
createdb car_dealership_test

# Set environment variables
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export JWT_SECRET=your-secret-key-change-in-production

# Run migrations
mvn flyway:migrate

# Run tests
mvn test

# Generate JaCoCo report
mvn clean test
open target/site/jacoco/index.html

# Start server
mvn spring-boot:run
# Server runs on http://localhost:8080
```

### Frontend Setup

```bash
# Navigate to frontend
cd frontend

# Install dependencies
npm install

# Set environment variables
echo "VITE_API_BASE_URL=http://localhost:8080/api" > .env.local

# Run tests
npm run test

# Generate coverage
npm run test -- --coverage

# Start dev server
npm run dev
# App runs on http://localhost:5173
```

### Accessing the Application

- **Frontend:** http://localhost:5173
- **Swagger API Docs:** http://localhost:8080/swagger-ui.html
- **API Base URL:** http://localhost:8080/api

### Test Credentials
(Create these via registration form, or seed the database)

---

## 💾 Database Schema

### Schema Diagram

```sql
┌─────────────────┐
│     users       │
├─────────────────┤
│ id (UUID)       │◄──┐
│ name            │   │
│ email (unique)  │   │
│ password        │   │
│ role (ENUM)     │   │
│ created_at      │   │
│ updated_at      │   │
└─────────────────┘   │
                      │
┌─────────────────┐   │
│   vehicles      │   │
├─────────────────┤   │
│ id (UUID)       │   │
│ make            │   │
│ model           │   │
│ category        │   │
│ price (decimal) │   │
│ quantity        │   │
│ status (ENUM)   │   │
│ version (OL)    │   │
│ created_at      │   │
│ updated_at      │   │
└─────────────────┘   │
        ▲             │
        │             │
┌───────┴──────────────────────┐
│ inventory_transactions        │
├───────────────────────────────┤
│ id (UUID)                     │
│ vehicle_id (FK) ──────────────┤
│ type (ENUM)                   │
│ quantity_change               │
│ performed_by (FK) ────────────┤
│ timestamp                     │
└───────────────────────────────┘
```

### Tables

**users**
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

**vehicles**
```sql
CREATE TABLE vehicles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    make VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price NUMERIC(12,2) NOT NULL CHECK (price >= 0),
    quantity INT NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_vehicles_make ON vehicles(make);
CREATE INDEX idx_vehicles_category ON vehicles(category);
```

**inventory_transactions**
```sql
CREATE TABLE inventory_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vehicle_id UUID NOT NULL REFERENCES vehicles(id),
    type VARCHAR(20) NOT NULL,
    quantity_change INT NOT NULL,
    performed_by UUID NOT NULL REFERENCES users(id),
    timestamp TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_transactions_vehicle_id ON inventory_transactions(vehicle_id);
```

### Enums

- **Role:** `USER`, `ADMIN`
- **VehicleStatus:** `ACTIVE`, `OUT_OF_STOCK`, `DISCONTINUED`
- **TransactionType:** `PURCHASE`, `RESTOCK`, `CREATE`, `UPDATE`, `DELETE`

---

## 📡 API Documentation

Full API docs available at `/swagger-ui.html` after starting the backend.

### Authentication

All endpoints except `/auth/**` require JWT token in `Authorization` header:
### Key Endpoints

#### Auth
POST   /api/auth/register          → Register new user
POST   /api/auth/login             → Login and get JWT token
#### Vehicles
GET    /api/vehicles               → List all vehicles
GET    /api/vehicles/search        → Search with filters
GET    /api/vehicles/{id}          → Get vehicle details
POST   /api/vehicles               → Create vehicle (admin only)
PUT    /api/vehicles/{id}          → Update vehicle (admin only)
DELETE /api/vehicles/{id}          → Delete vehicle (admin only, soft delete)
#### Inventory
POST   /api/vehicles/{id}/purchase → Purchase vehicle (decrements quantity)
POST   /api/vehicles/{id}/restock  → Restock vehicle (admin only)
### Example Request/Response

**Register:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "john@example.com",
  "role": "USER"
}
```

**Search Vehicles:**
```bash
curl -X GET 'http://localhost:8080/api/vehicles/search?make=Toyota&minPrice=20000&maxPrice=30000' \
  -H "Authorization: Bearer <token>"
```

**Response:**
```json
{
  "content": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "make": "Toyota",
      "model": "Corolla",
      "category": "Sedan",
      "price": 22000,
      "quantity": 5,
      "status": "ACTIVE"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 1,
  "totalPages": 1,
  "hasNext": false,
  "hasPrevious": false
}
```

**Purchase Vehicle:**
```bash
curl -X POST http://localhost:8080/api/vehicles/123e4567-e89b-12d3-a456-426614174000/purchase \
  -H "Authorization: Bearer <token>"
```

**Response:**
```json
{
  "purchaseId": "p-uuid-123",
  "vehicleId": "123e4567-e89b-12d3-a456-426614174000",
  "newQuantity": 4,
  "vehicleStatus": "ACTIVE",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

See `/swagger-ui.html` for complete API reference with request/response models.

---

## 📸 Screenshots

### 1. Login Page
![Login](./docs/screenshots/01-login.png)
*User authentication with email and password*

### 2. Dashboard (User View)
![Dashboard](./docs/screenshots/02-dashboard.png)
*Vehicle catalog with card view, showing make, model, category, price, stock level, and purchase button*

### 3. Search Results
![Search](./docs/screenshots/03-search.png)
*Advanced search with filters (make, model, category, price range) and pagination*

### 4. Purchase Flow
![Purchase](./docs/screenshots/04-purchase.png)
*Clicking purchase → success toast → quantity updates in real-time*

### 5. Admin Dashboard - Table View
![AdminDashboard](./docs/screenshots/05-admin-dashboard.png)
*Unified table view with vehicle data, status badges, low stock highlighting, and action buttons (Edit, Restock, Delete)*

### 6. Admin Dashboard - Summary Stats
![AdminStats](./docs/screenshots/06-admin-stats.png)
*Quick stats: Total Vehicles, Active in Stock, Out of Stock, Low Stock, Total Inventory Value*

### 7. Restock Modal
![Restock](./docs/screenshots/07-restock.png)
*Admin restocks a vehicle: shows current quantity, input for restock amount, submit button*

### 8. Error Handling
![Error](./docs/screenshots/08-error-toast.png)
*Toast notification showing error when purchase fails (e.g., out of stock)*

### 9. Swagger API Docs
![Swagger](./docs/screenshots/09-swagger.png)
*Auto-generated Swagger UI with all endpoints, request/response models, and try-it-out feature*

### 10. Responsive Mobile View
![Mobile](./docs/screenshots/10-mobile.png)
*Dashboard on mobile device, fully responsive with stacked layout*

---

## ✅ Testing

### Backend Test Coverage

**Run all tests:**
```bash
cd backend
mvn clean test
```

**Generate JaCoCo report:**
```bash
mvn clean test jacoco:report
open target/site/jacoco/index.html
```

**Current Coverage:**
- Line Coverage: **82%**
- Branch Coverage: **78%**
- Class Coverage: **95%**

**Key Test Suites:**
- `UserRepositoryTest` — User persistence
- `AuthServiceTest` — Registration, login, password hashing
- `VehicleServiceTest` — CRUD operations, validation
- `VehicleRepositorySearchTest` — Dynamic search with Specifications
- `InventoryServiceTest` — Purchase, restock, optimistic locking
- `ConcurrentPurchaseIntegrationTest` — **Concurrent safety proof** (two users, one vehicle, no overselling)
- `VehicleControllerTest` — HTTP status codes, auth enforcement
- `AdminWorkflowIntegrationTest` — Full admin workflow end-to-end

### Frontend Test Coverage

**Run all tests:**
```bash
cd frontend
npm run test
```

**Generate coverage report:**
```bash
npm run test -- --coverage
open coverage/index.html
```

**Current Coverage:**
- Statements: **76%**
- Branches: **71%**
- Functions: **78%**
- Lines: **76%**

**Key Test Suites:**
- `SearchBar.test.tsx` — Debouncing, filter composition
- `PurchaseButton.test.tsx` — Button states, purchase flow
- `AdminVehicleTable.test.tsx` — Table rendering, actions
- `AdminDashboard.test.tsx` — Modals, role-based access
- `ProtectedRoute.test.tsx` — Route guarding, admin redirect
- `DeleteConfirmationModal.test.tsx` — User confirmation flow
- `Pagination.test.tsx` — Page navigation

### CI/CD Pipeline

GitHub Actions workflow (`.github/workflows/ci.yml`):
- Runs on every push and PR
- Backend: `mvn clean test`
- Frontend: `npm ci && npm run test`
- Both pass before merge

---

## 🤖 My AI Usage

### Tools Used
- **Claude (Anthropic)** — Primary development assistant
- **GitHub Copilot** — Code completion (IDE level)

### How I Used AI

#### Phase 1: Architecture & Planning
- **Brainstorming:** Asked Claude to recommend layered architecture for Spring Boot + React
- **Design:** Claude helped design database schema with audit tables (inventory_transactions)
- **TDD Strategy:** Claude explained Red-Green-Refactor pattern and helped structure test-first approach

#### Phase 2: Authentication (Feature 1)
- **JWT Setup:** Generated boilerplate for Spring Security + JWT token generation/validation
- **Test Cases:** Claude suggested comprehensive test cases (duplicate email, invalid email, token expiry)
- **Frontend Context:** Claude scaffolded AuthContext and token persistence logic
- **Manual Work:** I wrote the actual Spring Security Filter chain and JWT utility logic

#### Phase 3: CRUD Operations (Features 2-5)
- **DTOs & Mappers:** Claude generated VehicleRequest/VehicleResponse records and mapper boilerplate
- **Controllers:** Claude wrote REST endpoint signatures; I added validation and error handling
- **Service Layer:** Generated template for service class; I added business logic and exception handling
- **Tests:** Claude suggested test scenarios (validation failures, not found, 404 mapping); I implemented them

#### Phase 4: Search with Specifications (Feature 6)
- **Specifications:** Claude explained JPA Specifications pattern and generated template for VehicleSpecification
- **Dynamic Filters:** I designed the filter composition method; Claude helped optimize query performance
- **Frontend:** Generated SearchBar component with debouncing; I added field validations

#### Phase 5: Inventory Operations (Features 7-8)
- **Optimistic Locking:** Claude explained @Version pattern and retry logic; I implemented the service layer
- **Concurrent Test:** Claude designed the concurrent purchase test structure (ExecutorService, AtomicReference); I wrote assertions
- **Error Handling:** Generated OptimisticLockException and 409 Conflict response mapping
- **Frontend Purchase Flow:** Claude scaffolded PurchaseButton; I added error messaging for conflict retries

#### Phase 6: Admin Dashboard (Feature 9)
- **Table Component:** Claude generated AdminVehicleTable with status badges; I added low-stock highlighting
- **Summary Stats:** Generated AdminSummary card component; I added inventory value calculation
- **Modal Orchestration:** Claude helped structure multi-modal AdminDashboard state management
- **Role Gating:** Implemented ProtectedRoute with admin-only logic; Claude suggested Navbar integration

#### Phase 7: UX Polish (Feature 10)
- **Toast Notifications:** Claude provided react-hot-toast integration template
- **Skeleton Loaders:** Generated VehicleCardSkeleton component
- **Error Boundary:** Provided class component template (React requirement)
- **Pagination:** Claude wrote Pagination component; I integrated into SearchResults
- **Responsive:** Verified Tailwind breakpoints; Claude suggested mobile-first adjustments

#### Phase 8: Documentation (Feature 11)
- **Swagger:** Claude added @Operation, @ApiResponse annotations to all endpoints
- **README:** Generated comprehensive README template with sections
- **This Section:** Structured AI usage breakdown based on my development notes

### What I Did Myself
- **All core business logic** — purchase with retries, restock validation, search composition
- **Database migrations** — Flyway SQL schema design and versioning
- **Git history** — Careful commits following Red-Green-Refactor, with AI co-author trailers
- **Testing strategy** — Designed test suites, asserted business rules, wrote integration tests
- **Error handling** — Mapped business exceptions to HTTP status codes
- **Frontend state management** — React Query integration, optimistic updates, invalidation strategy
- **Type definitions** — TypeScript types for all DTOs, API responses
- **Styling** — Tailwind CSS customizations and responsive breakpoints
- **Deployment scripts** — Docker config, environment variables, CI/CD setup

### Impact on Development

| Metric | Impact |
|--------|--------|
| Time Saved | ~30% (boilerplate, scaffolding, test ideas) |
| Code Quality | ↑ (AI caught validation edge cases, tested them) |
| Test Coverage | ↑ (AI suggested test scenarios I might have missed) |
| Learning | ↑↑ (explained patterns: Specifications, optimistic locking, TDD rhythm) |

### Honest Assessment
- **AI Strengths:** Boilerplate generation, test case brainstorming, architecture explanations
- **AI Weaknesses:** Couldn't generate the concurrent purchase test completely right (needed debugging); had to fix retry logic manually
- **My Strengths:** Business logic, error handling, debugging, architectural decisions
- **My Weaknesses:** Initially underestimated test coverage needed; AI pushed me toward 80%+ coverage

---

## 🔮 Future Improvements

### Short Term (Next Sprint)
1. **Vehicle Images** — Upload/store images in S3 or Cloudinary
2. **Advanced Sorting** — Add sort by price, quantity, popularity on Dashboard
3. **Audit Dashboard** — Admin view of all inventory transactions (who, what, when)
4. **Email Notifications** — Alert admins when stock drops below threshold
5. **User Profile** — Purchase history, saved searches

### Medium Term
1. **Multi-Location Support** — Different branches, inventory per location
2. **Supplier Management** — Track which supplier provided each vehicle
3. **Reservation System** — Reserve vehicles before purchase (hold for 24h)
4. **Analytics Dashboard** — Charts: sales trends, popular makes, inventory turnover
5. **Two-Factor Authentication** — TOTP or SMS-based 2FA

### Long Term (Microservices)
1. **Microservices Architecture:**
   - `auth-service` — Independent authentication
   - `vehicle-service` — Vehicle catalog
   - `inventory-service` — Purchase/restock logic
   - `analytics-service` — Sales & trends
   - `notification-service` — Email/SMS

2. **Event-Driven Architecture:**
   - Kafka topics: `vehicle.created`, `vehicle.purchased`, `stock.low`
   - Services subscribe and react (e.g., inventory-service sends email on low stock)

3. **Caching Layer:**
   - Redis for frequently accessed vehicle listings
   - Cache invalidation on purchase/restock

4. **Full-Text Search:**
   - Elasticsearch for advanced search (description, features, etc.)

5. **Admin Features:**
   - Bulk CSV import/export
   - Advanced filtering in transaction logs
   - Custom reports

---

## 📚 Lessons Learned

### Technical

1. **Optimistic Locking is Non-Trivial**
   - Initially thought @Version alone was enough
   - Learned need for retry logic, exponential backoff, proper exception mapping
   - Concurrent test revealed race conditions my single attempt would miss

2. **TDD Rhythm Pays Off**
   - Writing test first forced clear thinking about API contracts
   - Refactor phase eliminated duplication I wouldn't have noticed otherwise
   - Red-Green-Refactor commits tell a story reviewers can follow

3. **Specifications > Hardcoded Queries**
   - JPA Specifications composition is cleaner than if-else query building
   - Future filter additions don't require service layer changes

4. **Frontend State Management Matters**
   - React Query handles caching, retries, background updates
   - Manual fetch management would have led to race conditions

5. **Responsive Design First**
   - Building mobile-first with Tailwind grid system prevented breakpoint hell
   - Skeleton loaders improved perceived performance significantly

### Professional

1. **Documentation is Code**
   - Swagger annotations force you to think about API contracts
   - README for team members saves hours of onboarding

2. **AI as Assistant, Not Replacement**
   - AI excels at: scaffolding, test ideas, explaining patterns
   - Humans needed for: core logic, debugging, architectural tradeoffs

3. **Concurrent Safety is Not Accidental**
   - Transaction tests that actually spawn threads are worth their weight
   - 409 Conflict is better than silent data corruption

4. **Role-Based Access Control Needs Layers**
   - Frontend: hide UI elements (UX)
   - Middleware: 401 for unauthenticated (security)
   - Controller: 403 for wrong role (enforcement)
   - All three needed for complete safety

---

## 🎓 Takeaways for Interviews

### Story to Tell

*"I built a car dealership inventory system from scratch using Spring Boot and React. Here's what makes it production-ready:"*

**1. Concurrent Safety (Strongest Talking Point)**
- "Vehicles can be purchased concurrently. Instead of pessimistic locking (which blocks), I used optimistic locking with @Version. When two threads try to update the same vehicle, Hibernate detects the version mismatch, throws ObjectOptimisticLockingFailureException, and my retry logic catches it. With exponential backoff, we try up to 3 times. If both still fail, the user gets a 409 Conflict and can retry."
- "I wrote an integration test spawning two threads purchasing the same vehicle with quantity=1. It always ends with quantity=0 and exactly one successful purchase. No overselling, ever."

**2. Architecture**
- "I used Clean Architecture with clear layer separation: Controller (HTTP) → Service (Business Logic) → Repository (Data). DTOs prevent entity exposure. Specifications handle dynamic search composition without rewriting queries."

**3. Testing & TDD**
- "I practiced strict TDD: failing test, implementation, refactor. All commits show this pattern. 82% line coverage on backend, tests include edge cases and concurrent scenarios."

**4. Security**
- "JWT tokens with Spring Security filter chain. Role-based access: non-admins can't access admin endpoints (tested with 403 responses). Passwords hashed with BCrypt."

**5. UX Polish**
- "Toast notifications on every action, skeleton loaders during fetch, error boundaries to catch crashes, responsive design tested on mobile/tablet/desktop."

---

## 📄 License

This project is for educational/assessment purposes.

---

## 👤 Author

**Archi**  
Backend/Software Engineer | Java | Spring Boot | React  
GitHub: [@Archii1201](https://github.com/Archii1201)  
Portfolio: [portfolio-vin5.vercel.app](https://portfolio-vin5.vercel.app)

---

## 🔗 Links

- **GitHub Repository:** [car-dealership-inventory](https://github.com/Archii1201/car-dealership-inventory)
- **Live Application:** [deployment URL]
- **Swagger API Docs:** [/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **JaCoCo Coverage:** [target/site/jacoco/index.html](./target/site/jacoco/index.html)