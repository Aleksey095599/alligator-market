ALTER TABLE source_plan_entry
    DROP CONSTRAINT chk_source_plan_entry_lifecycle_status_allowed;

ALTER TABLE source_plan_entry
    ALTER COLUMN lifecycle_status TYPE VARCHAR(14),
    ALTER COLUMN lifecycle_status SET DEFAULT 'AVAILABLE';

UPDATE source_plan_entry
SET lifecycle_status = 'AVAILABLE'
WHERE lifecycle_status = 'ACTIVE';

UPDATE source_plan_entry
SET lifecycle_status = 'SOURCE_RETIRED'
WHERE lifecycle_status = 'RETIRED';

ALTER TABLE source_plan_entry
    ADD CONSTRAINT chk_source_plan_entry_lifecycle_status_allowed
        CHECK (lifecycle_status IN ('AVAILABLE', 'SOURCE_RETIRED'));
