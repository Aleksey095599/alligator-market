ALTER TABLE provider
    DROP CONSTRAINT IF EXISTS uq_provider_name_mode;

ALTER TABLE provider
    ADD CONSTRAINT uq_provider_name UNIQUE (name);
