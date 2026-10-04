# Task Manager

A full-stack task management application demonstrating role-based and ownership-based
authorization, built with React, Spring Boot, and Supabase.

## Overview

Two roles — **ADMIN** and **USER** — share one task board. ADMIN can create, assign,
edit, and delete any task. USER can view and update the status of only the tasks
assigned to them. Authorization is enforced entirely server-side; the frontend's
role-based UI is a convenience layer, not the security boundary.

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React, TypeScript, React Router, Axios, Vite |
| Backend | Java, Spring Boot, Spring Security, Spring Data JPA |
| Database & Auth | Supabase (PostgreSQL, Auth with JWT) |
| Auth verification | Spring Security OAuth2 Resource Server (JWKS, ES256) |

## Features

- Email/password registration and login via Supabase Auth
- Stateless JWT authentication, verified server-side against Supabase's public keys
- Role-based access control (ADMIN / USER) enforced via `@PreAuthorize`
- Ownership-based access control (a USER may only update tasks assigned to them)
- Full CRUD with request validation and centralized exception handling
- Task board UI grouped by status, with loading and error states throughout

## Architecture
React (Vite, :5173)
│ Bearer <Supabase JWT>
▼
Spring Boot REST API (:8080)
│ verifies JWT signature via Supabase JWKS endpoint
│ extracts user id + role from token claims
▼
PostgreSQL (Supabase-managed)


Supabase issues and signs JWTs on login. The backend never calls Supabase to validate
a token — it independently verifies the signature using Supabase's published public
keys, extracts the caller's id and role from the token's claims, and enforces access
rules in the service layer before touching the database.

## API Endpoints

| Method | Endpoint | Purpose | Access |
|---|---|---|---|
| POST | `/api/tasks` | Create (and optionally assign) a task | ADMIN |
| GET | `/api/tasks` | Get all tasks | ADMIN |
| GET | `/api/tasks/my` | Get the caller's own tasks | ADMIN, USER |
| GET | `/api/tasks/{id}` | Get one task | ADMIN, owner |
| PUT | `/api/tasks/{id}` | Update a task (USER: status only, own task) | ADMIN, owner |
| DELETE | `/api/tasks/{id}` | Delete a task | ADMIN |

## Getting Started

### Prerequisites

- Node.js 20+
- Java 17 (JDK)
- Maven 3.9+
- A free [Supabase](https://supabase.com) account

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/task-manager.git
cd task-manager
```

### 2. Set up Supabase

1. Create a new Supabase project.
2. In the SQL Editor, run:
```sql
   create table public.tasks (
     id uuid primary key default gen_random_uuid(),
     title text not null,
     description text,
     status text not null default 'TODO' check (status in ('TODO', 'IN_PROGRESS', 'DONE')),
     owner_id uuid not null references auth.users(id) on delete cascade,
     created_at timestamptz not null default now(),
     updated_at timestamptz not null default now()
   );
```
3. Under **Authentication → Providers → Email**, disable "Confirm email" for easier local testing.
4. Note down (from **Project Settings → API**): Project URL, anon public key, and the JWKS URL
   (`https://<project-ref>.supabase.co/auth/v1/.well-known/jwks.json`).

### 3. Backend setup

```bash
cd backend
```
Set these environment variables (e.g. in your IDE's run configuration):
DB_HOST=<your-supabase-pooler-host>
DB_PORT=5432
DB_NAME=postgres
DB_USER=<your-supabase-db-user>
DB_PASSWORD=<your-supabase-db-password>

Update `src/main/resources/application.properties`'s `jwk-set-uri` with your project ref, then run:
```bash
./mvnw spring-boot:run
```
Backend runs at `http://localhost:8080`.

### 4. Frontend setup

```bash
cd frontend
cp .env.example .env
```
Fill in `.env` with your Supabase URL and anon key, then:
```bash
npm install
npm run dev
```
Frontend runs at `http://localhost:5173`.

### 5. Create test accounts

Register two accounts via the app's `/register` page — one with role `ADMIN`, one with role `USER` — to explore both experiences.

## Project Structure
task-manager/
├── frontend/ React + TypeScript client
├── backend/ Spring Boot REST API
└── README.md


## Known Limitations

- Registration currently allows self-service role selection (ADMIN/USER) for
  simplicity. In production, new accounts would always default to USER, with
  promotion to ADMIN handled by an existing admin through a protected endpoint.

## License

MIT
