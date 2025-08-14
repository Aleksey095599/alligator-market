-- Переименование внешнего ключа типов инструментов провайдера
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_provider_instrument_type_provider') THEN
        ALTER TABLE provider_profile_instrument_type
            RENAME CONSTRAINT fk_provider_instrument_type_provider TO fk_provider_profile_instrument_type_provider_profile;
    END IF;
END $$;
