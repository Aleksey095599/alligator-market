-- capturer_passport: persisted projection of runtime capturer passports
CREATE TABLE capturer_passport
(
    id               BIGSERIAL PRIMARY KEY,

    capturer_code    VARCHAR(50)  NOT NULL,
    display_name     VARCHAR(100) NOT NULL,
    lifecycle_status VARCHAR(7)   NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT uq_capturer_passport_capturer_code
        UNIQUE (capturer_code),
    CONSTRAINT uq_capturer_passport_display_name
        UNIQUE (display_name),

    CONSTRAINT chk_capturer_passport_capturer_code_pattern
        CHECK (capturer_code ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_capturer_passport_display_name_not_blank
        CHECK (length(btrim(display_name)) > 0),
    CONSTRAINT chk_capturer_passport_display_name_no_control_chars
        CHECK (display_name !~ '[[:cntrl:]]'),
    CONSTRAINT chk_capturer_passport_lifecycle_status_pattern
        CHECK (lifecycle_status ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_capturer_passport_lifecycle_status_allowed
        CHECK (lifecycle_status IN ('ACTIVE', 'RETIRED'))
);
