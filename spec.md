# Feature Specification: CT-4

## Story

**Title:** Export Receipts with Date Range and Filters

**Summary:** As a finance user, I want to export receipts to Excel with date range and filters, so that I can analyze and report on receipt data in my preferred spreadsheet tool.

**User Story:** As a Finance Manager, I want to export receipt data to Excel with date range and filter options, so that I can analyze and share transaction records offline in a universally accessible format.

**Jira Reference:** CT-4 | https://xebia-ace-team.atlassian.net/browse/CT-4

**Status:** To Do | **Label:** must_have

---

## Behavior Summary

A Finance Manager on the receipts dashboard can select a date range and optional filters (category, vendor, status), then click 'Export to Excel' to trigger a validated export. The system generates a .xlsx file containing only the matching receipts, with the correct columns and data types, delivers a download link on success, shows a 'no data available' message when no records match, displays a progress indicator for exports exceeding 1,000 receipts, completes exports of up to 10,000 receipts within 10 seconds for 95% of requests, and logs all export activity to an audit trail.

---

## Acceptance Criteria & Functional Requirements

### CT-4-AC-1
**Scenario:** Given a user is on the receipts dashboard, When they select a date range and optional filters (category, vendor, status) and click 'Export to Excel', Then the system validates the selection and initiates export

**Functional Requirement:** The receipts dashboard must expose an 'Export to Excel' control that accepts a mandatory date range input and optional filter inputs for category, vendor, and status. Before initiating the export, the system must validate that the date range is present and well-formed; validation failure must prevent the export from being initiated and must surface an appropriate error to the user.

**Clause Hash:** sha256:05bf5c9ada7c2001360961adee654dc9bfc63fbf01ee07c9de83e832ca367b29
**Trust Status:** OBSERVED_UNVERIFIED | **Tier:** B | **Load Bearing:** true | **Origin:** brownfield
**Evidence Refs:** CT-4-AC-1:grounding:28037901f7b2 (code_reference, VERIFIED, producer: cast, source_ref: f4922aefad42f3de)
**Test Obligation:** test:characterization:CT-4-AC-1 — required_before: release, environment: ci
**CAST Traceability:** partially_mapped
- Components: ReceiptRepository (182097), InstalmentReceiptSearch (171699), ReceiptingService (171785)
- Evidence: InstalmentReceiptSearch (171699) — existing search/filter class on Receipt (171795); ReceiptRepository (182097) — data access interface callable with filter parameters; instalment_receipt.status (1100792) — status filter column confirmed in DB schema
- Gap: No CAST evidence of an existing UI export control, date-range validation component, or export-initiation endpoint. Vendor and category filter columns not confirmed in CAST DB inventory. New components required.
**Review Date:** 2026-10-19 | **Reviewed by:** a4b8c408-30d1-709a-c805-c7503d7b2024 | **Action:** approve

---

### CT-4-AC-2
**Scenario:** Given the export is initiated, When the export generates a .xlsx file, Then it contains only receipts within the selected date range and matching all applied filters

**Functional Requirement:** When an export is initiated, the system must query receipt data using the exact date range and all applied filter values (category, vendor, status) as constraints, such that the generated .xlsx file contains only receipts that fall within the selected date range AND match every applied filter. No receipts outside the selected criteria may appear in the file.

**Clause Hash:** sha256:71621a7ff77fdf323d6d21fae5d3df06c965eb991f60c901c5abde710f3e215f
**Trust Status:** OBSERVED_UNVERIFIED | **Tier:** B | **Load Bearing:** true | **Origin:** brownfield
**Evidence Refs:** CT-4-AC-2:grounding:28037901f7b2 (code_reference, VERIFIED, producer: cast, source_ref: f4922aefad42f3de)
**Test Obligation:** test:characterization:CT-4-AC-2 — required_before: release, environment: ci
**CAST Traceability:** partially_mapped
- Components: ReceiptRepository (182097), Receipt (171795), InstalmentReceiptSearch (171699)
- Evidence: InstalmentReceiptSearch (171699) — filter logic class; ReceiptRepository (182097) — interface for filtered data retrieval; public.instalment_receipt (1100473) — confirmed table with status, date_created, amount columns
- Gap: No CAST evidence of an existing .xlsx generation pipeline or filter-to-query binding for date range. Receipts base table (with vendor, category fields) not found in CAST DB inventory. Excel library not present in CAST scan.
**Review Date:** 2026-10-19 | **Reviewed by:** a4b8c408-30d1-709a-c805-c7503d7b2024 | **Action:** approve

---

### CT-4-AC-3
**Scenario:** Given the export file is generated, When the file is opened, Then it includes columns: date, vendor, amount, category, payment method, receipt ID with proper headers and correct data types (dates as dates, amounts as currency)

**Functional Requirement:** The generated .xlsx file must contain exactly six columns with the following headers and data types: 'Date' (Excel date type), 'Vendor' (text), 'Amount' (Excel currency type), 'Category' (text), 'Payment Method' (text), and 'Receipt ID' (text or numeric identifier). Column headers must appear in the first row. Each data cell must use the correct Excel cell type corresponding to its column definition (dates rendered as date values, amounts rendered as currency values).

**Clause Hash:** sha256:57218033c3d4f8295c7a0154a253713a3a10a4c62ed378193acad261891d859a
**Trust Status:** OBSERVED_UNVERIFIED | **Tier:** B | **Load Bearing:** true | **Origin:** brownfield
**Evidence Refs:** CT-4-AC-3:grounding:28037901f7b2 (code_reference, VERIFIED, producer: cast, source_ref: f4922aefad42f3de)
**Test Obligation:** test:characterization:CT-4-AC-3 — required_before: release, environment: ci
**CAST Traceability:** partially_mapped
- Components: Receipt (171795), ReceiptMapper (182108), PaymentDetails (177416), public.instalment_receipt (1100473)
- Evidence: PaymentDetails (177416) — downstream callee of Receipt; provides payment method data; ReceiptMapper (182108) — mapping interface for receipt fields; public.instalment_receipt.amount (1103337) — decimal(23,8) confirms currency-precision amount field; public.instalment_receipt.date_created (1104514) — datetime column confirms date field
- Gap: Vendor, category, and receipt ID columns not confirmed in CAST DB inventory (receipts base table not found). Excel column header and data-type formatting logic (dates as dates, amounts as currency) requires net-new implementation not present in CAST facts.
**Review Date:** 2026-10-19 | **Reviewed by:** a4b8c408-30d1-709a-c805-c7503d7b2024 | **Action:** approve

---

### CT-4-AC-4
**Scenario:** Given the export includes >1000 receipts, When the export is processing, Then a progress indicator displays and updates during generation

**Functional Requirement:** When an export request results in more than 1,000 receipts being processed, the system must display a progress indicator to the user. The progress indicator must update during the generation process (i.e., it must not remain static for the full duration of the export). The progress indicator must be accessible to screen readers and keyboard navigation.

**Clause Hash:** sha256:a2e0615d2152850379a1b73f7055577eead6fa9a28c7700b11160ddde7a3d8a8
**Trust Status:** OBSERVED_UNVERIFIED | **Tier:** B | **Load Bearing:** true | **Origin:** brownfield
**Evidence Refs:** CT-4-AC-4:grounding:28037901f7b2 (code_reference, VERIFIED, producer: cast, source_ref: f4922aefad42f3de)
**Test Obligation:** test:characterization:CT-4-AC-4 — required_before: release, environment: ci
**CAST Traceability:** unmapped
- Components: none
- Gap: No CAST structural evidence of a progress indicator component, async export job, or streaming mechanism. This is entirely net-new UI and backend infrastructure not visible in the current CAST scan of CarePay.
**Review Date:** 2026-10-19 | **Reviewed by:** a4b8c408-30d1-709a-c805-c7503d7b2024 | **Action:** approve

---

### CT-4-AC-5
**Scenario:** Given the export completes successfully, When the generation finishes, Then the system provides a download link with success confirmation message

**Functional Requirement:** When an export completes successfully, the system must present the user with a success confirmation message and a download link for the generated .xlsx file. Both the success message and the download link must be rendered in the UI following generation completion.

**Clause Hash:** sha256:bf57044f7d6a0c32b233315b8b1bde2feb16219f807186215b183fadc034a3ce
**Trust Status:** OBSERVED_UNVERIFIED | **Tier:** B | **Load Bearing:** true | **Origin:** brownfield
**Evidence Refs:** CT-4-AC-5:grounding:28037901f7b2 (code_reference, VERIFIED, producer: cast, source_ref: f4922aefad42f3de)
**Test Obligation:** test:characterization:CT-4-AC-5 — required_before: release, environment: ci
**CAST Traceability:** unmapped
- Components: none
- Gap: No CAST structural evidence of a download link generation service, file storage service, or success confirmation messaging component. These are net-new capabilities not present in the CAST inventory.
**Review Date:** 2026-10-19 | **Reviewed by:** a4b8c408-30d1-709a-c805-c7503d7b2024 | **Action:** approve

---

### CT-4-AC-6
**Scenario:** Given no receipts match the selected criteria, When the export is attempted, Then the system displays 'no data available' message and does not generate a file

**Functional Requirement:** When an export is attempted and no receipts match the selected date range and filters, the system must display a 'no data available' message to the user and must not generate or store a .xlsx file. The 'no data available' message must be communicated to assistive technologies.

**Clause Hash:** sha256:d3d8410578de118fead9792ead611754449db0a562b6eed3528a96272eac792b
**Trust Status:** OBSERVED_UNVERIFIED | **Tier:** B | **Load Bearing:** true | **Origin:** brownfield
**Evidence Refs:** CT-4-AC-6:grounding:28037901f7b2 (code_reference, VERIFIED, producer: cast, source_ref: f4922aefad42f3de)
**Test Obligation:** test:characterization:CT-4-AC-6 — required_before: release, environment: ci
**CAST Traceability:** partially_mapped
- Components: ReceiptRepository (182097), ReceiptingService (171785)
- Evidence: ReceiptRepository (182097) — can return empty result sets for unmatched filter queries; ReceiptingService (171785) — service layer where empty-result handling logic would reside
- Gap: No CAST evidence of an existing empty-result UI message component or guard logic that suppresses file generation on zero results. This behavior must be implemented as new logic.
**Review Date:** 2026-10-19 | **Reviewed by:** a4b8c408-30d1-709a-c805-c7503d7b2024 | **Action:** approve

---

### CT-4-AC-7
**Scenario:** Given the exported file is downloaded, When opened in Excel, Then all columns are properly formatted and readable

**Functional Requirement:** When the exported .xlsx file is opened in Excel, all six required columns (date, vendor, amount, category, payment method, receipt ID) must be properly formatted and readable. 'Properly formatted' means: date columns render as recognisable date values, amount columns render as currency values, text columns are legible, and no column contains corrupted or unreadable data.

**Clause Hash:** sha256:b874811b0896002fd97505f608c861e33b24da8032980161dbc2043b9b8cd1c0
**Trust Status:** OBSERVED_UNVERIFIED | **Tier:** B | **Load Bearing:** true | **Origin:** brownfield
**Evidence Refs:** CT-4-AC-7:grounding:28037901f7b2 (code_reference, VERIFIED, producer: cast, source_ref: f4922aefad42f3de)
**Test Obligation:** test:characterization:CT-4-AC-7 — required_before: release, environment: ci
**CAST Traceability:** partially_mapped
- Components: ReceiptMapper (182108), PaymentDetails (177416)
- Evidence: ReceiptMapper (182108) — field mapping interface relevant to column formatting; public.instalment_receipt.amount (1103337) decimal(23,8) — precision supports currency formatting; public.instalment_receipt.date_created (1104514) datetime — supports date formatting
- Gap: Excel-specific column formatting (cell styles, data types in .xlsx) requires a generation library not present in CAST inventory. Full column set (vendor, category, receipt ID) not confirmed in DB schema.
**Review Date:** 2026-10-19 | **Reviewed by:** a4b8c408-30d1-709a-c805-c7503d7b2024 | **Action:** approve

---

### CT-4-AC-8
**Scenario:** Given an export request is submitted, When the export processes up to 10,000 receipts, Then the operation completes within 10 seconds for 95% of requests

**Functional Requirement:** The export operation must complete within 10 seconds for at least 95% of requests when processing datasets of up to 10,000 receipts. This performance threshold applies end-to-end from export initiation to file availability.

**Clause Hash:** sha256:8226e299f3bfc03f7cac5dc075182a53fc50ccc02162103d70453b671f55a80c
**Trust Status:** OBSERVED_UNVERIFIED | **Tier:** B | **Load Bearing:** true | **Origin:** brownfield
**Evidence Refs:** CT-4-AC-8:grounding:28037901f7b2 (code_reference, VERIFIED, producer: cast, source_ref: f4922aefad42f3de)
**Test Obligation:** test:characterization:CT-4-AC-8 — required_before: release, environment: ci
**CAST Traceability:** unmapped
- Components: none
- Gap: No CAST structural evidence of performance optimization patterns (pagination, streaming, async processing) for large receipt exports. No performance benchmarks or load-handling infrastructure visible in CAST facts for the export path. Performance validation requires new implementation and testing.
**Review Date:** 2026-10-19 | **Reviewed by:** a4b8c408-30d1-709a-c805-c7503d7b2024 | **Action:** approve

---

### CT-4-AC-9
**Scenario:** Given an export operation is performed, When it completes, Then all export activities (user, timestamp, filters applied, record count) are logged to the audit trail

**Functional Requirement:** Upon completion of any export operation (whether successful, empty-result, or otherwise), the system must write an audit log entry that captures at minimum: the identity of the user who initiated the export, the timestamp of the operation, the filter parameters applied (date range, category, vendor, status), and the count of records included in the export.

**Clause Hash:** sha256:4d855c8e5ac23af411c8ff7015d2454a4f1003d138abbfe7cd30f07f8b3c5c8e
**Trust Status:** OBSERVED_UNVERIFIED | **Tier:** B | **Load Bearing:** true | **Origin:** brownfield
**Evidence Refs:** CT-4-AC-9:grounding:28037901f7b2 (code_reference, VERIFIED, producer: cast, source_ref: f4922aefad42f3de)
**Test Obligation:** test:characterization:CT-4-AC-9 — required_before: release, environment: ci
**CAST Traceability:** unmapped
- Components: none
- Gap: No audit log table, audit service, or audit-writing component identified in CAST structural facts for CarePay. The instalment_receipt table contains created_by, modified_by, deleted_by audit columns but these are record-level fields, not export-operation audit trail entries. A dedicated audit logging mechanism for export operations must be built from scratch.
**Review Date:** 2026-10-19 | **Reviewed by:** a4b8c408-30d1-709a-c805-c7503d7b2024 | **Action:** approve

---

## Gap Disclosures

**Accepted with gaps:** true
**Accepted at:** 2026-09-19T11:46:35.917715+00:00 | **Accepted by:** a4b8c408-30d1-709a-c805-c7503d7b2024

**Blocking reasons carried to spec:**
- Requirement traceability incomplete for CT-4-AC-1, CT-4-AC-2, CT-4-AC-3, CT-4-AC-4, CT-4-AC-5, CT-4-AC-6, CT-4-AC-7, CT-4-AC-8, CT-4-AC-9

**Evidence gaps (from accepted impact analysis):**
- Receipts base database table not found in CAST inventory — vendor, category, receipt ID column availability unconfirmed
- Transaction call graphs unavailable for all 5 expanded transactions (API fetch errors) — full call chain depth cannot be confirmed
- No audit log table or service identified in CAST structural facts — CT-4-AC-9 implementation evidence is absent
- No Excel export service or file storage service exists in CAST inventory — these are net-new components with no structural baseline
- Level-2+ transitive callers returned 0 results — may reflect CAST scan depth limit rather than true isolation; cannot confirm absence of deeper dependencies
- API endpoint inventory returned errors for 3 of 4 calls — full REST/GraphQL surface area of receipt-related endpoints is not confirmed

---

## Security Obligations

- Export endpoint must enforce authorization checks — only users with appropriate permissions may trigger export (assumption stated in story but no CAST enforcement evidence found)
- Exported .xlsx files containing financial data must be secured in temporary storage and cleaned up after download or retention period expiry to prevent unauthorized access
- Filter parameters (date range, category, vendor, status) must be validated and sanitized server-side to prevent injection into repository queries
- Audit trail must capture user identity, timestamp, filters applied, and record count for every export operation (CT-4-AC-9)
- Validate credential and personal-data handling against the story's acceptance criteria

## Accessibility Obligations

- Progress indicator for large exports (>1000 receipts) must be accessible to screen readers and keyboard navigation (CT-4-AC-4)
- Success confirmation message and download link must meet accessibility standards for finance user personas (CT-4-AC-5)
- 'No data available' message must be clearly communicated to assistive technologies (CT-4-AC-6)
- Export controls (date range picker, filter dropdowns, Export button) on the receipts dashboard must be keyboard-navigable and labeled per accessibility standards

---

## Provenance

- **Policy Version:** `ace-spec-format/v1`
- **Accepted Impact Hash:** `sha256:37892ee1fbd7d3130c9a610857a5370fd79d824e221b887bb2820fd88d712057`
- **Accepted Impact Revision:** `1`
- **Jira Snapshot Hash:** `33fd0a33b32a34d2cf28c9056f7ca0fb10ae1c22fe46fba7914d[REDACTED-PHONE]ccdf`
- **Repository Base SHA:** `11ecc5661862c03867eebf122bf3cfdcf7e7c48b`
- **Determinism Ref (CAST Fingerprint):** `f4922aefad42f3de`
- **CAST Application:** CarePay
- **Focus Object:** Receipt (171795) — Java Class — `com.carepay.premiumcalculation.domain.entities.receipts.Receipt`
- **Source File:** `C:\cast-node\common-data\upload\CarePay\main_sources\premium-calculation-invoicing\src\main\java\com\carepay\premiumcalculation\domain\entities\receipts\Receipt.java`
