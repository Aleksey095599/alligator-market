-- Создает таблицу provider_profile_instrument_type
CREATE TABLE provider_profile_instrument_type (
    provider_id BIGINT NOT NULL,
    instrument_type VARCHAR(20) NOT NULL,
    PRIMARY KEY (provider_id, instrument_type),
    CONSTRAINT fk_provider_instrument_type_provider FOREIGN KEY (provider_id) REFERENCES provider_profile(id)
);
