-- source_handler_passport: persisted projection of runtime source handler passports
CREATE TABLE source_handler_passport
(
    id              BIGSERIAL PRIMARY KEY,

    source_code     VARCHAR(50) NOT NULL,
    handler_code    VARCHAR(50) NOT NULL,
    delivery_mode   VARCHAR(4)  NOT NULL,
    access_method   VARCHAR(20) NOT NULL,
    registry_status VARCHAR(10) NOT NULL DEFAULT 'REGISTERED',

    CONSTRAINT uq_source_handler_passport_source_handler_code
        UNIQUE (source_code, handler_code),

    CONSTRAINT fk_source_handler_passport_source_passport
        FOREIGN KEY (source_code)
            REFERENCES source_passport (source_code),

    CONSTRAINT chk_source_handler_passport_source_code_pattern
        CHECK (source_code ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_source_handler_passport_handler_code_pattern
        CHECK (handler_code ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_source_handler_passport_delivery_mode_pattern
        CHECK (delivery_mode ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_source_handler_passport_delivery_mode_allowed
        CHECK (delivery_mode IN ('PULL', 'PUSH')),
    CONSTRAINT chk_source_handler_passport_access_method_pattern
        CHECK (access_method ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_source_handler_passport_access_method_allowed
        CHECK (access_method IN ('API_POLL', 'WEBSOCKET', 'FIX_PROTOCOL')),
    CONSTRAINT chk_source_handler_passport_registry_status_pattern
        CHECK (registry_status ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_source_handler_passport_registry_status_allowed
        CHECK (registry_status IN ('REGISTERED', 'RETIRED'))
);
