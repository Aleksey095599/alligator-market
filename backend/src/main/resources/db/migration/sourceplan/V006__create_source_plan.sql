CREATE TABLE source_plan
(
    capturer_code    VARCHAR(50)  NOT NULL,
    instrument_code  VARCHAR(50)  NOT NULL,
    execution_status VARCHAR(32)  NOT NULL DEFAULT 'EXECUTABLE',

    CONSTRAINT pk_source_plan
        PRIMARY KEY (capturer_code, instrument_code),

    CONSTRAINT fk_source_plan_capturer
        FOREIGN KEY (capturer_code)
            REFERENCES market_data_capturer_passport (capturer_code),
    CONSTRAINT fk_source_plan_instrument
        FOREIGN KEY (instrument_code)
            REFERENCES instrument_registry (instrument_code),

    CONSTRAINT chk_source_plan_execution_status
        CHECK (execution_status IN ('EXECUTABLE', 'CAPTURER_RETIRED', 'NO_EXECUTABLE_SOURCES'))
);
