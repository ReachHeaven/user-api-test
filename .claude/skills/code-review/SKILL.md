---
name: code-review
description: Review Kotlin API-test code and test-case docs for this repo — checks idiomatic/minimal Kotlin, ISTQB rigor, priority correctness, Allure hygiene, and test independence. Use when reviewing changes to src/test/kotlin or docs/.
---

# Code review (QA automation)

Scoped review for this repository. Apply the rules in `.claude/rules/test-design.md` and
`.claude/rules/kotlin-style.md`. Be specific: cite `file:line`, classify each finding by
severity, and prefer a concrete fix over a vague comment.

## How to run a review

1. Determine scope — review the diff if there is one (`git diff`), else the named files.
2. Read the relevant rule file(s) plus `CLAUDE.md` for the SUT contract.
3. Walk the checklist below; collect findings.
4. Report grouped by **Blocker / Major / Minor / Nit**, each with `file:line` and a fix.
   End with a one-line verdict: approve / approve-with-nits / changes-requested.

## Checklist

### Kotlin idiomaticity & minimalism
- [ ] Expression bodies / single-expression functions where natural; no needless block + `return`.
- [ ] No dead abstraction — no base class/wrapper/helper introduced before two callers need it.
- [ ] Shared setup (base URI, multipart spec, Allure filter) wired **once** in `support/`, reused.
- [ ] Families of inputs are `@ParameterizedTest`, not copy-pasted methods.
- [ ] `val` over `var`; data classes for payloads; no `!!` abuse; no unused imports / dead code.

### ISTQB / test-case quality
- [ ] Technique appears only in the design section / coverage matrix — **not** as a case field.
- [ ] Expected result reflects the **spec**, not observed buggy behavior.
- [ ] Each test has one verification intent; title reads as that intent.
- [ ] Coverage matrix updated; new cases traceable to a technique/area.

### Prioritization
- [ ] Priority matches the model (severity × likelihood × business risk), not automation ease.
- [ ] Every P0/P1 has a rationale; automated set == the P0/P1 set (or deviation is justified).

### Allure hygiene
- [ ] `@Epic/@Feature/@Story/@Severity` present and meaningful.
- [ ] Request/response attached via the `AllureRestAssured` filter, not manual per-test code.
- [ ] Tests that hit a known defect assert correct behavior and link it via `@Issue("BUG-xx")`.

### Reliability
- [ ] Unique data per run (UUID) — tests independent and repeatable.
- [ ] No `Thread.sleep` for timing, no shared mutable state, no order dependence.
- [ ] Assertions are precise (status, schema, values), not just "2xx".

## Severity guide
- **Blocker** — wrong/weakened assertion, flaky/order-dependent test, secret leaked.
- **Major** — missing key coverage, mis-prioritized P0/P1, broken independence.
- **Minor** — non-idiomatic Kotlin, duplication that should be parameterized.
- **Nit** — naming, formatting, comment polish.
