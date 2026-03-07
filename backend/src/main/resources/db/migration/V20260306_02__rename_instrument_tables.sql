-- Переименовываем базовую таблицу инструментов для явного разделения с таблицами-наследниками.
ALTER TABLE IF EXISTS instrument
    RENAME TO instrument_base;

-- Приводим имя таблицы FOREX_SPOT к единому стандарту именования инструментов.
ALTER TABLE IF EXISTS fx_spot
    RENAME TO instrument_fx_spot;

-- Переименовываем индексы базовой таблицы.
ALTER INDEX IF EXISTS idx_instrument_asset_class
    RENAME TO idx_instrument_base_asset_class;
ALTER INDEX IF EXISTS idx_instrument_contract_type
    RENAME TO idx_instrument_base_contract_type;

-- Переименовываем индексы таблицы FOREX_SPOT.
ALTER INDEX IF EXISTS idx_fx_spot_base
    RENAME TO idx_instrument_fx_spot_base;
ALTER INDEX IF EXISTS idx_fx_spot_quote
    RENAME TO idx_instrument_fx_spot_quote;

-- Переименовываем ограничения для консистентного нейминга.
ALTER TABLE IF EXISTS instrument_base
    RENAME CONSTRAINT uq_instrument_code TO uq_instrument_base_code;
ALTER TABLE IF EXISTS instrument_base
    RENAME CONSTRAINT chk_instrument_code_pattern TO chk_instrument_base_code_pattern;
ALTER TABLE IF EXISTS instrument_base
    RENAME CONSTRAINT chk_instrument_symbol_pattern TO chk_instrument_base_symbol_pattern;
ALTER TABLE IF EXISTS instrument_base
    RENAME CONSTRAINT chk_instrument_version_non_negative TO chk_instrument_base_version_non_negative;

ALTER TABLE IF EXISTS instrument_fx_spot
    RENAME CONSTRAINT fk_fx_spot_instrument TO fk_instrument_fx_spot_instrument_base;
ALTER TABLE IF EXISTS instrument_fx_spot
    RENAME CONSTRAINT fk_fx_spot_base TO fk_instrument_fx_spot_base;
ALTER TABLE IF EXISTS instrument_fx_spot
    RENAME CONSTRAINT fk_fx_spot_quote TO fk_instrument_fx_spot_quote;
ALTER TABLE IF EXISTS instrument_fx_spot
    RENAME CONSTRAINT uq_fx_spot_pair_tenor TO uq_instrument_fx_spot_pair_tenor;
ALTER TABLE IF EXISTS instrument_fx_spot
    RENAME CONSTRAINT chk_fx_spot_base_quote_diff TO chk_instrument_fx_spot_base_quote_diff;
ALTER TABLE IF EXISTS instrument_fx_spot
    RENAME CONSTRAINT chk_fx_spot_digits_range TO chk_instrument_fx_spot_digits_range;

ALTER TABLE IF EXISTS instrument_feed_config
    RENAME CONSTRAINT fk_ifc_instrument TO fk_ifc_instrument_base;
