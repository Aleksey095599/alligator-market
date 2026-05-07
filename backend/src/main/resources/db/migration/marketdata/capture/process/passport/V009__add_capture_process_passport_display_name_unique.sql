-- capture_process_passport: display name is unique for UI selection
ALTER TABLE capture_process_passport
    ADD CONSTRAINT uq_capture_process_display_name UNIQUE (display_name);
