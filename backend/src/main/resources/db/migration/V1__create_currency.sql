/* Таблица справочника валют */
CREATE TABLE public.currency
(
    /* --- бизнес-поля --- */
    code              CHAR(3)      NOT NULL,
    name              VARCHAR(50)  NOT NULL,
    country           VARCHAR(100) NOT NULL,
    decimal           INT          NOT NULL DEFAULT 2 CHECK (decimal BETWEEN 0 AND 10),

    /* --- технические поля из BaseEntity --- */
    version           INT          NOT NULL DEFAULT 0,
    created_timestamp TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_timestamp TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_by        VARCHAR(64),
    updated_by        VARCHAR(64),

    /* --- ограничения --- */
    CONSTRAINT pk_currency PRIMARY KEY (code),
    CONSTRAINT uq_currency_name UNIQUE (name)
);