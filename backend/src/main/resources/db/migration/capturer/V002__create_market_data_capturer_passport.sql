-- market_data_capturer_passport: materialized view of market data capturer passports
CREATE TABLE market_data_capturer_passport
(
    id               BIGSERIAL PRIMARY KEY,

    capturer_code    VARCHAR(50)  NOT NULL,
    display_name     VARCHAR(160) NOT NULL,
    lifecycle_status VARCHAR(16)  NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT uq_market_data_capturer_code
        UNIQUE (capturer_code),
    CONSTRAINT uq_market_data_capturer_display_name
        UNIQUE (display_name),

    CONSTRAINT chk_market_data_capturer_code_pattern
        CHECK (capturer_code ~ '^[A-Z0-9_.-]+$'),
    CONSTRAINT chk_market_data_capturer_code_not_blank
        CHECK (length(btrim(capturer_code)) > 0),
    CONSTRAINT chk_market_data_capturer_display_name_not_blank
        CHECK (length(btrim(display_name)) > 0),
    CONSTRAINT chk_market_data_capturer_passport_lifecycle_status
        CHECK (lifecycle_status IN ('ACTIVE', 'RETIRED'))
);
