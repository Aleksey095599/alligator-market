-- Убирает уникальные ограничения с provider_profile
ALTER TABLE provider_profile DROP CONSTRAINT IF EXISTS uq_provider_code;
ALTER TABLE provider_profile DROP CONSTRAINT IF EXISTS uq_display_name;
