-- instrument_fx_spot: таблица-наследник instrument_base для инструмента FOREX_SPOT.
CREATE TABLE instrument_fx_spot
(
    -- PK одновременно является FK на instrument_base.id (JOINED inheritance)
    id                    BIGINT       PRIMARY KEY,

    -- Валютная пара
    base_currency         VARCHAR(3)   NOT NULL,
    quote_currency        VARCHAR(3)   NOT NULL,
    tenor                 VARCHAR(5)   NOT NULL,
    quote_fraction_digits INTEGER      NOT NULL DEFAULT 4,

    -- Ограничения ссылочной целостности
    CONSTRAINT fk_fx_spot_instrument
        FOREIGN KEY (id) REFERENCES instrument_base (id),
    CONSTRAINT fk_fx_spot_base
        FOREIGN KEY (base_currency) REFERENCES currency (code),
    CONSTRAINT fk_fx_spot_quote
        FOREIGN KEY (quote_currency) REFERENCES currency (code),

    -- Ограничения уникальности
    CONSTRAINT uq_fx_spot_pair_tenor UNIQUE (base_currency, quote_currency, tenor),

    -- Ограничения доменной валидности
    CONSTRAINT chk_fx_spot_base_quote_diff
        CHECK (base_currency <> quote_currency),
    CONSTRAINT chk_fx_spot_digits_range
        CHECK (quote_fraction_digits BETWEEN 0 AND 10)
);

-- Индексы на FK-колонках для ускорения join/фильтрации.
CREATE INDEX idx_fx_spot_base ON instrument_fx_spot (base_currency);
CREATE INDEX idx_fx_spot_quote ON instrument_fx_spot (quote_currency);
