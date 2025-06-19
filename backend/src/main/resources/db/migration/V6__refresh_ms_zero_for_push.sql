-- Set refresh_ms to 0 for PUSH mode where it's irrelevant
UPDATE fx_pair_streaming_cfg
SET refresh_ms = 0
WHERE mode = 'PUSH';

