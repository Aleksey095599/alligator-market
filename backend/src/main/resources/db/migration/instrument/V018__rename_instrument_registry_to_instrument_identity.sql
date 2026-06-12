ALTER TABLE instrument_registry
    RENAME TO instrument_identity;

ALTER TABLE instrument_identity
    RENAME CONSTRAINT instrument_registry_pkey TO instrument_identity_pkey;

ALTER TABLE instrument_identity
    RENAME CONSTRAINT chk_instrument_registry_instrument_code_pattern
        TO chk_instrument_identity_instrument_code_pattern;
