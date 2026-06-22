# Rule: Kotlin test style (idiomatic, minimal, lean)

The bar: a reviewer should read each test top-to-bottom and see exactly what is verified, with
zero ceremony. Minimalism is a hard requirement of this assignment.

## Idiomatic Kotlin

- Prefer **expression bodies** and single-expression functions over block bodies with `return`.
- Use `val` by default; data classes for payloads; named args for call-site clarity.
- String templates over concatenation; `when` over if-else chains.
- No Java-isms: no getters/setters boilerplate, no `StringBuilder` for simple joins, no
  nullable abuse (`!!` only when truly invariant).
- Top-level helper functions / objects instead of static-util classes.

## Minimal & lean

- **No dead abstraction.** Don't add a base class, wrapper, or "framework" layer until two
  real cases need it. YAGNI.
- **Wire shared setup once** — base URI in `config/Config.kt`, the multipart `RequestSpecification`
  and Allure filter in `client/BaseClient.kt`, reused by all tests.
- **Data-driven over copy-paste** — use `@ParameterizedTest` + `@MethodSource`/`@CsvSource`
  for families of inputs (validation, boundaries) instead of near-duplicate methods.
- Keep helpers tiny and obvious; if a helper needs a comment to explain *what*, inline it.
- No unused imports, no commented-out code, no speculative config.

## REST-assured

- Build `RequestSpecification`/`ResponseSpecification` once; tests express only what differs.
- Use the fluent `given/when/then` chain; extract with `.path()`/`.extract()` for assertions
  that need values.
- Attach request/response to Allure via the `AllureRestAssured` filter set on the spec — not
  manually per test.

## Test structure

- One concern per test class (`UserCreateTest`, `UserGetTest`, ...).
- Method names in **backtick sentence** form describing the verification, e.g.
  `` fun `create with valid data returns created user`() ``.
- **Arrange / Act / Assert** visually separable; one verification intent per test.
- Annotate with Allure `@Epic/@Feature/@Severity`; link a test that hits a known defect via
  `@Issue("BUG-xx")`.
- Generate unique data per run (`UUID`) so tests stay independent and repeatable.

## Don't

- Don't catch-and-ignore, don't `Thread.sleep` for timing, don't share mutable state between
  tests, don't assert on the bug ("match observed") — assert the spec.
