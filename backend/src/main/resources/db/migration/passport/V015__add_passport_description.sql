ALTER TABLE source_passport
    ADD COLUMN description VARCHAR(100);

UPDATE source_passport
SET description = CASE source_code
    WHEN 'MOEX_ISS' THEN 'MOEX ISS market data source'
    WHEN 'TWELVE_DATA' THEN 'Twelve Data market data source'
    ELSE display_name
END;

ALTER TABLE source_passport
    ALTER COLUMN description SET NOT NULL,
    ADD CONSTRAINT chk_source_passport_description_not_blank
        CHECK (length(btrim(description)) > 0),
    ADD CONSTRAINT chk_source_passport_description_no_control_chars
        CHECK (description !~ '[[:cntrl:]]');

ALTER TABLE capturer_passport
    ADD COLUMN description VARCHAR(100);

UPDATE capturer_passport
SET description = CASE capturer_code
    WHEN 'QUOTE_MONITOR' THEN 'Captures selected instrument quotes for monitoring'
    ELSE display_name
END;

ALTER TABLE capturer_passport
    ALTER COLUMN description SET NOT NULL,
    ADD CONSTRAINT chk_capturer_passport_description_not_blank
        CHECK (length(btrim(description)) > 0),
    ADD CONSTRAINT chk_capturer_passport_description_no_control_chars
        CHECK (description !~ '[[:cntrl:]]');
