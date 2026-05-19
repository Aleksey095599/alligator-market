ALTER TABLE source_passport
    DROP CONSTRAINT chk_source_passport_delivery_mode_pattern,
    DROP CONSTRAINT chk_source_passport_delivery_mode_allowed,
    DROP CONSTRAINT chk_source_passport_access_method_pattern,
    DROP CONSTRAINT chk_source_passport_access_method_allowed;

ALTER TABLE source_passport
    DROP COLUMN delivery_mode,
    DROP COLUMN access_method;
