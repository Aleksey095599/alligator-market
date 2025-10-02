-- Таблица валютного справочника.
CREATE TABLE currency (
    id                BIGSERIAL    PRIMARY KEY,
    code              VARCHAR(3)   NOT NULL,
    name              VARCHAR(50)  NOT NULL,
    country           VARCHAR(100) NOT NULL,
    decimal_value     INT          NOT NULL,
    version           BIGINT       NOT NULL,
    created_timestamp TIMESTAMPTZ  NOT NULL,
    created_by        VARCHAR(255) NOT NULL,
    created_via       VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMPTZ  NOT NULL,
    updated_by        VARCHAR(255) NOT NULL,
    updated_via       VARCHAR(255) NOT NULL,
    CONSTRAINT uq_currency_code UNIQUE (code),
    CONSTRAINT uq_currency_name UNIQUE (name)
);
