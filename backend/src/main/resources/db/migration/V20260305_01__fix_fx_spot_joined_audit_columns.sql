-- В JOINED inheritance аудит/версия хранятся в корневой таблице instrument.
-- Из таблицы-наследника fx_spot убираем дублирующие колонки, которые не заполняются Hibernate.
ALTER TABLE fx_spot
    DROP CONSTRAINT IF EXISTS chk_fx_spot_version_non_negative;

ALTER TABLE fx_spot
    DROP COLUMN IF EXISTS version,
    DROP COLUMN IF EXISTS created_timestamp,
    DROP COLUMN IF EXISTS created_by,
    DROP COLUMN IF EXISTS created_via,
    DROP COLUMN IF EXISTS updated_timestamp,
    DROP COLUMN IF EXISTS updated_by,
    DROP COLUMN IF EXISTS updated_via;
