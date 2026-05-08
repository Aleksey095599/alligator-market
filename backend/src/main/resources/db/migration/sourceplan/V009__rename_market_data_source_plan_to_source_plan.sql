ALTER TABLE market_data_source_plan
    RENAME TO source_plan;

ALTER TABLE source_plan
    RENAME CONSTRAINT pk_market_data_source_plan TO pk_source_plan;
ALTER TABLE source_plan
    RENAME CONSTRAINT fk_market_data_source_plan_capturer TO fk_source_plan_capturer;
ALTER TABLE source_plan
    RENAME CONSTRAINT fk_market_data_source_plan_instrument TO fk_source_plan_instrument;

ALTER TABLE market_data_source_plan_entry
    RENAME TO source_plan_entry;

ALTER TABLE source_plan_entry
    RENAME CONSTRAINT fk_market_data_source_plan_entry_plan TO fk_source_plan_entry_plan;
ALTER TABLE source_plan_entry
    RENAME CONSTRAINT fk_market_data_source_plan_entry_source_passport TO fk_source_plan_entry_source_passport;
ALTER TABLE source_plan_entry
    RENAME CONSTRAINT uq_market_data_source_plan_entry_source TO uq_source_plan_entry_source;
ALTER TABLE source_plan_entry
    RENAME CONSTRAINT uq_market_data_source_plan_entry_priority TO uq_source_plan_entry_priority;
ALTER TABLE source_plan_entry
    RENAME CONSTRAINT chk_market_data_source_plan_entry_priority TO chk_source_plan_entry_priority;
ALTER TABLE source_plan_entry
    RENAME CONSTRAINT chk_market_data_source_plan_entry_lifecycle_status TO chk_source_plan_entry_lifecycle_status;
