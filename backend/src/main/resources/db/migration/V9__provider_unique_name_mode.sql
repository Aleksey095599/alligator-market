ALTER TABLE provider
    DROP CONSTRAINT uq_provider_name;

ALTER TABLE provider
    ADD CONSTRAINT uq_provider_name_mode UNIQUE (name, mode);
