ALTER TABLE source_plan
    ADD COLUMN execution_status VARCHAR(32) NOT NULL DEFAULT 'EXECUTABLE';

UPDATE source_plan
SET execution_status = 'EXECUTABLE';

ALTER TABLE source_plan
    ADD CONSTRAINT chk_source_plan_execution_status
        CHECK (execution_status IN ('EXECUTABLE', 'CAPTURER_RETIRED', 'NO_EXECUTABLE_SOURCES'));

CREATE OR REPLACE FUNCTION refresh_source_plan_execution_status()
    RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    WITH calculated AS (
        SELECT sp.capturer_code,
               sp.instrument_code,
               CASE
                   WHEN NOT EXISTS (
                       SELECT 1
                       FROM market_data_capturer_passport mdc
                       WHERE mdc.capturer_code = sp.capturer_code
                         AND mdc.lifecycle_status = 'ACTIVE'
                   ) THEN 'CAPTURER_RETIRED'
                   WHEN NOT EXISTS (
                       SELECT 1
                       FROM source_plan_entry spe
                                JOIN market_data_source_passport mds
                                     ON mds.source_code = spe.source_code
                       WHERE spe.capturer_code = sp.capturer_code
                         AND spe.instrument_code = sp.instrument_code
                         AND spe.lifecycle_status = 'ACTIVE'
                         AND mds.lifecycle_status = 'ACTIVE'
                   ) THEN 'NO_EXECUTABLE_SOURCES'
                   ELSE 'EXECUTABLE'
                   END AS execution_status
        FROM source_plan sp
    )
    UPDATE source_plan sp
    SET execution_status = calculated.execution_status
    FROM calculated
    WHERE sp.capturer_code = calculated.capturer_code
      AND sp.instrument_code = calculated.instrument_code
      AND sp.execution_status IS DISTINCT FROM calculated.execution_status;

    RETURN NULL;
END;
$$;

CREATE TRIGGER trg_refresh_source_plan_execution_status_after_source_plan_insert
    AFTER INSERT ON source_plan
    FOR EACH STATEMENT
EXECUTE FUNCTION refresh_source_plan_execution_status();

CREATE TRIGGER trg_refresh_source_plan_execution_status_after_entry_change
    AFTER INSERT OR UPDATE OR DELETE ON source_plan_entry
    FOR EACH STATEMENT
EXECUTE FUNCTION refresh_source_plan_execution_status();

CREATE TRIGGER trg_refresh_source_plan_execution_status_after_capturer_change
    AFTER INSERT OR UPDATE OR DELETE ON market_data_capturer_passport
    FOR EACH STATEMENT
EXECUTE FUNCTION refresh_source_plan_execution_status();

CREATE TRIGGER trg_refresh_source_plan_execution_status_after_source_change
    AFTER INSERT OR UPDATE OR DELETE ON market_data_source_passport
    FOR EACH STATEMENT
EXECUTE FUNCTION refresh_source_plan_execution_status();
