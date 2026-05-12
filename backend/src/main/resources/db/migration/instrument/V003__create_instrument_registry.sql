-- instrument_registry: registered financial instrument codes
CREATE TABLE instrument_registry
(
    instrument_code VARCHAR(50) PRIMARY KEY,

    CONSTRAINT chk_instrument_registry_instrument_code_pattern
        CHECK (instrument_code ~ '^[A-Z0-9_]+$')
);
