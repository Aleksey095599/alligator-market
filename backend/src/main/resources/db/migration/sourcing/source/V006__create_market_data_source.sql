-- market_data_source: one entry inside a market data source plan
CREATE TABLE market_data_source
(
    id                      BIGSERIAL PRIMARY KEY,

    collection_process_code VARCHAR(128) NOT NULL,
    instrument_code         VARCHAR(50)  NOT NULL,
    source_code             VARCHAR(50)  NOT NULL,
    priority                INTEGER      NOT NULL,
    lifecycle_status        VARCHAR(16)  NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT fk_market_data_source_plan
        FOREIGN KEY (collection_process_code, instrument_code)
            REFERENCES market_data_source_plan (collection_process_code, instrument_code)
            ON DELETE CASCADE,

    CONSTRAINT fk_market_data_source_passport
        FOREIGN KEY (source_code)
            REFERENCES market_data_source_passport (source_code),

    CONSTRAINT uq_market_data_source_plan_source
        UNIQUE (collection_process_code, instrument_code, source_code),
    CONSTRAINT uq_market_data_source_process_instr_priority
        UNIQUE (collection_process_code, instrument_code, priority),

    CONSTRAINT chk_market_data_source_priority
        CHECK (priority >= 0),
    CONSTRAINT chk_market_data_source_lifecycle_status
        CHECK (lifecycle_status IN ('ACTIVE', 'RETIRED'))
);
