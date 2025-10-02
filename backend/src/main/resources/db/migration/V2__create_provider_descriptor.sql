-- Таблица дескрипторов провайдеров.
CREATE TABLE provider_descriptor (
    id                BIGINT       PRIMARY KEY,
    display_name      VARCHAR(50)  NOT NULL,
    delivery_mode     VARCHAR(10)  NOT NULL,
    access_method     VARCHAR(20)  NOT NULL,
    bulk_subscription BOOLEAN      NOT NULL,
    CONSTRAINT fk_provider_descriptor_base FOREIGN KEY (id)
        REFERENCES market_data_provider (id)
        ON DELETE CASCADE,
    CONSTRAINT uq_display_name UNIQUE (display_name)
);
