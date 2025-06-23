ALTER TABLE ccypair_feed_settings
    DROP CONSTRAINT IF EXISTS uq_ccypair_feed_settings_pair_provider_mode;

ALTER TABLE ccypair_feed_settings
    ADD CONSTRAINT uq_ccypair_feed_settings_pair_provider UNIQUE (pair_id, provider);
