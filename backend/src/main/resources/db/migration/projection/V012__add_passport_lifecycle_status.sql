-- Passport projections keep retired rows for referential integrity.
ALTER TABLE provider_passport
    ADD COLUMN lifecycle_status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE';

ALTER TABLE provider_passport
    ADD CONSTRAINT chk_provider_passport_lifecycle_status
        CHECK (lifecycle_status IN ('ACTIVE', 'RETIRED'));

ALTER TABLE capture_process_passport
    ADD COLUMN lifecycle_status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE';

ALTER TABLE capture_process_passport
    ADD CONSTRAINT chk_capture_process_passport_lifecycle_status
        CHECK (lifecycle_status IN ('ACTIVE', 'RETIRED'));
