# Implementation Plan: manual-1dcddfb8

## Technical Context
- accepted_impact_hash:sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de
- accepted_impact_revision:1
- ac-1:grounding:28037901f7b2
- provider-object:107598
- provider-object:103672
- provider-object:1184427
- provider-object:1105675
- provider-object:1165884
- provider-object:1156550
- provider-object:1139785
- provider-object:1161502
- provider-object:1189871
- provider-object:1168570
- provider-object:1144797
- provider-object:1135246
- provider-object:1182039
- provider-object:1143445
- determinism-snapshot:sha256:6fb4c96c47836e0de74c3bd44ebcc5f6290ef038da6306e619cd4a9ab98411dc
- fingerprint:f4922aefad42f3de
- cast-application:CarePay
- repository:https://github.com/XEBIA-ACE/ACE_APP_TEMPLATE
- commit:11ecc5661862c03867eebf122bf3cfdcf7e7c48b
- test-obligation:test:characterization:ac-1
- clause-hash:ac-1:sha256:b0c304f928cfc1ea7c36f8c5fe995af9eec98dc3c0462e8146a688c1cd0e032f
- impact-content-hash:sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de
- grounding-evidence-hash:sha256:a099809498affc4e2d0b4998d6195afe7cbbf6634c2cba1013a1c1e68ceb4bee
- blocking-reason:all-transaction-call-graphs-failed
- blocking-reason:no-export-pipeline-component-in-CAST
- blocking-reason:net-new-dashboard-alerting-feedback-not-in-CAST
- blocking-reason:ac-1-requirement-traceability-incomplete
- Implement only against accepted impact revision 1 (sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de)
- Resolve or explicitly accept implementation-readiness item: All transaction call graphs failed to resolve, preventing topology-based impact assessment — carried verbatim from artifacts.accepted_impact_analysis.blocking_reasons_carried_to_spec
- Resolve or explicitly accept implementation-readiness item: CAST structural facts do not identify any export-pipeline component, export-specific transaction, or export-related DB table — the dashboard has no confirmed structural anchor in the current codebase as scoped — carried verbatim from artifacts.accepted_impact_analysis.blocking_reasons_carried_to_spec
- Resolve or explicitly accept implementation-readiness item: Net-new dashboard, alerting, and feedback components are required but not present in CAST analysis — implementation scope cannot be validated from current CAST data alone — carried verbatim from artifacts.accepted_impact_analysis.blocking_reasons_carried_to_spec
- Resolve or explicitly accept implementation-readiness item: Requirement traceability incomplete for ac-1 — carried verbatim from artifacts.accepted_impact_analysis.blocking_reasons_carried_to_spec
- Resolve or explicitly accept implementation-readiness item: Which existing CarePay service or module owns the export feature whose metrics are to be monitored? CAST did not return export-specific transactions — carried verbatim from artifacts.impact_analysis.unresolved_questions
- Resolve or explicitly accept implementation-readiness item: Where will dashboard metric data be persisted? No export-related DB tables were found in CAST — carried verbatim from artifacts.impact_analysis.unresolved_questions
- Resolve or explicitly accept implementation-readiness item: What alerting infrastructure is available in CarePay (e.g., PagerDuty, email, Slack)? No alerting components were found in CAST — carried verbatim from artifacts.impact_analysis.unresolved_questions
- Resolve or explicitly accept implementation-readiness item: Are the 20 Builder transactions actually export-related, or are they purely framework builder chains unrelated to the export feature? — carried verbatim from artifacts.impact_analysis.unresolved_questions
- Resolve or explicitly accept implementation-readiness item: Why did all 5 transaction call graph fetches fail? Is the CAST MCP instance healthy for CarePay? — carried verbatim from artifacts.impact_analysis.unresolved_questions

## Design Steps

### Step 1 — PREREQUISITE RESOLUTION [DEGRADED — ac-1 UNMAPPED]
[DEGRADED — ac-1 UNMAPPED] PREREQUISITE RESOLUTION: Before any implementation work proceeds, resolve the four blocking gaps carried from impact acceptance: (1) all transaction call graphs for Builder (107598) failed to resolve — CAST MCP topology is unknown; (2) no export-pipeline component, export-specific transaction, or export-related DB table was identified in CarePay CAST facts — the dashboard has no confirmed structural anchor; (3) net-new dashboard, alerting, and feedback components are required but absent from CAST analysis; (4) requirement traceability for ac-1 is incomplete (status: unmapped). These gaps must be resolved — via CAST MCP remediation, manual codebase inspection, or explicit scope decision — before implementation steps 2–10 can be safely executed.

Evidence: accepted_impact_hash sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de, revision 1; ac-1:grounding:28037901f7b2; provider-object:107598; determinism-snapshot sha256:6fb4c96c47836e0de74c3bd44ebcc5f6290ef038da6306e619cd4a9ab98411dc

### Step 2 — Export Instrumentation [ac-1 / FR-1.1, FR-1.2]
SCOPED TO: portal.proxy.js (exports, CAST ID 1184427) and its 8 confirmed direct callers (portal.proxy.aar_uat.js CAST ID 1139785, portal.proxy.acs_acc.js CAST ID 1161502, portal.proxy.acs_prod.js CAST ID 1189871, portal.proxy.ae_acc.js CAST ID 1168570, portal.proxy.ae_prod.js CAST ID 1144797, portal.proxy.ae_uat.js CAST ID 1135246, portal.proxy.ken_acc.js CAST ID 1182039, portal.proxy.ken_prod.js CAST ID 1143445). Instrument the exports function (CAST ID 1184427) in portal.proxy.js to emit export completion events carrying error type, success/failure status, and completion timestamp. This instrumentation is the confirmed touch-point closest to export execution visible in CAST structural facts. Note: the 8 proxy callers span environment variants (aar_uat, acs_acc, acs_prod, ae_acc, ae_prod, ae_uat, ken_acc, ken_prod) — instrumentation must not introduce environment-specific branching that breaks existing proxy behaviour.

Evidence: accepted_impact_hash sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de; CAST object 1184427 (exports, JavaScript function, portal.proxy.js); CAST direct callers 1139785, 1161502, 1189871, 1168570, 1144797, 1135246, 1182039, 1143445; determinism-snapshot sha256:6fb4c96c47836e0de74c3bd44ebcc5f6290ef038da6306e619cd4a9ab98411dc; fingerprint f4922aefad42f3de

### Step 3 — New Database Schema [ac-1 / FR-1.1, FR-1.2]
SCOPED TO: public.ctl_bundles.exports (CAST ID 1105675) — PostgreSQL Table Column. Design and add new database schema objects (table or columns) to persist export error event telemetry (error type, error count, timestamp, export duration, success flag) required to back the error-rate-by-type dashboard view. CAST confirms public.ctl_bundles.exports (1105675) exists as a PostgreSQL table column but no dedicated error-metrics or dashboard-metrics table was found — new schema is required. Schema must be additive (no alteration of public.ctl_bundles structure confirmed by CAST).

Evidence: accepted_impact_hash sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de; CAST object 1105675 (public.ctl_bundles.exports, PostgreSQL Table Column); impact data_impacts: 'Export event telemetry tables absent from CAST structural facts — new schema design required'; fingerprint f4922aefad42f3de

### Step 4 — Dashboard Route Registration [ac-1 / FR-1.1, FR-1.2]
SCOPED TO: DashboardChildFeatureFlaggedRouteGuard (CAST ID 1165884) — TypeScript Class, dashboard-child-feature-flagged-route.guard.ts. Extend the existing dashboard routing module (confirmed by CAST at frontend/apps/portal/src/app/dashboard/routing/) to add a protected route for the new export error metrics dashboard view. The DashboardChildFeatureFlaggedRouteGuard (1165884) with its canActivate method (CAST ID 1156550) is the confirmed access-control mechanism for dashboard child routes — the new error dashboard route must be registered as a child route guarded by this class. Role-based access must restrict dashboard access to authorised roles only (Product Manager, Operations).

Evidence: accepted_impact_hash sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de; CAST object 1165884 (DashboardChildFeatureFlaggedRouteGuard, TypeScript Class); CAST intra-method 1156550 (canActivate); source file dashboard-child-feature-flagged-route.guard.ts; fingerprint f4922aefad42f3de

### Step 5 — Error Dashboard Angular Component [ac-1 / FR-1.1, FR-1.2]
SCOPED TO: Angular portal frontend (confirmed technology: @angular/core 20.3.19, @angular/router 20.3.19, @apollo/client 3.14.1, @datadog/browser-rum 6.27.1 — all confirmed in dependency_graph). Implement the error dashboard Angular component within the portal frontend app (frontend/apps/portal/src/app/dashboard/) that queries the new error telemetry data store and renders error rate broken down by error type. The component must comply with WCAG 2.1 AA (accessible charts, keyboard navigation) per ac-1 accessibility obligations. Use @datadog/browser-rum (confirmed v6.27.1) for RUM instrumentation if it is the designated observability tool; do not introduce unconfirmed monitoring libraries.

Evidence: accepted_impact_hash sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de; dependency_graph modules: @angular/core 20.3.19, @angular/router 20.3.19, @apollo/client 3.14.1, @datadog/browser-rum 6.27.1; CAST object 1165884 (dashboard routing); fingerprint f4922aefad42f3de

### Step 6 — Threshold Alerting Logic [ac-1 / FR-1.3, FR-1.4]
SCOPED TO: exports instrumentation in portal.proxy.js (CAST ID 1184427) and the new telemetry schema (step 3). Implement threshold evaluation logic that reads aggregated export metrics from the new telemetry store and triggers alerts when: (a) export success rate drops below 95%, (b) average export completion time exceeds 10 seconds, or (c) export error rate exceeds the configurable threshold. NOTE: The alerting delivery channel (email, Slack, PagerDuty) is an open gap in ac-1 (FR-1.4) — this step must be scoped to the threshold detection and alert dispatch interface only; the concrete channel must be decided before implementation. Alerting payloads must not expose PII or PHI (CarePay healthcare payments domain constraint).

Evidence: accepted_impact_hash sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de; ac-1 FR-1.3/FR-1.4 gap: alerting channel unspecified; CAST object 1184427 (exports); fingerprint f4922aefad42f3de

### Step 7 — Post-Export Feedback Mechanism [ac-1 / FR-1.5, FR-1.6]
SCOPED TO: portal.proxy.js exports function (CAST ID 1184427) and its 8 confirmed caller proxy files (CAST IDs 1139785, 1161502, 1189871, 1168570, 1144797, 1135246, 1182039, 1143445). After a successful export completion event is detected via the instrumented exports function, trigger the post-export user satisfaction feedback mechanism in the portal UI. The mechanism must be: non-blocking (optional for the user), keyboard-navigable, and screen-reader compatible (WCAG 2.1 AA). NOTE: The specific feedback form design (rating scale, survey questions, periodic survey frequency) is an open gap in ac-1 (FR-1.6) — this step implements the trigger and presentation shell only; form content must be resolved before implementation. Feedback data collection must comply with applicable data privacy regulations (consent, data minimisation).

Evidence: accepted_impact_hash sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de; CAST object 1184427 (exports); CAST direct callers 1139785, 1161502, 1189871, 1168570, 1144797, 1135246, 1182039, 1143445; ac-1 FR-1.5/FR-1.6; fingerprint f4922aefad42f3de

### Step 8 — Safety Guard: Do Not Modify Builder (107598) [SAFETY]
SCOPED TO: Builder (CAST ID 107598) — com.authzed.api.v1.SubjectReference.Builder, and its intra-object build() method (CAST ID 103672). Builder (107598) participates in 20 CAST-measured transactions spanning Spring Security, AWS SDK, and Elasticsearch builder chains. Any instrumentation or modification touching the build() method (103672) risks side effects across all 20 transaction paths. This step mandates: (a) do NOT modify Builder (107598) or build() (103672) as part of this enhancement — they are generic framework-level builder pattern classes with no confirmed export-domain role; (b) if future investigation confirms an export-specific builder subclass exists, that subclass must be identified by CAST ID before any change is made.

Evidence: accepted_impact_hash sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de; CAST object 107598 (Builder, 20 transactions); CAST intra-method 103672 (build); impact dependency_impacts: 'instrumentation added to build() method risks side effects across all 20 transaction paths'; fingerprint f4922aefad42f3de

### Step 9 — Test Implementation [TEST]
SCOPED TO: all confirmed components. Implement the required test obligations before release: (1) Unit tests for error rate aggregation logic by error type (grounded in new telemetry schema, step 3); (2) Integration tests for threshold-based alerting triggers — success rate < 95%, completion time > 10s, error rate breach (grounded in step 6); (3) End-to-end tests for post-export feedback mechanism presentation and submission flow via the instrumented exports function (CAST ID 1184427) (grounded in step 7); (4) Performance tests to confirm dashboard load does not degrade export transaction throughput across the 8 confirmed proxy callers (CAST IDs 1139785, 1161502, 1189871, 1168570, 1144797, 1135246, 1182039, 1143445); (5) Characterisation test for ac-1 (obligation ID: test:characterization:ac-1, required_before: release, required_environment: ci).

Evidence: accepted_impact_hash sha256:c3b67625916488451f8dac4774b240278c9b357b6ef165b500c1f2314a1154de; test obligation test:characterization:ac-1; ac-1:grounding:28037901f7b2; CAST objects 1184427, 1139785, 1161502, 1189871, 1168570, 1144797, 1135246, 1182039, 1143445; fingerprint f4922aefad42f3de

## Contract Changes
- API: endpoint — no ID returned; method — unknown; note — API inventory queries returned errors or non-structured results; no concrete endpoints mapped to Builder (107598). Contract change required.
- Data: No database tables found for application 'CarePay' with name containing 'Build'. Dashboard metrics storage (error rates, performance timings, satisfaction scores) will require new data infrastructure not currently evidenced in CAST. Contract change required.
- Data: Export event telemetry tables (for error type tracking, completion time, success rate) are absent from CAST structural facts — new schema design required. Contract change required.

## API Impact
- **Contract Change Required**: true
- **Checkpoint**: implementation_readiness
- **Status**: ready_with_gaps
- **Impacted Service IDs**: https-github-com-xebia-ace-ace-app-template
- **Contract Change Reasons**:
  - Grounded API impact was identified: API inventory queries for CarePay (scoped to Dashboard, Export, and Build) returned errors or non-structured results — no concrete REST endpoints were mapped, but the impact analysis confirms an API touchpoint exists with an unresolved endpoint (no ID returned), indicating a net-new API contract will be required for dashboard metrics exposure and post-export feedback collection.
  - Grounded data-contract impact was identified: CAST structural facts confirm no export-related DB tables exist in CarePay for Dashboard, Export, or Build scopes — new schema objects (export event telemetry tables, dashboard metrics store, feedback store) must be designed and introduced, constituting a new data contract.
- **Unresolved Items** (carried verbatim):
  - All transaction call graphs failed to resolve, preventing topology-based impact assessment — carried verbatim from artifacts.accepted_impact_analysis.blocking_reasons_carried_to_spec
  - CAST structural facts do not identify any export-pipeline component, export-specific transaction, or export-related DB table — the dashboard has no confirmed structural anchor in the current codebase as scoped — carried verbatim from artifacts.accepted_impact_analysis.blocking_reasons_carried_to_spec
  - Net-new dashboard, alerting, and feedback components are required but not present in CAST analysis — implementation scope cannot be validated from current CAST data alone — carried verbatim from artifacts.accepted_impact_analysis.blocking_reasons_carried_to_spec
  - Requirement traceability incomplete for ac-1 — carried verbatim from artifacts.accepted_impact_analysis.blocking_reasons_carried_to_spec
  - Which existing CarePay service or module owns the export feature whose metrics are to be monitored? CAST did not return export-specific transactions — carried verbatim from artifacts.impact_analysis.unresolved_questions
  - Where will dashboard metric data be persisted? No export-related DB tables were found in CAST — carried verbatim from artifacts.impact_analysis.unresolved_questions
  - What alerting infrastructure is available in CarePay (e.g., PagerDuty, email, Slack)? No alerting components were found in CAST — carried verbatim from artifacts.impact_analysis.unresolved_questions
  - Are the 20 Builder transactions actually export-related, or are they purely framework builder chains unrelated to the export feature? — carried verbatim from artifacts.impact_analysis.unresolved_questions
  - Why did all 5 transaction call graph fetches fail? Is the CAST MCP instance healthy for CarePay? — carried verbatim from artifacts.impact_analysis.unresolved_questions

## Security and Quality
- Dashboard endpoints exposing error rates, performance metrics, and user satisfaction data must enforce role-based access control — only authorized roles (e.g., Product Manager, Operations) should access monitoring data.
- Alerting mechanisms must not expose PII or PHI in notification payloads (CarePay is a healthcare payments domain).
- Post-export user satisfaction feedback collection must comply with applicable data privacy regulations (consent, data minimization).
- Verify the changed user flow against the stated accessibility criteria.

## Accessibility Obligations
- The export performance and error dashboard UI must meet WCAG 2.1 AA standards, including accessible charts/graphs for error rate visualization.
- Post-export feedback/rating mechanism must be keyboard-navigable and screen-reader compatible.

## Verification Plan
- **ac-1**: Given the error dashboard is accessed, When viewing error metrics, Then it displays error rate by error type to identify common failure patterns Given monitoring thresholds are defined, When success rate drops below 95%, average completion time exceeds 10 seconds, or error rate exceeds threshold, Then alerts notify the team Given a user completes an export, When the export finishes, Then a user satisfaction feedback mechanism is presented (e.g., optional post-export rating or periodic survey)
  - Test Obligation ID: test:characterization:ac-1
  - Required Before: release
  - Required Environment: ci
  - Test Type: characterization
- **IMPACT**: Unit tests required for error rate aggregation logic by error type.
- **IMPACT**: Integration tests required for threshold-based alerting triggers (success rate < 95%, completion time > 10s, error rate breach).
- **IMPACT**: End-to-end tests required for post-export feedback mechanism presentation and submission flow.
- **IMPACT**: Performance tests required to ensure the dashboard itself does not degrade export transaction throughput across the 20 identified transaction paths.
- **IMPACT**: Alert notification delivery tests (verify team notification on threshold breach).

## Affected Components (CAST-grounded)
- Builder (CAST ID: 107598) — com.authzed.api.v1.SubjectReference.Builder, Java Class, SubjectReference_9508.java
- build (CAST ID: 103672) — Java Method, intra-object member of Builder (107598)
- portal.proxy.aar_uat.js (CAST ID: 1139785) — JavaScript File, direct caller of exports (1184427)
- portal.proxy.acs_acc.js (CAST ID: 1161502) — JavaScript File, direct caller of exports (1184427)
- portal.proxy.acs_prod.js (CAST ID: 1189871) — JavaScript File, direct caller of exports (1184427)
- portal.proxy.ae_acc.js (CAST ID: 1168570) — JavaScript File, direct caller of exports (1184427)
- portal.proxy.ae_prod.js (CAST ID: 1144797) — JavaScript File, direct caller of exports (1184427)
- portal.proxy.ae_uat.js (CAST ID: 1135246) — JavaScript File, direct caller of exports (1184427)
- portal.proxy.ken_acc.js (CAST ID: 1182039) — JavaScript File, direct caller of exports (1184427)
- portal.proxy.ken_prod.js (CAST ID: 1143445) — JavaScript File, direct caller of exports (1184427)
- DashboardChildFeatureFlaggedRouteGuard (CAST ID: 1165884) — TypeScript Class, dashboard routing guard, dashboard-child-feature-flagged-route.guard.ts
- exports (CAST ID: 1184427) — JavaScript function, portal.proxy.js
- public.ctl_bundles.exports (CAST ID: 1105675) — PostgreSQL Table Column, SQL

## Assumptions (carried from impact analysis)
- The 'Builder' focus object (107598) is the generic builder pattern class in SubjectReference_9508.java; it is not a dedicated export pipeline or dashboard component — the story's dashboard feature is net-new.
- The 20 transactions identified by CAST are framework-level builder chains (Spring Security, AWS, Elasticsearch), not export-specific business transactions; export-specific transactions may exist but were not returned by CAST.
- Dashboard, alerting, and feedback components described in the story do not yet exist in the CarePay codebase as evidenced by CAST.
- Transaction call graph fetch failures are infrastructure-level CAST MCP errors, not indicative of missing transactions.
