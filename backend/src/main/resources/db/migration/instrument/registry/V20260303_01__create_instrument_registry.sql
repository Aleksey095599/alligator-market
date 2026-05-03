-- instrument_registry: единый реестр кодов инструментов
CREATE TABLE instrument_registry
(
    -- Глобальная идентичность инструмента
    code VARCHAR(50) PRIMARY KEY,

    -- Базовая доменная валидность
    CONSTRAINT chk_instrument_registry_code_pattern
        CHECK (code ~ '^[A-Z0-9_]+$')
);
