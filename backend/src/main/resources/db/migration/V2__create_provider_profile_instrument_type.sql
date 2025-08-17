-- Миграция создания таблицы типов инструментов провайдера
CREATE TABLE public.provider_profile_instrument_type (
    provider_id BIGINT NOT NULL,
    instrument_type VARCHAR(20) NOT NULL,
    CONSTRAINT fk_provider_profile_instrument_type_provider_profile
        FOREIGN KEY (provider_id) REFERENCES public.provider_profile (id),
    CONSTRAINT pk_provider_profile_instrument_type PRIMARY KEY (provider_id, instrument_type)
);
