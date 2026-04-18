-- Удаляем version у instrument_base: optimistic locking для инструментов не используется.
ALTER TABLE instrument_base
    DROP COLUMN version;
