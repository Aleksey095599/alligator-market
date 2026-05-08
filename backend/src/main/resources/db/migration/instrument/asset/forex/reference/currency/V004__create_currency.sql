-- currency: currency reference data
CREATE TABLE currency
(
    id              BIGSERIAL PRIMARY KEY,

    code            VARCHAR(3)   NOT NULL,
    name            VARCHAR(50)  NOT NULL,
    country         VARCHAR(100) NOT NULL,
    fraction_digits INTEGER      NOT NULL DEFAULT 2,

    CONSTRAINT uq_currency_code
        UNIQUE (code),
    CONSTRAINT uq_currency_name
        UNIQUE (name),

    CONSTRAINT chk_currency_code_pattern
        CHECK (code ~ '^[A-Z]{3}$'),
    CONSTRAINT chk_currency_name_not_blank
        CHECK (length(btrim(name)) > 0),
    CONSTRAINT chk_currency_country_not_blank
        CHECK (length(btrim(country)) > 0),
    CONSTRAINT chk_currency_fraction_digits
        CHECK (fraction_digits BETWEEN 0 AND 10)
);
