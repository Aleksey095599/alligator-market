-- Обновляем ограничения таблицы валютных пар.
ALTER TABLE fx_spot
    ADD CONSTRAINT ck_fx_spot_currency_difference CHECK (base_currency <> quote_currency);

ALTER TABLE fx_spot
    ADD CONSTRAINT uq_fx_spot_pair_value_date UNIQUE (base_currency, quote_currency, value_date_code);
