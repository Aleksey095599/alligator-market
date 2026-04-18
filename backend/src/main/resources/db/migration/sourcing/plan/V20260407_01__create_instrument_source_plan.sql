-- instrument_source_plan: план источников рыночных данных для конкретного инструмента.
CREATE TABLE instrument_source_plan
(
    -- Идентичность плана источников
    instrument_code VARCHAR(50) PRIMARY KEY,

    -- Ограничение ссылочной целостности
    CONSTRAINT fk_instr_source_plan_instrument
        FOREIGN KEY (instrument_code) REFERENCES instrument_registry (code)
);
