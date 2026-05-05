-- market_data_source: active removed; source presence in a plan means it is selected
ALTER TABLE market_data_source
    DROP COLUMN active;
