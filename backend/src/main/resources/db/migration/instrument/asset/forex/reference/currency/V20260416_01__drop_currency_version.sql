-- Удаляем version у reference data валют: optimistic locking для currency не используется.
ALTER TABLE currency
    DROP COLUMN version;
