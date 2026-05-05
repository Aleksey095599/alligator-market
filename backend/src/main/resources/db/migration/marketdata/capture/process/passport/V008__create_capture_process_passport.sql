-- capture_process_passport: materialized view паспортов процессов фиксации рыночных данных
CREATE TABLE capture_process_passport
(
    -- Суррогатный PK
    id                   BIGSERIAL PRIMARY KEY,

    -- Натуральный ключ: код процесса фиксации рыночных данных
    capture_process_code VARCHAR(128) NOT NULL,

    -- Поля паспорта процесса фиксации рыночных данных
    display_name         VARCHAR(160) NOT NULL,

    -- Уникальность натурального ключа
    CONSTRAINT uq_capture_process_code UNIQUE (capture_process_code),

    -- Ограничения полей
    CONSTRAINT chk_capture_process_code_pattern
        CHECK (capture_process_code ~ '^[A-Z0-9_.-]+$'),
    CONSTRAINT chk_capture_process_code_not_blank
        CHECK (length(btrim(capture_process_code)) > 0),
    CONSTRAINT chk_capture_process_display_name_not_blank
        CHECK (length(btrim(display_name)) > 0)
);
