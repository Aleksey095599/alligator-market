-- market_data_source_plan -> capture_process_passport: source plan capture process
ALTER TABLE market_data_source_plan
    ADD CONSTRAINT fk_market_data_source_plan_capture_process
        FOREIGN KEY (collection_process_code)
            REFERENCES capture_process_passport (capture_process_code);
