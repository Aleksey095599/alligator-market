ALTER TABLE market_data_source_passport
    RENAME TO source_passport;

ALTER SEQUENCE market_data_source_passport_id_seq
    RENAME TO source_passport_id_seq;

ALTER TABLE source_passport
    RENAME CONSTRAINT market_data_source_passport_pkey
        TO source_passport_pkey;

ALTER TABLE source_passport
    RENAME CONSTRAINT uq_market_data_source_passport_source_code
        TO uq_source_passport_source_code;

ALTER TABLE source_passport
    RENAME CONSTRAINT chk_market_data_source_passport_source_code_pattern
        TO chk_source_passport_source_code_pattern;

ALTER TABLE source_passport
    RENAME CONSTRAINT chk_market_data_source_passport_source_code_not_blank
        TO chk_source_passport_source_code_not_blank;

ALTER TABLE source_passport
    RENAME CONSTRAINT chk_market_data_source_passport_display_name_not_blank
        TO chk_source_passport_display_name_not_blank;

ALTER TABLE source_passport
    RENAME CONSTRAINT chk_market_data_source_passport_delivery_mode_allowed
        TO chk_source_passport_delivery_mode_allowed;

ALTER TABLE source_passport
    RENAME CONSTRAINT chk_market_data_source_passport_access_method_allowed
        TO chk_source_passport_access_method_allowed;

ALTER TABLE source_passport
    RENAME CONSTRAINT chk_market_data_source_passport_lifecycle_status
        TO chk_source_passport_lifecycle_status;
