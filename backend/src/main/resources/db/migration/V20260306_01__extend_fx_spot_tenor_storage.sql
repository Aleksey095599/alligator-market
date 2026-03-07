-- Ослабляем ограничение tenor: храним код как строку без фиксированного перечня.
ALTER TABLE fx_spot
    DROP CONSTRAINT IF EXISTS chk_fx_spot_tenor_allowed;

ALTER TABLE fx_spot
    ALTER COLUMN tenor TYPE VARCHAR(5);
