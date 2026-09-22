# Feature Specification: manual-b68bb6ff

## User Scenarios & Testing
- **manual-b68bb6ff**: Care coordinators can flag claims as disputed from the claim detail page.
- **manual-b68bb6ff**: Dispute reasons can be selected from a predefined list.
- **manual-b68bb6ff**: An optional free-text note can be added to the dispute.
- **manual-b68bb6ff**: The coordinator raising the dispute is recorded with the dispute details.
- **manual-b68bb6ff**: Disputed claims are excluded from the automatic payout batch until resolved.
- **manual-b68bb6ff**: Patients receive a notification that their claim is under review.

## Functional Requirements
- FR-001: The system must allow care coordinators to flag claims as disputed directly from the claim detail page.
- FR-002: The system must provide a predefined list of dispute reasons that users can select from when flagging a claim as disputed.
- FR-003: The system must allow users to add an optional free-text note when flagging a claim as disputed.
- FR-004: The system must record the identity of the coordinator who flags a dispute, alongside the dispute details.
- FR-005: The system must ensure that any claim flagged as disputed is excluded from the automatic payout batch until it is resolved.
- FR-006: The system must send a notification to the patient informing them that their claim is under review when it gets flagged as disputed.

## Success Criteria
- **ac-1**: Care coordinators can flag claims as disputed from the claim detail page.
- **ac-2**: Dispute reasons can be selected from a predefined list.
- **ac-3**: An optional free-text note can be added to the dispute.
- **ac-4**: The coordinator raising the dispute is recorded with the dispute details.
- **ac-5**: Disputed claims are excluded from the automatic payout batch until resolved.
- **ac-6**: Patients receive a notification that their claim is under review.

## Provenance
- **Policy Version**: `ace-spec-format/v1`
- **Accepted Impact Hash**: `sha256:ee2ac08b18dd58a4911f15312ae8affa0b281440bee32390a81a4b5498646fcd`
- **Accepted Impact Revision**: `1`
- **Repository Base Sha**: `11ecc5661862c03867eebf122bf3cfdcf7e7c48b`
- **Determinism Ref**: `f4922aefad42f3de`