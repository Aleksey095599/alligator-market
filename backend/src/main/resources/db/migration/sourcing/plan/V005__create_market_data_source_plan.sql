-- market_data_source_plan: root/identity плана источников рыночных данных
CREATE TABLE market_data_source_plan
(
    -- Идентичность плана источников рыночных данных
    collection_process_code VARCHAR(128) NOT NULL,
    instrument_code         VARCHAR(50)  NOT NULL,

    -- Ограничение ссылочной целостности
    CONSTRAINT fk_market_data_source_plan_instrument
        FOREIGN KEY (instrument_code) REFERENCES instrument_registry (code),

    CONSTRAINT pk_market_data_source_plan
        PRIMARY KEY (collection_process_code, instrument_code)
);
