-- capture_process_passport: отображаемое имя процесса фиксации уникально для выбора в UI
ALTER TABLE capture_process_passport
    ADD CONSTRAINT uq_capture_process_display_name UNIQUE (display_name);
