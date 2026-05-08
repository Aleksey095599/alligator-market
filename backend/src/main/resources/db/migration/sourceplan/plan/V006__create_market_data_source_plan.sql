-- market_data_source_plan: source plan identity
CREATE TABLE market_data_source_plan
(
    capturer_code   VARCHAR(128) NOT NULL,
    instrument_code VARCHAR(50)  NOT NULL,

    CONSTRAINT pk_market_data_source_plan
        PRIMARY KEY (capturer_code, instrument_code),

    CONSTRAINT fk_market_data_source_plan_capturer
        FOREIGN KEY (capturer_code)
            REFERENCES market_data_capturer_passport (capturer_code),
    CONSTRAINT fk_market_data_source_plan_instrument
        FOREIGN KEY (instrument_code)
            REFERENCES instrument_registry (code)
);
