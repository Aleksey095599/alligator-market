-- Добавляет столбцы created_via и updated_via во все таблицы
ALTER TABLE currency ADD COLUMN created_via VARCHAR(255);
ALTER TABLE currency ADD COLUMN updated_via VARCHAR(255);
ALTER TABLE currency_pair ADD COLUMN created_via VARCHAR(255);
ALTER TABLE currency_pair ADD COLUMN updated_via VARCHAR(255);
ALTER TABLE provider_profile ADD COLUMN created_via VARCHAR(255);
ALTER TABLE provider_profile ADD COLUMN updated_via VARCHAR(255);
ALTER TABLE fx_spot_instruments ADD COLUMN created_via VARCHAR(255);
ALTER TABLE fx_spot_instruments ADD COLUMN updated_via VARCHAR(255);
