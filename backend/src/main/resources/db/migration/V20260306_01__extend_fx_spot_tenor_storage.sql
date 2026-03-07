-- Ослабляем ограничение tenor: храним код как строку без фиксированного перечня.
ALTER TABLE instrument_fx_spot
    DROP CONSTRAINT IF EXISTS chk_fx_spot_tenor_allowed;

ALTER TABLE instrument_fx_spot
    ALTER COLUMN tenor TYPE VARCHAR(5);
