-- market_data_source_passport: materialized view of runtime market data source passports
CREATE TABLE market_data_source_passport
(
    id                BIGSERIAL PRIMARY KEY,

    source_code       VARCHAR(50)  NOT NULL,

    display_name      VARCHAR(50)  NOT NULL,
    delivery_mode     VARCHAR(10)  NOT NULL,
    access_method     VARCHAR(20)  NOT NULL,
    bulk_subscription BOOLEAN      NOT NULL,
    lifecycle_status  VARCHAR(16)  NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT uq_market_data_source_passport_source_code
        UNIQUE (source_code),

    CONSTRAINT chk_market_data_source_passport_source_code_pattern
        CHECK (source_code ~ '^[A-Z0-9_.-]+$'),
    CONSTRAINT chk_market_data_source_passport_source_code_not_blank
        CHECK (length(btrim(source_code)) > 0),
    CONSTRAINT chk_market_data_source_passport_display_name_not_blank
        CHECK (length(btrim(display_name)) > 0),
    CONSTRAINT chk_market_data_source_passport_delivery_mode_allowed
        CHECK (delivery_mode IN ('PULL', 'PUSH')),
    CONSTRAINT chk_market_data_source_passport_access_method_allowed
        CHECK (access_method IN ('API_POLL', 'WEBSOCKET', 'FIX_PROTOCOL')),
    CONSTRAINT chk_market_data_source_passport_lifecycle_status
        CHECK (lifecycle_status IN ('ACTIVE', 'RETIRED'))
);
