-- source_passport: persisted projection of runtime source passports
CREATE TABLE source_passport
(
    id               BIGSERIAL PRIMARY KEY,

    source_code      VARCHAR(50)  NOT NULL,
    display_name     VARCHAR(100) NOT NULL,
    delivery_mode    VARCHAR(4)   NOT NULL,
    access_method    VARCHAR(20)  NOT NULL,
    lifecycle_status VARCHAR(7)   NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT uq_source_passport_source_code
        UNIQUE (source_code),

    CONSTRAINT chk_source_passport_source_code_pattern
        CHECK (source_code ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_source_passport_display_name_not_blank
        CHECK (length(btrim(display_name)) > 0),
    CONSTRAINT chk_source_passport_display_name_no_control_chars
        CHECK (display_name !~ '[[:cntrl:]]'),
    CONSTRAINT chk_source_passport_delivery_mode_pattern
        CHECK (delivery_mode ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_source_passport_delivery_mode_allowed
        CHECK (delivery_mode IN ('PULL', 'PUSH')),
    CONSTRAINT chk_source_passport_access_method_pattern
        CHECK (access_method ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_source_passport_access_method_allowed
        CHECK (access_method IN ('API_POLL', 'WEBSOCKET', 'FIX_PROTOCOL')),
    CONSTRAINT chk_source_passport_lifecycle_status_pattern
        CHECK (lifecycle_status ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_source_passport_lifecycle_status_allowed
        CHECK (lifecycle_status IN ('ACTIVE', 'RETIRED'))
);
