ALTER TABLE source_passport
    RENAME COLUMN lifecycle_status TO registry_status;

ALTER TABLE source_passport
    RENAME CONSTRAINT chk_source_passport_lifecycle_status_pattern
        TO chk_source_passport_registry_status_pattern;

ALTER TABLE source_passport
    RENAME CONSTRAINT chk_source_passport_lifecycle_status_allowed
        TO chk_source_passport_registry_status_allowed;

ALTER TABLE capturer_passport
    RENAME COLUMN lifecycle_status TO registry_status;

ALTER TABLE capturer_passport
    RENAME CONSTRAINT chk_capturer_passport_lifecycle_status_pattern
        TO chk_capturer_passport_registry_status_pattern;

ALTER TABLE capturer_passport
    RENAME CONSTRAINT chk_capturer_passport_lifecycle_status_allowed
        TO chk_capturer_passport_registry_status_allowed;
