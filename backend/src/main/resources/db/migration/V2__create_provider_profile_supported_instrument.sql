-- Миграция создания таблицы поддерживаемых инструментов профиля провайдера
CREATE TABLE public.provider_profile_supported_instrument (
    provider_id BIGINT NOT NULL,
    instruments_supported VARCHAR(20) NOT NULL,
    CONSTRAINT fk_provider_profile_supported_instrument_provider_profile
        FOREIGN KEY (provider_id) REFERENCES public.provider_profile (id),
    CONSTRAINT pk_provider_profile_supported_instrument PRIMARY KEY (provider_id, instruments_supported)
);
