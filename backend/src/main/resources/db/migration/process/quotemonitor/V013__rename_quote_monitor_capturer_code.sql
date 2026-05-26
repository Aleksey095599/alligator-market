ALTER TABLE quote_monitor_instruments
    DROP CONSTRAINT chk_quote_monitor_instruments_capturer;

ALTER TABLE quote_monitor_instruments
    DROP CONSTRAINT fk_quote_monitor_instruments_source_plan;

ALTER TABLE source_plan_entry
    DROP CONSTRAINT fk_source_plan_entry_plan;

ALTER TABLE source_plan
    DROP CONSTRAINT fk_source_plan_capturer;

UPDATE capturer_passport
SET capturer_code = 'QUOTE_MONITOR',
    display_name = 'Quote Monitor'
WHERE capturer_code = 'LIVE_QUOTE_MONITOR';

UPDATE source_plan
SET capturer_code = 'QUOTE_MONITOR'
WHERE capturer_code = 'LIVE_QUOTE_MONITOR';

UPDATE source_plan_entry
SET capturer_code = 'QUOTE_MONITOR'
WHERE capturer_code = 'LIVE_QUOTE_MONITOR';

UPDATE quote_monitor_instruments
SET capturer_code = 'QUOTE_MONITOR'
WHERE capturer_code = 'LIVE_QUOTE_MONITOR';

ALTER TABLE quote_monitor_instruments
    ALTER COLUMN capturer_code SET DEFAULT 'QUOTE_MONITOR';

ALTER TABLE source_plan
    ADD CONSTRAINT fk_source_plan_capturer
        FOREIGN KEY (capturer_code)
            REFERENCES capturer_passport (capturer_code);

ALTER TABLE source_plan_entry
    ADD CONSTRAINT fk_source_plan_entry_plan
        FOREIGN KEY (capturer_code, instrument_code)
            REFERENCES source_plan (capturer_code, instrument_code)
            ON DELETE CASCADE;

ALTER TABLE quote_monitor_instruments
    ADD CONSTRAINT fk_quote_monitor_instruments_source_plan
        FOREIGN KEY (capturer_code, instrument_code)
            REFERENCES source_plan (capturer_code, instrument_code)
            ON DELETE CASCADE;

ALTER TABLE quote_monitor_instruments
    ADD CONSTRAINT chk_quote_monitor_instruments_capturer
        CHECK (capturer_code = 'QUOTE_MONITOR');
