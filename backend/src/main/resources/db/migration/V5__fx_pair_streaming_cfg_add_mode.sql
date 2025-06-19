ALTER TABLE fx_pair_streaming_cfg
    ADD COLUMN mode VARCHAR(4) NOT NULL DEFAULT 'PULL';

ALTER TABLE fx_pair_streaming_cfg
    DROP CONSTRAINT uq_fx_pair_streaming_cfg_pair_provider;

ALTER TABLE fx_pair_streaming_cfg
    ADD CONSTRAINT uq_fx_pair_streaming_cfg_pair_provider_mode UNIQUE (pair_id, provider, mode);
