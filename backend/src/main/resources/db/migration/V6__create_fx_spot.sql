-- Таблица инструментов FX Spot.
CREATE TABLE fx_spot (
    id              BIGINT      PRIMARY KEY,
    base_currency   VARCHAR(3)  NOT NULL,
    quote_currency  VARCHAR(3)  NOT NULL,
    value_date_code VARCHAR(4)  NOT NULL,
    quote_decimal   INT         NOT NULL,
    CONSTRAINT fk_fx_spot_base FOREIGN KEY (id)
        REFERENCES instrument (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_fx_spot_base_currency FOREIGN KEY (base_currency)
        REFERENCES currency (code)
        ON DELETE RESTRICT,
    CONSTRAINT fk_fx_spot_quote_currency FOREIGN KEY (quote_currency)
        REFERENCES currency (code)
        ON DELETE RESTRICT
);
