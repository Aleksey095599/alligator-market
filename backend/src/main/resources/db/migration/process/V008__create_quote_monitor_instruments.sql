CREATE TABLE quote_monitor_instruments
(
    instrument_code VARCHAR(50)  NOT NULL,
    capturer_code   VARCHAR(128) NOT NULL DEFAULT 'LIVE_QUOTE_MONITOR',

    CONSTRAINT pk_quote_monitor_instruments
        PRIMARY KEY (instrument_code),

    CONSTRAINT fk_quote_monitor_instruments_source_plan
        FOREIGN KEY (capturer_code, instrument_code)
            REFERENCES source_plan (capturer_code, instrument_code)
            ON DELETE CASCADE,

    CONSTRAINT chk_quote_monitor_instruments_capturer
        CHECK (capturer_code = 'LIVE_QUOTE_MONITOR')
);
