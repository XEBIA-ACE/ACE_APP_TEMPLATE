# Implementation Plan: CT-4

## Technical Context & Grounding

All implementation decisions must be grounded in accepted impact revision 1 (sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057), CAST fingerprint f4922aefad42f3de, and the clause hashes listed below.

**Provider Objects (CAST-confirmed):**
- Receipt (171795) — Java Class
- ReceiptRepository (182097) — Java Interface
- ReceiptingService (171785) — Java Class
- InstalmentReceiptSearch (171699) — Java Class
- ReceiptMapper (182108) — Java Interface
- PaymentDetails (177416) — Java Record
- ReceiptDocumentGenerationService (171701) — Java Class
- ReceiptDocumentDataCollector (171702) — Java Class
- ReceiptDocumentMapper (182088) — Java Interface
- InstalmentReceipt (171796) — Java Class
- InstalmentReceiptMapper (182116) — Java Interface
- IReceiptingService (182122) — Java Interface
- CreateReceiptResolver (169458) — Java Class
- GetPremiumReceiptsGQL (1143458) — Typescript Class

**Database (CAST-confirmed):**
- public.instalment_receipt (1100473) — PostgreSQL Table
  - amount (1103337): decimal(23,8)
  - date_created (1104514): datetime
  - status (1100792): varchar(30)
  - created_by (1101631): varchar(255)
  - deleted_at (1104050): datetime(6)

**Clause Hashes:**
- CT-4-AC-1: sha256:05bf5c9ada7c2001360961adee654dc9bfc63fbf01ee07c9de83e832ca367b29
- CT-4-AC-2: sha256:71621a7ff77fdf323d6d21fae5d3df06c965eb991f60c901c5abde710f3e215f
- CT-4-AC-3: sha256:57218033c3d4f8295c7a0154a253713a3a10a4c62ed378193acad261891d859a
- CT-4-AC-4: sha256:a2e0615d2152850379a1b73f7055577eead6fa9a28c7700b11160ddde7a3d8a8
- CT-4-AC-5: sha256:bf57044f7d6a0c32b233315b8b1bde2feb16219f807186215b183fadc034a3ce
- CT-4-AC-6: sha256:d3d8410578de118fead9792ead611754449db0a562b6eed3528a96272eac792b
- CT-4-AC-7: sha256:b874811b0896002fd97505f608c861e33b24da8032980161dbc2043b9b8cd1c0
- CT-4-AC-8: sha256:8226e299f3bfc03f7cac5dc075182a53fc50ccc02162103d70453b671f55a80c
- CT-4-AC-9: sha256:4d855c8e5ac23af411c8ff7015d2454a4f1003d138abbfe7cd30f07f8b3c5c8e

---

## Implementation-Readiness Items to Resolve or Explicitly Accept

The following items are carried forward verbatim from the accepted impact analysis and must be resolved or explicitly accepted before implementation proceeds:

1. Requirement traceability incomplete for CT-4-AC-1, CT-4-AC-2, CT-4-AC-3, CT-4-AC-4, CT-4-AC-5, CT-4-AC-6, CT-4-AC-7, CT-4-AC-8, CT-4-AC-9 (carried forward verbatim from accepted impact blocking_reasons)
2. What is the receipts base table name in CarePay's database? CAST returned no results for 'Receipts' — without this, full column availability (vendor, category, receipt ID) cannot be confirmed structurally
3. Does vendor and category data reside in the instalment_receipt table or a joined table? Neither column appears in the confirmed instalment_receipt schema
4. Is ReceiptDocumentGenerationService (171701) intended to be extended for Excel export, or should a new ExcelExportService be created?
5. Which Excel generation library has been selected (Apache POI, EPPlus, ExcelJS)? This affects both backend technology stack and performance characteristics
6. Where will temporary export files be stored, and what is the cleanup mechanism? No file storage service is visible in CAST inventory
7. What is the existing audit logging infrastructure? No audit log table or service was identified in CAST facts — CT-4-AC-9 implementation path is unknown
8. Are the 20 transactions passing through Receipt (171795) all active in production, or are some legacy/deprecated? Call graphs were unavailable to confirm
9. Does GetPremiumReceiptsGQL already support date-range and filter parameters, or must the GraphQL schema be extended?
10. Receipts base database table not found in CAST inventory — vendor, category, receipt ID column availability unconfirmed
11. Transaction call graphs unavailable for all 5 expanded transactions (API fetch errors) — full call chain depth cannot be confirmed
12. No audit log table or service identified in CAST structural facts — CT-4-AC-9 implementation evidence is absent
13. No Excel export service or file storage service exists in CAST inventory — these are net-new components with no structural baseline
14. Level-2+ transitive callers returned 0 results — may reflect CAST scan depth limit rather than true isolation; cannot confirm absence of deeper dependencies
15. API endpoint inventory returned errors for 3 of 4 calls — full REST/GraphQL surface area of receipt-related endpoints is not confirmed
16. The receipts base table exists in CarePay's database but was not resolved by CAST tooling; the instalment_receipt table (1100473) is the only confirmed DB object (assumption carried forward)
17. The Receipt Java Class (171795) is the canonical domain entity for export data sourcing; the JPA Entity Receipt (166687) is its persistence counterpart (assumption carried forward)
18. ReceiptDocumentGenerationService (171701) and ReceiptDocumentDataCollector (171702) represent existing patterns that the new export service should follow or extend (assumption carried forward)
19. PaymentDetails (177416) Java Record provides payment method data required for the 'payment method' export column (assumption carried forward)
20. No Excel export functionality currently exists in CarePay — this is a net-new capability (assumption carried forward)
21. Transaction call graphs were unavailable (5 of 5 fetch attempts failed); blast radius is based on object-level caller/transaction counts only (assumption carried forward)

---

## Affected Services

- **premium-calculation-invoicing** — Backend service hosting Receipt (171795), ReceiptRepository (182097), ReceiptingService (171785), InstalmentReceiptSearch (171699), ReceiptMapper (182108), PaymentDetails (177416), ReceiptDocumentGenerationService (171701), ReceiptDocumentDataCollector (171702). Grounded in CAST object graph, fingerprint f4922aefad42f3de.
- **graphql-gateway** — Hosts CreateReceiptResolver (169458) and GetPremiumReceiptsGQL (1143458) — the GraphQL entry points through which the export mutation/query will be exposed. Grounded in CAST transaction inventory, fingerprint f4922aefad42f3de.

---

## API / Contract Changes

**Contract change required:** true

**Reasons:**
- Grounded API impact was identified: five API/service touchpoints (CreateReceiptGQL, CreateReceiptResolver, GetPremiumReceiptsGQL, GetPremiumReceiptBconValuesGQL, IReceiptingService) pass through Receipt (171795) and the export capability requires extending or adding a GraphQL mutation/query and the IReceiptingService interface contract.
- Grounded data-contract impact was identified: the public.instalment_receipt table (ID 1100473) is the only CAST-confirmed DB table in scope and the export requires new filtered, date-ranged queries against it; additionally, the receipts base table (unresolved in CAST) must be identified and its schema confirmed before the full data contract can be established.

**API Touchpoints:**
- CreateReceiptGQL (GraphQL Mutation) — Transaction passing through Receipt (171795); call graph unavailable
- CreateReceiptResolver (GraphQL Resolver) — Transaction passing through Receipt (171795); call graph unavailable
- GetPremiumReceiptsGQL (GraphQL Query) — Transaction passing through Receipt (171795); most directly relevant to export data retrieval
- GetPremiumReceiptBconValuesGQL (GraphQL Query) — Transaction passing through Receipt (171795); may overlap with export data scope
- IReceiptingService (Service Interface) — Transaction/interface boundary passing through Receipt (171795)

**Data Touchpoints:**
- public.instalment_receipt (1100473) — Only CAST-confirmed DB table in scope; contains amount (decimal 23,8), currency (varchar 3), status (varchar 30), date_created (datetime), instalment_id (FK bigint), receipt_id (FK bigint) — these fields partially satisfy the required export columns
- receipts base table (NOT FOUND) — A dedicated receipts base table was NOT found in CAST DB inventory for CarePay (query returned no results for 'Receipts'); this is a significant evidence gap for confirming full column availability (vendor, category, payment method, receipt ID)
- NOTE: The instalment_receipt table has no primary key defined in CAST metadata; unique constraints exist on (instalment_id, receipt_id) composite — export queries must account for this
- NOTE: Soft-delete columns (deleted_at, deleted_by, deleted_reason) exist on instalment_receipt; export logic must apply appropriate filters to exclude soft-deleted records
- PaymentDetails (177416) Java Record is a downstream callee of Receipt — payment method data for the export column likely flows through this object

**Impacted Services:** premium-calculation-invoicing, graphql-gateway

---

## Design Steps

**STEP 1 — Extend ReceiptRepository (182097):** Add a new JPQL/Spring Data query method accepting date-range (start/end datetime) and optional filter parameters (status from instalment_receipt.status 1100792, date_created 1104514) to return a filtered, soft-delete-aware (WHERE deleted_at IS NULL) list of Receipt (171795) entities. This method is the sole data-access foundation for CT-4-AC-1 and CT-4-AC-2. Note: vendor, category, and receipt ID column availability is unconfirmed in CAST DB inventory (receipts base table not found); the query must be validated against the actual schema before merge. [Evidence: ReceiptRepository (182097), public.instalment_receipt (1100473), CT-4-AC-1, CT-4-AC-2, accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057]

**STEP 2 — Extend InstalmentReceiptSearch (171699):** Validate and, if necessary, extend the existing filter-logic class to support all export filter parameters (category, vendor, status, date range) required by CT-4-AC-1 and CT-4-AC-2. Reuse confirmed filter logic rather than building a parallel path, to avoid duplication. Scope of existing filter support for category/vendor is unconfirmed in CAST facts — this step must begin with a code review of InstalmentReceiptSearch before implementing changes. [Evidence: InstalmentReceiptSearch (171699), CT-4-AC-1, CT-4-AC-2, accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057]

**STEP 3 — Add export operation to IReceiptingService and ReceiptingService (171785):** Declare a new exportReceipts(filters, dateRange) method on IReceiptingService (Java Interface, CAST ID 182122) and implement it in ReceiptingService (171785). The implementation must: (a) call the extended ReceiptRepository (182097) query from Step 1; (b) return an empty-result signal when no records match (CT-4-AC-6); (c) delegate to the new ExcelExportService (Step 4) when records exist; (d) not modify any existing createReceipt, buildReversalReceipt, or calculateReceiptedAmount logic to avoid side-effects on the 20 existing transactions passing through Receipt (171795). [Evidence: ReceiptingService (171785), IReceiptingService (182122), ReceiptRepository (182097), CT-4-AC-1, CT-4-AC-6, accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057]

**STEP 4 — Implement ExcelExportService (net-new, modelled after ReceiptDocumentGenerationService 171701):** Create a new ExcelExportService class in the premium-calculation-invoicing service, following the structural pattern of ReceiptDocumentGenerationService (171701) and ReceiptDocumentDataCollector (171702). This service must: (a) accept a List<Receipt> from ReceiptingService; (b) use ReceiptMapper (182108) and PaymentDetails (177416) to project the confirmed fields (amount from instalment_receipt.amount 1103337, date from instalment_receipt.date_created 1104514, payment method from PaymentDetails 177416) into export row DTOs; (c) apply the approved Excel generation library to produce a .xlsx workbook with six required columns (Date, Vendor, Amount, Category, Payment Method, Receipt ID) with correct Excel cell types (date type for dates, currency type for amounts) per CT-4-AC-3 and CT-4-AC-7. NOTE: vendor, category, and receipt ID column sourcing is structurally unconfirmed — this step is degraded pending resolution of the receipts base table gap. [Evidence: ReceiptDocumentGenerationService (171701), ReceiptDocumentDataCollector (171702), ReceiptMapper (182108), PaymentDetails (177416), public.instalment_receipt.amount (1103337), public.instalment_receipt.date_created (1104514), CT-4-AC-3, CT-4-AC-7, accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057]

**STEP 5 — Implement progress tracking for large exports in ExcelExportService (CT-4-AC-4, DEGRADED — net-new, no structural anchor):** Add a progress-update mechanism to ExcelExportService that activates when the record count exceeds 1,000. The mechanism must emit incremental progress events consumable by the frontend. No existing async job, streaming, or progress-indicator component was found in the CAST object graph; the implementation pattern (SSE, WebSocket, polling endpoint) must be decided and is outside the confirmed CAST inventory. This step is marked degraded: CT-4-AC-4 is unmapped in the accepted impact analysis. [Evidence: CT-4-AC-4 unmapped per accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057, no structural anchor in CAST graph]

**STEP 6 — Implement file storage and download-link delivery (CT-4-AC-5, DEGRADED — net-new, no structural anchor):** After ExcelExportService generates the .xlsx workbook, persist it to a temporary file storage service (not present in CAST inventory) and return a time-limited download URL with a success confirmation payload to the calling GraphQL resolver. Implement cleanup logic (TTL-based deletion) to prevent storage bloat. This step is marked degraded: CT-4-AC-5 is unmapped in the accepted impact analysis and the file storage service is not a confirmed CAST component. [Evidence: CT-4-AC-5 unmapped per accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057]

**STEP 7 — Implement empty-result guard in ReceiptingService (171785) (CT-4-AC-6):** In the exportReceipts method added in Step 3, add a guard that checks the result set returned by ReceiptRepository (182097). If the result is empty, return a structured 'no data available' response without invoking ExcelExportService and without generating any file. ReceiptingService (171785) and ReceiptRepository (182097) are confirmed structural anchors for this guard. The UI messaging component is not in the CAST graph and must be implemented net-new. [Evidence: ReceiptingService (171785), ReceiptRepository (182097), CT-4-AC-6, accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057]

**STEP 8 — Extend GetPremiumReceiptsGQL / add new export GraphQL mutation in graphql-gateway:** Evaluate whether GetPremiumReceiptsGQL (1143458) already accepts date-range and filter parameters. If not, extend the GraphQL schema in the graphql-gateway service to add an exportReceipts mutation (or query) that accepts dateFrom, dateTo, category, vendor, and status inputs, delegates to ReceiptingService (171785) via IReceiptingService (182122), and returns either a download URL + success message or a 'no data available' indicator. The CreateReceiptResolver (169458) pattern should be followed for the new resolver. [Evidence: GetPremiumReceiptsGQL (1143458), CreateReceiptResolver (169458), IReceiptingService (182122), CT-4-AC-1, CT-4-AC-5, CT-4-AC-6, accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057]

**STEP 9 — Implement export audit logging (CT-4-AC-9, DEGRADED — net-new, no structural anchor):** Upon completion of any export operation in ReceiptingService (171785), write an audit log entry capturing: authenticated user identity, UTC timestamp, filter parameters applied (date range, category, vendor, status), and record count. No audit log table or audit service was identified in CAST structural facts; the audit log target (table, service, or event bus) must be determined before implementation. instalment_receipt.created_by (1101631) and modified_by (1105125) are record-level fields only and do not serve as an export-operation audit trail. This step is marked degraded: CT-4-AC-9 is unmapped in the accepted impact analysis. [Evidence: CT-4-AC-9 unmapped per accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057, ReceiptingService (171785), public.instalment_receipt.created_by (1101631)]

**STEP 10 — Performance hardening on ReceiptRepository (182097) query (CT-4-AC-8, partially mapped):** Validate that the new date-range + filter query added in Step 1 can return up to 10,000 Receipt (171795) rows and pass them through ExcelExportService within 10 seconds for 95% of requests. Confirm that the composite unique constraint on (instalment_id, receipt_id) in public.instalment_receipt (1100473) is exploited by the query plan. Add streaming/pagination at the ReceiptRepository layer if profiling shows memory pressure. No performance-optimised bulk-fetch pattern exists in the current CAST graph; this is net-new work. [Evidence: ReceiptRepository (182097), public.instalment_receipt (1100473), CT-4-AC-8, accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057]

**STEP 11 — Regression guard:** Verify existing transactions passing through Receipt (171795) are unaffected. Run the full existing test suite covering CreateReceiptGQL, GetPremiumReceiptsGQL, CreateReceiptResolver, and the 20 CAST-measured transactions that pass through Receipt (171795). Any change to ReceiptRepository (182097) or ReceiptingService (171785) must not alter the behaviour of existing methods (createReceipt 119713, buildReversalReceipt 131204, calculateReceiptedAmount 131454, findByReceiptCode 119739, generateAndUpload 119715). [Evidence: Receipt (171795), ReceiptRepository (182097), ReceiptingService (171785), createReceipt (119713), buildReversalReceipt (131204), calculateReceiptedAmount (131454), findByReceiptCode (119739), generateAndUpload (119715), accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057]

---

## Blast Radius

CAST measured 32 direct callers, 0 transitive callers, 25 callees, 25 transactions, and 9 data objects. CAST-assessed remediation priority: CRITICAL.

**Affected File:** `C:\cast-node\common-data\upload\CarePay\main_sources\premium-calculation-invoicing\src\main\java\com\carepay\premiumcalculation\domain\entities\receipts\Receipt.java`

**Affected Symbols:** AbstractEntity, AbstractIdEntity, BatchAggregateFilterOptions, BatchAggregateMapper, BatchAggregateService, Collection<E>, HashSet<E>, InstalmentReceipt, InstalmentReceiptMapper, InstalmentReceiptSearch, IsiroBatchClient, List<E>, Loader<K,V>, PaymentDetails, Receipt, ReceiptDocumentDataCollector, ReceiptDocumentGenerationService, ReceiptDocumentMapper, ReceiptMapper, ReceiptRepository, ReceiptingService, Set<E>, Stream<T>, UUID, batchFiltersLoader, buildReversalReceipt, calculateReceiptedAmount, collect, createReceipt, entityToDto, findByReceiptCode, generateAndUpload, getBatchFilters, getReceipt, getReceiptPdfUrl, log

---

## Dependency Impact

- ReceiptRepository (182097) — must be extended with date-range and multi-filter query methods to support export; changes here affect all 20 transactions passing through Receipt (171795)
- ReceiptingService (171785) — referenced 3 times as direct caller; any new export service method added here risks side-effects on existing receipt creation and instalment flows
- ReceiptDocumentGenerationService (171701) — existing document generation service is structurally analogous to the proposed export service; reuse or parallel implementation must be evaluated to avoid duplication
- InstalmentReceiptSearch (171699) — existing search class may contain reusable filter logic for the export; integration should be assessed before building new filter logic
- Excel generation library (Apache POI, EPPlus, or ExcelJS) — not yet present in CAST inventory; must be integrated as a new dependency
- File storage service for temporary export files — not present in CAST inventory; new infrastructure dependency
- Audit logging target — no audit log table or service identified in CAST facts; implementation path for CT-4-AC-9 is structurally unconfirmed

---

## Security & Quality Obligations

- Export endpoint must enforce authorization checks — only users with appropriate permissions may trigger export (assumption stated in story but no CAST enforcement evidence found)
- Exported .xlsx files containing financial data must be secured in temporary storage and cleaned up after download or retention period expiry to prevent unauthorized access
- Filter parameters (date range, category, vendor, status) must be validated and sanitized server-side to prevent injection into repository queries
- Audit trail must capture user identity, timestamp, filters applied, and record count for every export operation (CT-4-AC-9)
- Validate credential and personal-data handling against the story's acceptance criteria
- Verify the changed user flow against the stated accessibility criteria

---

## Verification Plan

- **CT-4-AC-1**: Given a user is on the receipts dashboard, When they select a date range and optional filters (category, vendor, status) and click 'Export to Excel', Then the system validates the selection and initiates export
- **CT-4-AC-2**: Given the export is initiated, When the export generates a .xlsx file, Then it contains only receipts within the selected date range and matching all applied filters
- **CT-4-AC-3**: Given the export file is generated, When the file is opened, Then it includes columns: date, vendor, amount, category, payment method, receipt ID with proper headers and correct data types (dates as dates, amounts as currency)
- **CT-4-AC-4**: Given the export includes >1000 receipts, When the export is processing, Then a progress indicator displays and updates during generation
- **CT-4-AC-5**: Given the export completes successfully, When the generation finishes, Then the system provides a download link with success confirmation message
- **CT-4-AC-6**: Given no receipts match the selected criteria, When the export is attempted, Then the system displays 'no data available' message and does not generate a file
- **CT-4-AC-7**: Given the exported file is downloaded, When opened in Excel, Then all columns are properly formatted and readable
- **CT-4-AC-8**: Given an export request is submitted, When the export processes up to 10,000 receipts, Then the operation completes within 10 seconds for 95% of requests
- **CT-4-AC-9**: Given an export operation is performed, When it completes, Then all export activities (user, timestamp, filters applied, record count) are logged to the audit trail
- **IMPACT**: Unit tests must cover: date range validation, filter application logic, .xlsx file generation, column header and data type correctness, empty-result handling, and audit log entry creation — targeting >80% coverage per Definition of Done
- **IMPACT**: Integration tests must validate end-to-end flow: UI filter selection → backend query via ReceiptRepository → file generation → download link delivery
- **IMPACT**: Performance tests must confirm export of 10,000 receipts completes within 10 seconds for 95% of requests, using realistic CarePay dataset volumes
- **IMPACT**: Data integrity tests must confirm exported data exactly matches source data in instalment_receipt and the receipts base table (once confirmed), with correct formatting and no data loss
- **IMPACT**: Regression tests must confirm existing transactions (CreateReceiptGQL, GetPremiumReceiptsGQL, CreateReceiptResolver, etc.) are unaffected by ReceiptRepository query additions

---

## Provenance

- **Policy Version:** `ace-spec-format/v1`
- **Accepted Impact Hash:** `sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057`
- **Accepted Impact Revision:** `1`
- **Jira Snapshot Hash:** `33fd0a33b32a34d2cf28c9056f7ca0fb10ae1c22fe46fba7914d[REDACTED-PHONE]ccdf`
- **Repository Base SHA:** `11ecc5661862c03867eebf122bf3cfdcf7e7c48b`
- **Determinism Ref (CAST Fingerprint):** `f4922aefad42f3de`
