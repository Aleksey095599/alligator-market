ALTER TABLE ccypair
    ADD CONSTRAINT fk_pair_code1 FOREIGN KEY (code1) REFERENCES currency (code);

ALTER TABLE ccypair
    ADD CONSTRAINT fk_pair_code2 FOREIGN KEY (code2) REFERENCES currency (code);