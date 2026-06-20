# Rule: Test design (ISTQB-aligned)

How test cases are derived, written, and prioritized in this repo.

## Technique is a design activity, not a case field

ISTQB techniques (EP, BVA, decision tables, state transition) are used to **derive** cases.
They belong in the **test-design section** and the **coverage matrix**, never as a column in
the test case itself. A test case carries only: `ID`, area, title, preconditions, test data,
steps, expected result, priority, severity, automate?.

## Techniques to apply

- **Equivalence Partitioning** — split each input (`username`, `email`, `password`) into
  valid and invalid classes; one representative per class.
- **Boundary Value Analysis** — for sized inputs test `0, 1, min, max, max+1`; distinguish
  **empty** (`""`), **missing** (field absent), and **whitespace-only**.
- **Decision table** — combine valid/invalid `username`×`email`×`password` for `/user/create`;
  collapse impossible/duplicate rules.
- **State / CRUD consistency** — a created user must appear in `GET /user/get`; cover
  duplicate email/username, ordering, empty state.
- **Negative & robustness** — wrong HTTP method, wrong content-type (JSON vs multipart),
  missing/empty body, extra fields, oversized payload, injection strings **as data**.
- **Contract** — status code, `Content-Type`, response schema/shape.
- **Security** — password must never be returned (plaintext or hash) in any response.

Maintain a **coverage matrix** (area/technique → case IDs) so gaps and traceability are visible.

## Expected result = spec, not observed behavior

Each case states the expected result **per the implied spec**, plus a separate
"вероятно фактически / подозрение на дефект" note. When the API contradicts the spec, that is
a **defect** — the case still asserts the correct behavior. Do not document the bug as expected.

## Prioritization model (graded — give it special attention)

`Priority = f(Severity of failure, Likelihood / usage frequency, Business risk)`

| Level | Meaning | Examples |
|-------|---------|----------|
| **P0** | Critical / smoke — core flow or data-integrity/security | happy-path create+read, password not leaked, created user persists |
| **P1** | High — key validation & contract | required-field validation, duplicate email, status/content-type |
| **P2** | Medium — boundaries & robustness | length boundaries, wrong method/content-type, extra fields |
| **P3** | Low — rare edge / cosmetic | unicode names, very large payloads, ordering nuances |

Rules:
- Justify **every P0/P1** with a one-line rationale.
- Automate the **P0/P1** set first; state explicitly what is left manual/documented and why.
- Priority reflects risk, not ease of automation.

## Case quality bar

- **One intent per case**; the title reads as the verification goal.
- **Atomic & independent** — no case depends on another's side effects; generate unique data.
- **Deterministic & repeatable** — same result on re-run.
- Steps are concrete enough to execute manually; test data is explicit.
