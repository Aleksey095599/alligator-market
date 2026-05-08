-- market_data_source_plan: source plan identity
CREATE TABLE market_data_source_plan
(
    collection_process_code VARCHAR(128) NOT NULL,
    instrument_code         VARCHAR(50)  NOT NULL,

    CONSTRAINT pk_market_data_source_plan
        PRIMARY KEY (collection_process_code, instrument_code),

    CONSTRAINT fk_market_data_source_plan_capture_process
        FOREIGN KEY (collection_process_code)
            REFERENCES capture_process_passport (capture_process_code),
    CONSTRAINT fk_market_data_source_plan_instrument
        FOREIGN KEY (instrument_code)
            REFERENCES instrument_registry (code)
);
