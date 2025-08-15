-- Удаляем уникальное ограничение на страну валюты.
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uq_currency_country') THEN
        ALTER TABLE currency DROP CONSTRAINT uq_currency_country;
    END IF;
END $$;
