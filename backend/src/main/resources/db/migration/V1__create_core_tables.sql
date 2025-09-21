-- Миграция Flyway: создание основных таблиц для рыночных данных

CREATE TABLE provider_descriptor (
    id                 BIGSERIAL PRIMARY KEY,
    version            BIGINT                  NOT NULL,
    created_timestamp  TIMESTAMPTZ             NOT NULL,
    created_by         VARCHAR(255)            NOT NULL,
    created_via        VARCHAR(255)            NOT NULL,
    updated_timestamp  TIMESTAMPTZ             NOT NULL,
    updated_by         VARCHAR(255)            NOT NULL,
    updated_via        VARCHAR(255)            NOT NULL,
    provider_code      VARCHAR(50)             NOT NULL,
    display_name       VARCHAR(50)             NOT NULL,
    delivery_mode      VARCHAR(10)             NOT NULL,
    access_method      VARCHAR(20)             NOT NULL,
    bulk_subscription  BOOLEAN                 NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_provider_descriptor_provider_code UNIQUE (provider_code),
    CONSTRAINT uq_provider_descriptor_display_name UNIQUE (display_name)
);

CREATE TABLE currency (
    id                 BIGSERIAL PRIMARY KEY,
    version            BIGINT                  NOT NULL,
    created_timestamp  TIMESTAMPTZ             NOT NULL,
    created_by         VARCHAR(255)            NOT NULL,
    created_via        VARCHAR(255)            NOT NULL,
    updated_timestamp  TIMESTAMPTZ             NOT NULL,
    updated_by         VARCHAR(255)            NOT NULL,
    updated_via        VARCHAR(255)            NOT NULL,
    code               VARCHAR(3)              NOT NULL,
    name               VARCHAR(50)             NOT NULL,
    country            VARCHAR(100)            NOT NULL,
    decimal_value      INTEGER                 NOT NULL,
    CONSTRAINT uq_currency_code UNIQUE (code),
    CONSTRAINT uq_currency_name UNIQUE (name),
    CONSTRAINT chk_currency_decimal CHECK (decimal_value BETWEEN 0 AND 10)
);

CREATE TABLE instrument (
    id                 BIGSERIAL PRIMARY KEY,
    version            BIGINT                  NOT NULL,
    created_timestamp  TIMESTAMPTZ             NOT NULL,
    created_by         VARCHAR(255)            NOT NULL,
    created_via        VARCHAR(255)            NOT NULL,
    updated_timestamp  TIMESTAMPTZ             NOT NULL,
    updated_by         VARCHAR(255)            NOT NULL,
    updated_via        VARCHAR(255)            NOT NULL,
    code               VARCHAR(32)             NOT NULL,
    type               VARCHAR(32)             NOT NULL,
    CONSTRAINT uq_instrument_code UNIQUE (code)
);

CREATE TABLE fx_spot (
    id                 BIGINT                  PRIMARY KEY,
    base_currency      VARCHAR(3)              NOT NULL,
    quote_currency     VARCHAR(3)              NOT NULL,
    value_date_code    VARCHAR(4)              NOT NULL,
    quote_decimal      INTEGER                 NOT NULL,
    CONSTRAINT fk_fx_spot_instrument FOREIGN KEY (id) REFERENCES instrument (id) ON DELETE CASCADE,
    CONSTRAINT fk_fx_spot_base FOREIGN KEY (base_currency) REFERENCES currency (code),
    CONSTRAINT fk_fx_spot_quote FOREIGN KEY (quote_currency) REFERENCES currency (code),
    CONSTRAINT chk_fx_spot_quote_decimal CHECK (quote_decimal BETWEEN 0 AND 10)
);

CREATE INDEX idx_fx_spot_base_currency ON fx_spot (base_currency);
CREATE INDEX idx_fx_spot_quote_currency ON fx_spot (quote_currency);
