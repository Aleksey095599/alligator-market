-- Миграция переименования столбца типов инструментов провайдера
ALTER TABLE public.provider_profile_instrument_type
    RENAME COLUMN instrument_type TO instrument_types;
