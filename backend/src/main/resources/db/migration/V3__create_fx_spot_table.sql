CREATE TABLE fx_spot (
    id BIGINT PRIMARY KEY,
    base_currency VARCHAR(3) NOT NULL,
    quote_currency VARCHAR(3) NOT NULL,
    tenor VARCHAR(4) NOT NULL,
    quote_fraction_digits INTEGER NOT NULL DEFAULT 4,
    CONSTRAINT fk_fx_spot_instrument FOREIGN KEY (id) REFERENCES instrument (id) ON DELETE CASCADE,
    CONSTRAINT fk_fx_spot_base FOREIGN KEY (base_currency) REFERENCES currency (code),
    CONSTRAINT fk_fx_spot_quote FOREIGN KEY (quote_currency) REFERENCES currency (code),
    CONSTRAINT uq_fx_spot_pair_tenor UNIQUE (base_currency, quote_currency, tenor),
    CONSTRAINT chk_fx_spot_base_quote_diff CHECK (base_currency <> quote_currency),
    CONSTRAINT chk_fx_spot_digits_range CHECK (quote_fraction_digits BETWEEN 0 AND 10),
    CONSTRAINT chk_fx_spot_tenor_allowed CHECK (tenor IN ('TOD', 'TOM', 'SPOT'))
);

CREATE INDEX idx_fx_spot_base ON fx_spot (base_currency);
CREATE INDEX idx_fx_spot_quote ON fx_spot (quote_currency);
