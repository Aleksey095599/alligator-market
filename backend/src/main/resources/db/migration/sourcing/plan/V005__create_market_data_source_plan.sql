-- market_data_source_plan: source plan identity
CREATE TABLE market_data_source_plan
(
    collection_process_code VARCHAR(128) NOT NULL,
    instrument_code         VARCHAR(50)  NOT NULL,

    CONSTRAINT fk_market_data_source_plan_instrument
        FOREIGN KEY (instrument_code) REFERENCES instrument_registry (code),

    CONSTRAINT pk_market_data_source_plan
        PRIMARY KEY (collection_process_code, instrument_code)
);
