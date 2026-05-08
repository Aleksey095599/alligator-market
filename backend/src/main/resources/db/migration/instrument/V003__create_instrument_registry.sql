-- instrument_registry: registered financial instrument codes
CREATE TABLE instrument_registry
(
    code VARCHAR(50) PRIMARY KEY,

    CONSTRAINT chk_instrument_registry_code_pattern
        CHECK (code ~ '^[A-Z0-9_]+$')
);
