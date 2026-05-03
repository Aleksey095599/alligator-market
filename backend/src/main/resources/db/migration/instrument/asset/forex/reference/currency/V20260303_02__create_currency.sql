-- currency: справочник валют
CREATE TABLE currency
(
    -- Суррогатный PK
    id                BIGSERIAL PRIMARY KEY,

    -- Натуральный ключ
    code              VARCHAR(3)   NOT NULL,

    -- Идентичность валюты
    name              VARCHAR(50)  NOT NULL,
    country           VARCHAR(100) NOT NULL,
    fraction_digits   INTEGER      NOT NULL DEFAULT 2,

    -- Ограничения уникальности
    CONSTRAINT uq_currency_code UNIQUE (code),
    CONSTRAINT uq_currency_name UNIQUE (name),

    -- Ограничения доменной валидности
    CONSTRAINT chk_currency_code_pattern
        CHECK (code ~ '^[A-Z]{3}$'),
    CONSTRAINT chk_currency_name_not_blank
        CHECK (length(btrim(name)) > 0),
    CONSTRAINT chk_currency_country_not_blank
        CHECK (length(btrim(country)) > 0),
    CONSTRAINT chk_currency_fraction_digits
        CHECK (fraction_digits BETWEEN 0 AND 10)
);
