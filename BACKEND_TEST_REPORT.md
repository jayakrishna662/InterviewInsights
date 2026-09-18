# BACKEND TEST REPORT

**Project:** InterviewInsights backend  
**Test date:** 2026-09-18  
**Scope:** Existing production-oriented configuration. No production code, schema, security configuration, or database data was changed. No Gemini request was sent.

## 1. Endpoint inventory

| Area | Endpoint | Access configured in `WebSecurityConfig` |
|---|---|---|
| Auth | `POST /api/auth/register`, `POST /api/auth/login` | Public |
| Auth | `GET /api/auth/me` | Authenticated |
| Companies | `GET /api/companies`, `GET /api/companies/active` | Public |
| Companies | `POST /api/companies`, `PUT /api/companies/{id}`, `PATCH /api/companies/{id}/toggle-status` | ADMIN |
| Batches | `GET /api/batches` | Public |
| Batches | `POST /api/batches`, `PUT /api/batches/{id}` | ADMIN |
| Departments | `GET /api/departments` | Public |
| Departments | `POST /api/departments` | ADMIN |
| Users | `GET /api/users` | ADMIN |
| Experiences | `POST /api/interview-experiences`, `GET /api/interview-experiences`, `GET /api/interview-experiences/{id}`, `GET /api/interview-experiences/company/{companyId}` | Authenticated |
| Questions | `GET /api/questions`, `GET /api/questions/category/{category}`, `GET /api/questions/company/{companyId}/frequency` | Authenticated |
| Questions | `POST /api/questions` | ADMIN |
| Experience/question links | `POST /api/experience-questions/link` | ADMIN |
| Experience/question links | `GET /api/experience-questions/experience/{experienceId}` | Authenticated |
| Admin | `GET /api/admin/stats`, `GET /api/admin/experiences/ai-status`, `POST /api/admin/experiences/{experienceId}/ai/retry` | ADMIN |

### Services and persistence inventory

- Services: `AuthService`, `UserService`, `CompanyService`, `BatchService`, `InterviewExperienceService`, `QuestionService`, `ExperienceQuestionService`, `AdminService`, `AiRetryService`, `AsyncQuestionProcessingService`, `GeminiService`, and `EmbeddingService`.
- Repositories: user, company, batch, department, interview experience, question, and experience-question repositories.
- Relationships: `User -> Department` and `User -> Batch` are many-to-one; `InterviewExperience -> User` and `InterviewExperience -> Company` are many-to-one; `ExperienceQuestion -> InterviewExperience` and `ExperienceQuestion -> Question` are many-to-one.
- Declared uniqueness: user email; company name; batch name; department name; `(question_text, category)`; `(experience_id, question_id)`.
- DTO validation is present only on registration and experience-create DTOs. Company, batch, department, question, login, and link DTOs have no bean-validation annotations; several controllers also omit `@Valid`.
- AI pipeline: an experience starts `PENDING`; the async worker conditionally marks it `PROCESSING`, calls Gemini, parses category arrays, creates/reuses questions and links, then marks `COMPLETED`; exceptions mark it `FAILED`. Retry is ADMIN-only and accepts only `FAILED` records.

## 2. Executed test cases

| Test | Operation | Expected | Actual | Result | Evidence |
|---|---|---|---|---|---|
| Maven build | `mvnw.cmd test` | Compile and tests pass | Build passed; 1 context-load test passed | PASS | Maven: `BUILD SUCCESS`; 1/1 tests passed |
| Startup and DB | `@SpringBootTest` | App and PostgreSQL connect | Spring context started; Hikari connected to PostgreSQL 17.6 | PASS | Build log: pool started and context started |
| Live startup | Local jar, port 8080/8081 | HTTP server accepts requests | App served requests; second instance used 8081 to avoid disrupting first | PASS | `GET /api/companies` returned 200 |
| Public companies | `GET /api/companies` | 200 | 200 with persisted companies | PASS | Live response |
| Active companies | `GET /api/companies/active` | 200 | 200 with active companies | PASS | Live response |
| Public batches | `GET /api/batches` | 200 | 200 | PASS | Live response |
| Public departments | `GET /api/departments` | 200 | 200 | PASS | Live response |
| Anonymous protected reads | GET questions, experiences, users, admin stats | Rejected | 403 for each | PASS (rejected); see security finding on status | Live responses |
| Anonymous protected writes | POST company, batch, department, question, experience | Rejected before mutation | 403 for each | PASS | Live responses; no records created |
| ADMIN authorization | ADMIN-token GET admin stats and users | 200 | 200 for both | PASS | Temporary in-memory signed token; tokens not retained |
| USER authorization | USER-token GET admin stats | Forbidden | 403 | PASS | Live response |
| USER read authorization | USER-token GET questions and experiences | 200 | 200 for both | PASS | Live responses |
| Invalid enum | USER-token `GET /api/questions/category/NOT_A_CATEGORY` | 400 | 400 | PASS | Live response |
| Invalid roll number | `POST /api/auth/login` | Controlled 401/400 response | 403 empty response | FAIL | Controller was reached; uncaught runtime error was converted to 403 |
| Missing login fields | `POST /api/auth/login` with `{}` | Controlled 400 | 403 empty response | FAIL | Live response |
| Malformed login JSON | `POST /api/auth/login` malformed body | 400 | 403 empty response | FAIL | Controller log records JSON parse error; HTTP response is 403 |
| Rate limit below limit | Repeated harmless `GET /api/companies` | Requests allowed until quota | First two remaining quota requests: 200 | PASS | Live responses |
| Rate limit above limit | Continued harmless GETs | HTTP 429 | Subsequent three requests: 429 | PASS | Live responses |
| Schema validation | Hibernate `ddl-auto=validate` during startup | Mapping/schema compatible | Context started with validated schema | PARTIAL | Verifies mapped schema availability, not every live FK/constraint |

## 3. Bugs discovered

1. **Error handling is masked as HTTP 403.**  
   - **Location:** `AuthService.login`, `GlobalExceptionHandler`, `WebSecurityConfig`.  
   - **Cause:** Login uses uncaught `RuntimeException`; validation and JSON-read errors have no dedicated handler. The error dispatch is itself protected by `anyRequest().authenticated()`, resulting in an empty 403 response.  
   - **Impact:** Invalid credentials, missing fields, and malformed requests do not return predictable client errors; clients cannot distinguish authorization failure from input/authentication failure.  
   - **Recommended fix:** Use explicit authentication exceptions plus a controlled exception handler; handle bean-validation and unreadable-JSON exceptions; ensure the error path produces the intended error response without exposing internals.

2. **Roll number is not database-unique.**  
   - **Location:** `entity/User.java`, `AuthService.register`.  
   - **Cause:** `roll_number` lacks `unique = true`; only a pre-save repository lookup prevents duplicates.  
   - **Impact:** Concurrent registration can create duplicate roll numbers, violating the requested identity constraint and making login ambiguous.  
   - **Recommended fix:** Add a database unique constraint through the normal migration process, retain the service check for a friendlier error, and map constraint violations to 409.

3. **Most write DTOs have no effective validation.**  
   - **Location:** company, batch, department, question, login, and experience-question request handling.  
   - **Cause:** Missing constraint annotations and/or absent `@Valid`.  
   - **Impact:** Blank/null/oversize values reach services or the database and can yield 500/403-masked errors rather than useful 400 responses.  
   - **Recommended fix:** Define request constraints and apply `@Valid`; add consistent validation error handling.

4. **AI processing can leave partial data when a later question/link fails.**  
   - **Location:** `AsyncQuestionProcessingService`.  
   - **Cause:** The worker saves each question and link independently; the method is not transactional. Duplicate links hit the `(experience_id, question_id)` unique constraint after earlier writes.  
   - **Impact:** A `FAILED` experience can retain some question links and altered question frequencies.  
   - **Recommended fix:** Make the persistence portion transactional or perform safe idempotent link creation; test failure rollback with mocked Gemini/embedding responses.

5. **Question frequency field has two incompatible meanings.**  
   - **Location:** `QuestionService.saveQuestion` and `AiRetryService.resetFailedAiProcessing`.  
   - **Cause:** Creation/duplicate processing increments `Question.frequency`, but retry resets it to the number of remaining links. Admin-created questions begin at 1 even with no links.  
   - **Impact:** Stored frequency can disagree with the frequency query and change unexpectedly on retry.  
   - **Recommended fix:** Establish one invariant (preferably derive frequency from distinct links) and migrate/test accordingly.

## 4. Security findings

- BCrypt password hashing is used; raw passwords were neither logged nor reported.
- JWTs are signed and expiration is checked. The filter directly trusts the token subject and role and does not verify that the referenced user still exists or still has that role. Role revocation/deletion therefore does not invalidate an issued token before expiry.
- Authorization rules correctly separated ADMIN-only writes and admin routes in the executed cases. Unauthenticated protected endpoints return 403 rather than a conventional 401, and invalid-token responses are a plain-text custom response.
- CORS is limited to the two localhost development origins but permits all request headers and credentials. Production origins are not configured in this source.
- Rate limiting is per in-memory client IP (20/minute), confirmed with 429. It resets on process restart and is not shared across application instances, so it is not a distributed-production control.
- `AsyncQuestionProcessingService` uses `printStackTrace()`, which can expose provider/network details in logs. Replace with structured, redacted logging.
- Gemini API-key use is sourced from environment configuration; no key value was read or exposed during testing.

## 5. Tests not automated/executed

These require authenticated write access and safe isolated test data. They were intentionally not run against the configured populated database because no local SQL client/test profile existed for deterministic cleanup:

- Valid USER and valid ADMIN login with real credentials.
- Registration success, mismatch, duplicates, invalid batch/department, and all company/batch/department create/update/duplicate/inactive scenarios.
- Experience creation, 20-experience cap, invalid/inactive company, text limits, and individual/company retrieval with known data.
- Question creation and all frequency filter/order combinations using controlled link fixtures.
- Experience-question link uniqueness and live FK/orphan checks.
- AI `PENDING -> PROCESSING -> COMPLETED/FAILED`, retry, extraction, link creation, and duplicate similarity behavior. These would call Gemini/embedding services, which were deliberately not spammed.
- Full database FK/unique-constraint inspection. Hibernate schema validation passed, but it does not prove all database constraints.

## 6. Manual frontend testing

- Login/registration error messages and handling of the current 403 responses.
- CORS behavior from the actual production frontend origin.
- UI handling/polling of AI statuses, failed retry, and question-frequency presentation.
- Client token storage, logout, expiry, and behavior after role changes.

## 7. Production testing

- Run write-path integration tests in a dedicated disposable PostgreSQL database with test-only accounts.
- Exercise Gemini with a capped sandbox key and mocked/recorded responses before enabling real provider traffic.
- Test rate limiting across multiple deployed instances/proxy IP handling.
- Validate secret rotation, token revocation policy, production CORS origins, log redaction, backups, and migration-enforced constraints.

## 8. Overall summary

**Build/startup/database connectivity passed.** Public catalog endpoints, role gates, authenticated reads, enum validation, and the local in-memory rate limit were verified. The suite has only one existing context-load test. The production-safe live pass found a reproducible authentication/error-response defect and several source-level data-validation/integrity risks. No real records were changed and no AI calls were made.
