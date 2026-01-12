CREATE TABLE instrument (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(32) NOT NULL,
    symbol VARCHAR(32) NOT NULL,
    type VARCHAR(32) NOT NULL,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL,
    CONSTRAINT uq_instrument_code UNIQUE (code)
);

CREATE INDEX idx_instrument_type ON instrument (type);

CREATE TABLE currency (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(3) NOT NULL,
    name VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    fraction_digits INTEGER NOT NULL DEFAULT 2,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL,
    CONSTRAINT uq_currency_code UNIQUE (code),
    CONSTRAINT uq_currency_name UNIQUE (name),
    CONSTRAINT chk_currency_code_pattern CHECK (code ~ '^[A-Z]{3}$'),
    CONSTRAINT chk_currency_fraction_digits CHECK (fraction_digits BETWEEN 0 AND 10)
);

CREATE TABLE fx_spot (
    id BIGINT PRIMARY KEY,
    base_currency VARCHAR(3) NOT NULL,
    quote_currency VARCHAR(3) NOT NULL,
    tenor VARCHAR(4) NOT NULL,
    quote_fraction_digits INTEGER NOT NULL DEFAULT 4,
    CONSTRAINT fk_fx_spot_instrument FOREIGN KEY (id) REFERENCES instrument (id),
    CONSTRAINT fk_fx_spot_base FOREIGN KEY (base_currency) REFERENCES currency (code),
    CONSTRAINT fk_fx_spot_quote FOREIGN KEY (quote_currency) REFERENCES currency (code),
    CONSTRAINT uq_fx_spot_pair_tenor UNIQUE (base_currency, quote_currency, tenor),
    CONSTRAINT chk_fx_spot_base_quote_diff CHECK (base_currency <> quote_currency),
    CONSTRAINT chk_fx_spot_digits_range CHECK (quote_fraction_digits BETWEEN 0 AND 10),
    CONSTRAINT chk_fx_spot_tenor_allowed CHECK (tenor IN ('TOD', 'TOM', 'SPOT'))
);

CREATE INDEX idx_fx_spot_base ON fx_spot (base_currency);
CREATE INDEX idx_fx_spot_quote ON fx_spot (quote_currency);

CREATE TABLE market_data_provider (
    id BIGSERIAL PRIMARY KEY,
    provider_code VARCHAR(50) NOT NULL,
    display_name VARCHAR(50) NOT NULL,
    delivery_mode VARCHAR(10) NOT NULL,
    access_method VARCHAR(20) NOT NULL,
    bulk_subscription BOOLEAN NOT NULL,
    min_update_interval_seconds BIGINT NOT NULL,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL,
    CONSTRAINT uq_provider_code UNIQUE (provider_code),
    CONSTRAINT chk_provider_min_update_interval CHECK (min_update_interval_seconds >= 1),
    CONSTRAINT chk_provider_code_pattern CHECK (provider_code ~ '^[A-Z0-9_.-]+$'),
    CONSTRAINT chk_provider_delivery_mode_allowed CHECK (delivery_mode IN ('PULL', 'PUSH')),
    CONSTRAINT chk_provider_access_method_allowed CHECK (access_method IN ('API_POLL', 'WEBSOCKET', 'FIX_PROTOCOL'))
);

CREATE TABLE instrument_feed_config (
    id BIGSERIAL PRIMARY KEY,
    instrument_id BIGINT NOT NULL,
    provider_code VARCHAR(50) NOT NULL,
    feed_role VARCHAR(16) NOT NULL,
    enabled BOOLEAN NOT NULL,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMPTZ NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL,
    CONSTRAINT fk_ifc_instrument FOREIGN KEY (instrument_id) REFERENCES instrument (id),
    CONSTRAINT uq_ifc_instrument_feed_role UNIQUE (instrument_id, feed_role),
    CONSTRAINT uq_ifc_instrument_provider_code UNIQUE (instrument_id, provider_code),
    CONSTRAINT chk_ifc_provider_code CHECK (
        provider_code = upper(provider_code)
        AND provider_code ~ '^[A-Z0-9_.-]+$'
        AND feed_role IN ('PRIMARY', 'SECONDARY')
    )
);

CREATE INDEX idx_ifc_enabled ON instrument_feed_config (enabled);
