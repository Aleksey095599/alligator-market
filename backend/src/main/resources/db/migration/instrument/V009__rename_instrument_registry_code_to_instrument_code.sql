ALTER TABLE instrument_registry
    RENAME COLUMN code TO instrument_code;

ALTER TABLE instrument_registry
    RENAME CONSTRAINT chk_instrument_registry_code_pattern
        TO chk_instrument_registry_instrument_code_pattern;
