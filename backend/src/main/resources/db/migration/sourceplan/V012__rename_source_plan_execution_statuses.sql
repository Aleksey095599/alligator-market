ALTER TABLE source_plan
    DROP CONSTRAINT chk_source_plan_execution_status_allowed;

ALTER TABLE source_plan
    ALTER COLUMN execution_status SET DEFAULT 'AVAILABLE';

UPDATE source_plan
SET execution_status = 'AVAILABLE'
WHERE execution_status = 'EXECUTABLE';

UPDATE source_plan
SET execution_status = 'NO_AVAILABLE_SOURCES'
WHERE execution_status = 'NO_EXECUTABLE_SOURCES';

ALTER TABLE source_plan
    ADD CONSTRAINT chk_source_plan_execution_status_allowed
        CHECK (execution_status IN ('AVAILABLE', 'CAPTURER_RETIRED', 'NO_AVAILABLE_SOURCES'));
