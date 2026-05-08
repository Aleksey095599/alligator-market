-- instrument_fx_spot: FOREX_SPOT instrument aggregate
CREATE TABLE instrument_fx_spot
(
    instrument_code       VARCHAR(50) PRIMARY KEY,

    base_currency         VARCHAR(3)  NOT NULL,
    quote_currency        VARCHAR(3)  NOT NULL,
    tenor                 VARCHAR(5)  NOT NULL,
    quote_fraction_digits INTEGER     NOT NULL DEFAULT 4,

    CONSTRAINT fk_fx_spot_instrument
        FOREIGN KEY (instrument_code)
            REFERENCES instrument_registry (code),
    CONSTRAINT fk_fx_spot_base
        FOREIGN KEY (base_currency)
            REFERENCES currency (code),
    CONSTRAINT fk_fx_spot_quote
        FOREIGN KEY (quote_currency)
            REFERENCES currency (code),

    CONSTRAINT uq_fx_spot_pair_tenor
        UNIQUE (base_currency, quote_currency, tenor),

    CONSTRAINT chk_fx_spot_base_quote_diff
        CHECK (base_currency <> quote_currency),
    CONSTRAINT chk_fx_spot_digits_range
        CHECK (quote_fraction_digits BETWEEN 0 AND 10)
);

CREATE INDEX idx_fx_spot_base ON instrument_fx_spot (base_currency);
CREATE INDEX idx_fx_spot_quote ON instrument_fx_spot (quote_currency);
