-- Переименование таблицы FX SPOT в FX OUTRIGHT
ALTER TABLE IF EXISTS fx_spot_instruments
    RENAME TO fx_outright_instruments;

-- Переименование внешнего ключа к валютной паре
ALTER TABLE fx_outright_instruments
    RENAME CONSTRAINT fk_fx_spot_pair TO fk_fx_outright_pair;
