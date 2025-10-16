CREATE TABLE fx_spot (
    id BIGINT PRIMARY KEY,
    base_currency VARCHAR(3) NOT NULL,
    quote_currency VARCHAR(3) NOT NULL,
    value_date VARCHAR(4) NOT NULL,
    quote_fraction_digits INTEGER NOT NULL,
    CONSTRAINT fk_fx_spot_instrument FOREIGN KEY (id) REFERENCES instrument (id) ON DELETE CASCADE,
    CONSTRAINT fk_fx_spot_base FOREIGN KEY (base_currency) REFERENCES currency (code),
    CONSTRAINT fk_fx_spot_quote FOREIGN KEY (quote_currency) REFERENCES currency (code),
    CONSTRAINT uq_fx_spot_pair_value_date UNIQUE (base_currency, quote_currency, value_date),
    CONSTRAINT chk_fx_spot_integrity CHECK (
        base_currency <> quote_currency
        AND quote_fraction_digits BETWEEN 0 AND 10
        AND value_date IN ('TOD', 'TOM', 'SPOT')
    )
);

CREATE INDEX idx_fx_spot_base ON fx_spot (base_currency);
CREATE INDEX idx_fx_spot_quote ON fx_spot (quote_currency);
