-- Source rows keep retired state when linked passports are no longer current.
ALTER TABLE market_data_source
    ADD COLUMN lifecycle_status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE';

ALTER TABLE market_data_source
    ADD CONSTRAINT chk_market_data_source_lifecycle_status
        CHECK (lifecycle_status IN ('ACTIVE', 'RETIRED'));
