ALTER TABLE source_passport
    DROP CONSTRAINT chk_source_passport_lifecycle_status_allowed;

ALTER TABLE source_passport
    ALTER COLUMN lifecycle_status TYPE VARCHAR(10),
    ALTER COLUMN lifecycle_status SET DEFAULT 'REGISTERED';

UPDATE source_passport
SET lifecycle_status = 'REGISTERED'
WHERE lifecycle_status = 'ACTIVE';

ALTER TABLE source_passport
    ADD CONSTRAINT chk_source_passport_lifecycle_status_allowed
        CHECK (lifecycle_status IN ('REGISTERED', 'RETIRED'));

ALTER TABLE capturer_passport
    DROP CONSTRAINT chk_capturer_passport_lifecycle_status_allowed;

ALTER TABLE capturer_passport
    ALTER COLUMN lifecycle_status TYPE VARCHAR(10),
    ALTER COLUMN lifecycle_status SET DEFAULT 'REGISTERED';

UPDATE capturer_passport
SET lifecycle_status = 'REGISTERED'
WHERE lifecycle_status = 'ACTIVE';

ALTER TABLE capturer_passport
    ADD CONSTRAINT chk_capturer_passport_lifecycle_status_allowed
        CHECK (lifecycle_status IN ('REGISTERED', 'RETIRED'));
