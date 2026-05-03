-- source_plan: план источников рыночных данных
CREATE TABLE source_plan
(
    -- Идентичность плана источников
    collection_process_code VARCHAR(128) NOT NULL,
    instrument_code         VARCHAR(50)  NOT NULL,

    -- Ограничение ссылочной целостности
    CONSTRAINT fk_instr_source_plan_instrument
        FOREIGN KEY (instrument_code) REFERENCES instrument_registry (code),

    CONSTRAINT pk_source_plan
        PRIMARY KEY (collection_process_code, instrument_code)
);
