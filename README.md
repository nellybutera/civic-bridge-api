# Civic Bridge Africa — API

Spring Boot REST API for [Civic Bridge Africa](https://github.com/nellybutera/civic-bridge),
implementing the Next.js + Spring Boot + PostgreSQL architecture specified in the project's SRS.

**Interactive API docs (Swagger UI):** https://civic-bridge-api.onrender.com/swagger-ui/index.html
(raw OpenAPI spec at `/v3/api-docs`). Click **Authorize** and paste `Bearer <token>` from a
`/api/auth/login` response to try the role-gated endpoints directly from the browser. This is
the link to use to see the API is live — hitting the bare root URL
(`https://civic-bridge-api.onrender.com/`) returns a 500 by design, since there's no landing
route mapped there.

The backend is a Render free-tier instance that sleeps after ~15 minutes idle. A GitHub Actions
cron in this repo (`.github/workflows/keep-warm.yml`) pings it every 10 minutes to keep this
rare, but the first request after a long gap can still take up to ~2 minutes to wake it up.

## Tech stack

- Java 17, Spring Boot 4 (Web MVC, Spring Data JPA, Bean Validation)
- PostgreSQL
- BCrypt password hashing + JWT (`io.jsonwebtoken`) for auth
- springdoc-openapi for Swagger/OpenAPI docs
- Deployed on Render (Docker)

## Modules / endpoints

| Module | Endpoints |
|---|---|
| Auth | `POST /api/auth/signup`, `POST /api/auth/login` |
| Civic content | `GET /api/content`, `GET /api/content/{id}`, `POST/PUT/DELETE /api/content[/{id}]` (Admin) |
| Quizzes | `GET /api/quizzes`, `GET /api/quizzes/{id}`, `POST /api/quizzes/{id}/results` (any logged-in user), `GET /api/quizzes/results/{userId}` |
| Forum | `GET /api/forum`, `POST /api/forum` (any logged-in user), `DELETE /api/forum/{id}` (Moderator/Admin) |
| Regional tracker | `GET /api/tracker`, `POST/PUT/DELETE /api/tracker[/{id}]` (Admin) |

Role-gated write endpoints require an `Authorization: Bearer <token>` header with a JWT
issued by `/api/auth/login` or `/api/auth/signup`. Requests without a valid token, or from a
role not permitted for that action, are rejected server-side (401/403) regardless of what the
client claims — see `RoleAuthorizationFilter`.

Civic content carries a `sourceUrl` (every article links its verified original source) and
forum posts carry a `topic` (the discussion room a post belongs to — e.g. "Regional Trade",
"Youth Employment", "Elections"; omitted/null posts are treated as "General"). Three quizzes
are seeded at startup, covering parliamentary process, civic rights, and what the AU/EAC do.

## Running locally

**Prerequisites:** Java 17+, Maven (or use the bundled `./mvnw`), and a PostgreSQL database
(local or a free instance from [Render](https://render.com) / [Neon](https://neon.tech)).

1. Clone the repo:
   ```bash
   git clone https://github.com/nellybutera/civic-bridge-api.git
   cd civic-bridge-api
   ```
2. Set the required environment variables (or export them in your shell):
   ```bash
   export DB_HOST=your-db-host
   export DB_PORT=5432
   export DB_NAME=your-db-name
   export DB_USER=your-db-user
   export DB_PASSWORD=your-db-password
   export JWT_SECRET=some-long-random-string-at-least-32-characters
   ```
3. Run it:
   ```bash
   ./mvnw spring-boot:run
   ```
4. The API is available at `http://localhost:8080`. Tables are created automatically
   (`ddl-auto=update`) and seed data (demo users, content, quizzes, forum posts, tracker
   items) is inserted on first startup.

### Demo accounts (seeded automatically)

| Role | Email | Password |
|---|---|---|
| Admin | admin@civicbridge.africa | admin123 |
| Moderator | moderator@civicbridge.africa | mod123 |
| Youth User | youth@civicbridge.africa | youth123 |

## Deploying your own copy (Render)

1. Push this repo to your own GitHub account.
2. On Render: **New → Web Service** → connect the repo → environment **Docker** (uses the
   included `Dockerfile`).
3. Add environment variables: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` (from
   your Postgres instance's **external** connection details) and `JWT_SECRET` (a long random
   string — do not use the fallback dev value baked into `application.properties`).
4. Deploy. Render assigns a public URL once the build finishes and will auto-deploy on every
   push to `main` from then on.

## Relationship to the frontend

The deployed [Next.js frontend](https://github.com/nellybutera/civic-bridge) calls this API
directly over HTTPS for everything — auth, civic content, quizzes and results, the forum, and
the regional tracker. There is no client-side data layer; the only thing the frontend keeps in
the browser is the session JWT itself, in `localStorage`, so a page refresh doesn't log you out.
