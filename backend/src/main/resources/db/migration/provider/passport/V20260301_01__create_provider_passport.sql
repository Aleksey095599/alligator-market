-- provider_passport: materialized view паспортов провайдеров.
CREATE TABLE provider_passport
(
    -- Суррогатный PK
    id                BIGSERIAL PRIMARY KEY,

    -- Натуральный ключ: код провайдера
    provider_code     VARCHAR(50)  NOT NULL,

    -- Паспорт провайдера
    display_name      VARCHAR(50)  NOT NULL,
    delivery_mode     VARCHAR(10)  NOT NULL,
    access_method     VARCHAR(20)  NOT NULL,
    bulk_subscription BOOLEAN      NOT NULL,

    -- Уникальность натурального ключа
    CONSTRAINT uq_provider_code UNIQUE (provider_code),

    -- Ограничения полей
    CONSTRAINT chk_provider_code_pattern
        CHECK (provider_code ~ '^[A-Z0-9_.-]+$'),
    CONSTRAINT chk_provider_code_not_blank
        CHECK (length(btrim(provider_code)) > 0),
    CONSTRAINT chk_display_name_not_blank
        CHECK (length(btrim(display_name)) > 0),
    CONSTRAINT chk_provider_delivery_mode_allowed
        CHECK (delivery_mode IN ('PULL', 'PUSH')),
    CONSTRAINT chk_provider_access_method_allowed
        CHECK (access_method IN ('API_POLL', 'WEBSOCKET', 'FIX_PROTOCOL'))
);
