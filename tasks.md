# Tasks: CT-4

## Implementation Tasks

- [ ] T001 [CT-4-AC-1] Implement 'Export to Excel' control on receipts dashboard: expose mandatory date range input and optional filter inputs for category, vendor, and status; add server-side validation that date range is present and well-formed; surface validation error to user on failure and prevent export initiation.
  - Evidence: ReceiptRepository (182097), InstalmentReceiptSearch (171699), ReceiptingService (171785), clause sha256:05bf5c9ada7c2001360961adee654dc9bfc63fbf01ee07c9de83e832ca367b29
  - Gap: No CAST evidence of existing UI export control, date-range validation component, or export-initiation endpoint. Vendor and category filter columns not confirmed in CAST DB inventory. New components required.

- [ ] T002 [CT-4-AC-2] Implement filtered receipt query: extend ReceiptRepository (182097) with a date-range and multi-filter query method; bind InstalmentReceiptSearch (171699) filter logic to export parameters; ensure generated .xlsx contains only receipts matching all applied criteria.
  - Evidence: ReceiptRepository (182097), Receipt (171795), InstalmentReceiptSearch (171699), public.instalment_receipt (1100473), clause sha256:71621a7ff77fdf323d6d21fae5d3df06c965eb991f60c901c5abde710f3e215f
  - Gap: No existing .xlsx generation pipeline or filter-to-query binding for date range in CAST graph. Receipts base table (with vendor, category fields) not found in CAST DB inventory. Excel library not present in CAST scan.

- [ ] T003 [CT-4-AC-3] Implement .xlsx file generation with required columns: implement ExcelExportService (net-new, modelled after ReceiptDocumentGenerationService 171701); produce six-column workbook (Date, Vendor, Amount, Category, Payment Method, Receipt ID) using ReceiptMapper (182108) and PaymentDetails (177416); enforce correct Excel cell types (date type for dates, currency type for amounts).
  - Evidence: ReceiptDocumentGenerationService (171701), ReceiptDocumentDataCollector (171702), ReceiptMapper (182108), PaymentDetails (177416), public.instalment_receipt.amount (1103337), public.instalment_receipt.date_created (1104514), clause sha256:57218033c3d4f8295c7a0154a253713a3a10a4c62ed378193acad261891d859a
  - Gap: Vendor, category, and receipt ID column sourcing structurally unconfirmed — degraded pending resolution of receipts base table gap. Excel generation library not in CAST inventory.

- [ ] T004 [CT-4-AC-4] Implement progress indicator for large exports (DEGRADED — net-new, no structural anchor): add progress-update mechanism to ExcelExportService activating when record count exceeds 1,000; emit incremental progress events consumable by frontend; ensure progress indicator is accessible to screen readers and keyboard navigation.
  - Evidence: CT-4-AC-4 unmapped per accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057; no structural anchor in CAST graph.
  - Gap: No existing async job, streaming, or progress-indicator component in CAST object graph. Implementation pattern (SSE, WebSocket, polling) must be decided.

- [ ] T005 [CT-4-AC-5] Implement file storage and download-link delivery (DEGRADED — net-new, no structural anchor): persist generated .xlsx to temporary file storage service; return time-limited download URL and success confirmation to calling GraphQL resolver; implement TTL-based cleanup.
  - Evidence: CT-4-AC-5 unmapped per accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057; file storage service not in CAST inventory.
  - Gap: No download-link generation service, file storage service, or success-confirmation messaging component in CAST graph.

- [ ] T006 [CT-4-AC-6] Implement empty-result guard in ReceiptingService (171785): add guard in exportReceipts method that checks result set from ReceiptRepository (182097); if empty, return 'no data available' response without invoking ExcelExportService and without generating any file; implement accessible UI messaging.
  - Evidence: ReceiptingService (171785), ReceiptRepository (182097), clause sha256:d3d8410578de118fead9792ead611754449db0a562b6eed3528a96272eac792b
  - Gap: No existing empty-result UI message component or guard logic in CAST graph. UI messaging must be implemented net-new.

- [ ] T007 [CT-4-AC-7] Validate .xlsx column formatting: ensure all six required columns (Date, Vendor, Amount, Category, Payment Method, Receipt ID) render as properly formatted and readable values when opened in Excel; date columns as date values, amount columns as currency values, text columns legible, no corrupted data.
  - Evidence: ReceiptMapper (182108), PaymentDetails (177416), public.instalment_receipt.amount (1103337), public.instalment_receipt.date_created (1104514), clause sha256:b874811b0896002fd97505f608c861e33b24da8032980161dbc2043b9b8cd1c0
  - Gap: Excel-specific column formatting requires a generation library not present in CAST inventory. Full column set (vendor, category, receipt ID) not confirmed in DB schema.

- [ ] T008 [CT-4-AC-8] Performance hardening on export path: validate that new ReceiptRepository (182097) query can return up to 10,000 Receipt (171795) rows and pass through ExcelExportService within 10 seconds for 95% of requests; exploit composite unique constraint on (instalment_id, receipt_id) in public.instalment_receipt (1100473); add streaming/pagination if profiling shows memory pressure.
  - Evidence: ReceiptRepository (182097), public.instalment_receipt (1100473), clause sha256:8226e299f3bfc03f7cac5dc075182a53fc50ccc02162103d70453b671f55a80c
  - Gap: No performance-optimised bulk-fetch pattern in current CAST graph. Primary receipts table unresolved.

- [ ] T009 [CT-4-AC-9] Implement export audit logging (DEGRADED — net-new, no structural anchor): upon completion of any export operation in ReceiptingService (171785), write audit log entry capturing authenticated user identity, UTC timestamp, filter parameters applied, and record count; determine audit log target (table, service, or event bus) before implementation.
  - Evidence: CT-4-AC-9 unmapped per accepted_impact_hash sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057; ReceiptingService (171785); public.instalment_receipt.created_by (1101631) is a record-level field only, not an export-operation audit trail.
  - Gap: No audit log table, audit service, or audit-writing component in CAST graph.

- [ ] T010 Extend GraphQL schema in graphql-gateway: evaluate whether GetPremiumReceiptsGQL (1143458) already accepts date-range and filter parameters; if not, add exportReceipts mutation/query accepting dateFrom, dateTo, category, vendor, status; follow CreateReceiptResolver (169458) pattern for new resolver; delegate to IReceiptingService (182122).
  - Evidence: GetPremiumReceiptsGQL (1143458), CreateReceiptResolver (169458), IReceiptingService (182122)

- [ ] T011 Regression guard: run full existing test suite covering CreateReceiptGQL, GetPremiumReceiptsGQL, CreateReceiptResolver, and all 20 CAST-measured transactions passing through Receipt (171795); confirm no change to behaviour of createReceipt (119713), buildReversalReceipt (131204), calculateReceiptedAmount (131454), findByReceiptCode (119739), generateAndUpload (119715).
  - Evidence: Receipt (171795), ReceiptRepository (182097), ReceiptingService (171785)

---

## Verification Tasks

- [ ] T012 Verify CT-4-AC-1: Given a user is on the receipts dashboard, When they select a date range and optional filters (category, vendor, status) and click 'Export to Excel', Then the system validates the selection and initiates export
  - Obligation ID: test:characterization:CT-4-AC-1 | Type: characterization | Required before: release | Environment: ci

- [ ] T013 Verify CT-4-AC-2: Given the export is initiated, When the export generates a .xlsx file, Then it contains only receipts within the selected date range and matching all applied filters
  - Obligation ID: test:characterization:CT-4-AC-2 | Type: characterization | Required before: release | Environment: ci

- [ ] T014 Verify CT-4-AC-3: Given the export file is generated, When the file is opened, Then it includes columns: date, vendor, amount, category, payment method, receipt ID with proper headers and correct data types (dates as dates, amounts as currency)
  - Obligation ID: test:characterization:CT-4-AC-3 | Type: characterization | Required before: release | Environment: ci

- [ ] T015 Verify CT-4-AC-4: Given the export includes >1000 receipts, When the export is processing, Then a progress indicator displays and updates during generation
  - Obligation ID: test:characterization:CT-4-AC-4 | Type: characterization | Required before: release | Environment: ci

- [ ] T016 Verify CT-4-AC-5: Given the export completes successfully, When the generation finishes, Then the system provides a download link with success confirmation message
  - Obligation ID: test:characterization:CT-4-AC-5 | Type: characterization | Required before: release | Environment: ci

- [ ] T017 Verify CT-4-AC-6: Given no receipts match the selected criteria, When the export is attempted, Then the system displays 'no data available' message and does not generate a file
  - Obligation ID: test:characterization:CT-4-AC-6 | Type: characterization | Required before: release | Environment: ci

- [ ] T018 Verify CT-4-AC-7: Given the exported file is downloaded, When opened in Excel, Then all columns are properly formatted and readable
  - Obligation ID: test:characterization:CT-4-AC-7 | Type: characterization | Required before: release | Environment: ci

- [ ] T019 Verify CT-4-AC-8: Given an export request is submitted, When the export processes up to 10,000 receipts, Then the operation completes within 10 seconds for 95% of requests
  - Obligation ID: test:characterization:CT-4-AC-8 | Type: characterization | Required before: release | Environment: ci

- [ ] T020 Verify CT-4-AC-9: Given an export operation is performed, When it completes, Then all export activities (user, timestamp, filters applied, record count) are logged to the audit trail
  - Obligation ID: test:characterization:CT-4-AC-9 | Type: characterization | Required before: release | Environment: ci

- [ ] T021 Verify IMPACT: Unit tests must cover: date range validation, filter application logic, .xlsx file generation, column header and data type correctness, empty-result handling, and audit log entry creation — targeting >80% coverage per Definition of Done

- [ ] T022 Verify IMPACT: Integration tests must validate end-to-end flow: UI filter selection → backend query via ReceiptRepository → file generation → download link delivery

- [ ] T023 Verify IMPACT: Performance tests must confirm export of 10,000 receipts completes within 10 seconds for 95% of requests, using realistic CarePay dataset volumes

- [ ] T024 Verify IMPACT: Data integrity tests must confirm exported data exactly matches source data in instalment_receipt and the receipts base table (once confirmed), with correct formatting and no data loss

- [ ] T025 Verify IMPACT: Regression tests must confirm existing transactions (CreateReceiptGQL, GetPremiumReceiptsGQL, CreateReceiptResolver, etc.) are unaffected by ReceiptRepository query additions
