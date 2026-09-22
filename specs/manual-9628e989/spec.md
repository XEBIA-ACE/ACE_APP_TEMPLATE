# Feature Specification: manual-9628e989

## User Scenarios & Testing
- **manual-9628e989**: An Export PDF action is available on the care plan page for users with the care coordinator role
- **manual-9628e989**: The exported PDF contains the patient name, active goals, interventions and the responsible clinician
- **manual-9628e989**: Every export is recorded in the audit log with the exporting user and timestamp

## Functional Requirements
- FR-001: An Export PDF action must be available on the care plan page, specifically limited to users with the care coordinator role.
- FR-002: The exported PDF must include the patient name, all active goals, listed interventions, and identify the responsible clinician.
- FR-003: The system must record each export action in the audit log, capturing the exporting user's identification and the timestamp of the action.

## Success Criteria
- **ac-1**: An Export PDF action is available on the care plan page for users with the care coordinator role
- **ac-2**: The exported PDF contains the patient name, active goals, interventions and the responsible clinician
- **ac-3**: Every export is recorded in the audit log with the exporting user and timestamp
- **IMPACT**: Verify acceptance criterion: An Export PDF action is available on the care plan page for users with the care coordinator role
- **IMPACT**: Verify acceptance criterion: The exported PDF contains the patient name, active goals, interventions and the responsible clinician
- **IMPACT**: Verify acceptance criterion: Every export is recorded in the audit log with the exporting user and timestamp

## Provenance
- **Policy Version**: `ace-spec-format/v1`
- **Accepted Impact Hash**: `sha256:ee89fc440293bfb82d846b0a2d7652c15cd1ebadbdb091712f09b99ca0781bc3`
- **Accepted Impact Revision**: `1`
- **Repository Base Sha**: `11ecc5661862c03867eebf122bf3cfdcf7e7c48b`
- **Determinism Ref**: `f4922aefad42f3de`