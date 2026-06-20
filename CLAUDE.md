# CLAUDE.md

Guidance for Claude Code when working in this repository.

## What this is

A take-home **QA automation** project for the AdonisJS "User API" tutorial backend.
Goal: document & prioritize test cases (ISTQB-aligned), then automate the highest-priority
ones in idiomatic Kotlin with Allure reporting, Docker, and a one-click CI run.

This is a **test** project — there is no production application code here, only the System
Under Test (a remote API) and the tests/docs that exercise it.

## System Under Test (SUT)

Base URL: provided via the `BASE_URI` env var / `-DbaseUri` (a repository secret in CI) — not stored in the repo.

| Method | Path           | Body                                      | Purpose                       |
|--------|----------------|-------------------------------------------|-------------------------------|
| POST   | `/user/create` | multipart **FormData**: `username`, `email`, `password` | Register a user    |
| GET    | `/user/get`    | —                                         | List all registered users     |
| GET    | `/`            | —                                         | `{"greeting":"..."}` (health) |

It is the well-known **Adonis API tutorial** app, which historically ships with **no input
validation**. Treat spec-violating behavior (duplicate emails, malformed email accepted,
empty fields accepted, password leaked in response) as **defects to report**, not as the
baseline to assert as "correct".

## Stack

- **Kotlin** + **Gradle (Kotlin DSL)**, JDK 23
- **JUnit 5 (Jupiter)** — runner, `@ParameterizedTest` for data-driven cases
- **REST-assured** — HTTP client / API assertions (multipart support)
- **Allure** (`allure-junit5`, `allure-rest-assured`) — reporting with auto request/response attachments
- **Hamcrest / AssertJ** — assertions
- **Docker** — containerized run
- **GitHub Actions** — `workflow_dispatch` (one-click) + `push`, publishes Allure to GitHub Pages

## Layout

```
docs/                 Test cases, checklist, prioritization (RU — reviewer-facing)
.claude/rules/        Working rules (test design, Kotlin style)
.claude/skills/       code-review skill
src/test/kotlin/userapi/   Test code (layered)
  config/             environment settings (base URI) — single source of truth
  model/              request payloads + data factories
  client/             BaseClient (shared spec + Allure) and UserApi service
  tests/              tests by feature (UserCreate, UserGet, ...)
.github/workflows/    CI
Dockerfile, docker-compose.yml
```

## Commands

```bash
./gradlew test                 # run the suite
./gradlew test -DbaseUri=...   # override SUT base URL
./gradlew allureReport         # generate report -> build/reports/allure-report/allureReport
docker compose up --build tests
```

## Conventions

- **Language:** docs under `docs/` are **Russian** (graded deliverable). Everything else —
  code, identifiers, CLAUDE.md, rules, skill, README — **English**.
- Follow `.claude/rules/test-design.md` (ISTQB) and `.claude/rules/kotlin-style.md` when
  writing cases and code.
- **Defects:** tests assert correct/spec behavior; a test that hits an open defect fails and is
  linked to the bug report via Allure `@Issue("BUG-xx")`. Allure surfaces it — no special tag or
  task. Never weaken an assertion to "match the bug".
- Tests must be **independent and repeatable** — generate unique `username`/`email` per run.
- Stack is fixed (above) — don't introduce new frameworks without asking.
