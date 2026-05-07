-- capture_process_passport: materialized view of market data capture process passports
CREATE TABLE capture_process_passport
(
    id                   BIGSERIAL PRIMARY KEY,

    capture_process_code VARCHAR(128) NOT NULL,
    display_name         VARCHAR(160) NOT NULL,
    lifecycle_status     VARCHAR(16)  NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT uq_capture_process_code UNIQUE (capture_process_code),

    CONSTRAINT chk_capture_process_code_pattern
        CHECK (capture_process_code ~ '^[A-Z0-9_.-]+$'),
    CONSTRAINT chk_capture_process_code_not_blank
        CHECK (length(btrim(capture_process_code)) > 0),
    CONSTRAINT chk_capture_process_display_name_not_blank
        CHECK (length(btrim(display_name)) > 0),
    CONSTRAINT chk_capture_process_passport_lifecycle_status
        CHECK (lifecycle_status IN ('ACTIVE', 'RETIRED'))
);
