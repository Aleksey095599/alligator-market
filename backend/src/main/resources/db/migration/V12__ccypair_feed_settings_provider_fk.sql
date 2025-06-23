ALTER TABLE ccypair_feed_settings
    ADD CONSTRAINT fk_ccypair_feed_settings_provider FOREIGN KEY (provider) REFERENCES provider (name);
