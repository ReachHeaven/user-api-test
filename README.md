# User API — QA automation

Automated API tests for the AdonisJS "User API" tutorial backend, written in Kotlin with
REST-assured + JUnit 5, reported via Allure, runnable in Docker and GitHub Actions.

[![API tests](https://github.com/ReachHeaven/user-api-test/actions/workflows/tests.yml/badge.svg)](https://github.com/ReachHeaven/user-api-test/actions/workflows/tests.yml)

- **Run in CI:** [Actions → API tests → Run workflow](https://github.com/ReachHeaven/user-api-test/actions/workflows/tests.yml) (button top-right)
- **Allure report:** https://reachheaven.github.io/user-api-test/
- **Test cases:** [docs/test-cases.md](docs/test-cases.md) (RU) — ISTQB-derived, prioritized P0–P3.
- **Found defects:** [docs/defects.md](docs/defects.md) (RU) — bug report linked to case IDs.
- **Manual probes:** [http/user-api.http](http/user-api.http) — fire each request by hand (IntelliJ / VS Code HTTP Client).

## System under test

`POST /user/create` (multipart `username`, `email`, `password`) and `GET /user/get`. The base
URL is supplied at runtime via `BASE_URI` / `-DbaseUri` (a `BASE_URI` repository secret in CI) —
it is not committed to the repo.

## Stack

Kotlin · Gradle (Kotlin DSL) · JUnit 5 · REST-assured · Gson · Allure · Docker · GitHub Actions. JDK 23.

## Architecture

Layered, separation of concerns:

```
src/test/kotlin/userapi/
  config/Config.kt     environment settings (base URI) — single source of truth
  model/NewUser.kt     request payload + unique-data factory
  model/Responses.kt   response POGOs (UserDto, CreateResult) + typed extraction helpers
  client/BaseClient.kt shared request spec (base URI + Allure logging)
  client/UserApi.kt    service layer: create(), list()
  tests/               tests by feature: UserCreateTest, UserGetTest
http/user-api.http     manual probes (IntelliJ / VS Code HTTP Client)
```

## Run

The base URL is required at runtime via `-DbaseUri` or the `BASE_URI` env var (it is not stored
in the repo).

**Local** (needs JDK 23):

```bash
./gradlew test -DbaseUri=http://host:port   # run the suite against the SUT
BASE_URI=http://host:port ./gradlew test    # same, via env var
```

**Docker** (no local JDK needed):

```bash
BASE_URI=http://host:port docker compose up --build tests   # runs the suite, writes ./build/allure-results
docker compose up report                                    # serves Allure at http://localhost:5050
```

## CI

`.github/workflows/tests.yml` runs on **manual dispatch** (one-click in the Actions tab) and on
push to `main`: runs the suite, uploads `allure-results`, builds the Allure report and deploys it
to **GitHub Pages** via the official Pages pipeline. Enable it once: Settings → Pages → Source →
**GitHub Actions**. Set a repository secret **`BASE_URI`** (Settings → Secrets and variables →
Actions) with the SUT base URL — the workflow injects it into the run. The defect-documenting
tests fail by design, so the test step is `continue-on-error` (the pipeline stays green; the
failures are visible in the published report).
