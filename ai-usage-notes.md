# AI Usage Notes

This document is a running log of how AI tools were used during the development of the Car Dealership Inventory System. It is maintained throughout the project to ensure transparency and to support the final "My AI Usage" section in the README.

---

## AI Tools Used

- ChatGPT (GPT-5.5)
- (Add others if used, e.g. GitHub Copilot, Claude, Gemini)

---

# Feature 0 – Project Foundation

**Date:** 10 July 2026

### Goal
Set up the initial development environment and project structure.

### AI Assistance

#### Architecture Planning
- Discussed backend technology choices (Spring Boot vs Node.js/Express).
- Compared PostgreSQL vs MongoDB for the project requirements.
- Designed a scalable layered architecture suitable for future extensions.
- Planned the feature development order using a vertical slice approach.

#### Backend Setup
AI assisted with:
- Spring Initializr dependency selection.
- Maven project configuration.
- PostgreSQL configuration.
- Flyway migration setup.
- `application.yml` configuration.
- Spring Security skeleton.
- Swagger/OpenAPI setup.
- Global exception handling design.

#### Database Design
AI helped design:
- Users table
- Vehicles table
- Inventory Transactions table
- Index strategy
- Optimistic locking preparation (`version` column)

The final schema was reviewed and manually adjusted before implementation.

#### Frontend Setup
AI assisted with:
- React + Vite project initialization.
- Selecting JavaScript instead of TypeScript.
- Project folder organization.
- Library selection (Axios, React Query, React Router).

#### Git Workflow
AI suggested:
- Commit message conventions.
- Feature-based development workflow.
- TDD-friendly commit history.
- Repository organization.

---

## Decisions Made Manually

The following decisions were made independently after evaluating different options:

- Chose Spring Boot over Node.js because of stronger familiarity and faster development.
- Chose PostgreSQL over MongoDB due to the relational nature of inventory data.
- Selected React with JavaScript instead of TypeScript.
- Decided to use Flyway for database migrations.
- Finalized the feature development order.

---

## Verification

All generated suggestions were reviewed before implementation.

No code was copied blindly.

Every configuration, SQL migration, and architecture decision was manually verified and tested.

---

## Reflection

AI significantly accelerated project setup by helping compare technologies, identify best practices, and explain configuration issues. It acted as a technical mentor rather than replacing implementation work. All major architectural decisions, code integration, debugging, and testing remained under my control.

---

# Future Entries

Continue adding entries for each completed feature.

Example format:

---

## Feature 1 – Authentication

**Date:**

### AI Assistance

- Designed JWT authentication flow.
- Reviewed Spring Security configuration.
- Suggested test cases for registration.
- Explained password hashing.
- Reviewed controller structure.

### Manual Work

- Implemented business logic.
- Wrote tests.
- Debugged authentication flow.
- Refactored service layer.

---

## Feature 2 – Vehicle Management

(To be updated...)

---

## Feature 3 – Inventory Operations

(To be updated...)

---

## Feature 4 – Frontend Dashboard

(To be updated...)

---

## Feature 5 – Testing & Deployment

(To be updated...)