-- Переименование столбцов таблицы currency_pair
ALTER TABLE currency_pair RENAME COLUMN code1 TO base;
ALTER TABLE currency_pair RENAME COLUMN code2 TO quote;
ALTER TABLE currency_pair RENAME COLUMN pair TO symbol;

-- Переименование ограничений
ALTER TABLE currency_pair DROP CONSTRAINT IF EXISTS fk_pair_code1;
ALTER TABLE currency_pair ADD CONSTRAINT fk_pair_base FOREIGN KEY (base) REFERENCES currency(code);
ALTER TABLE currency_pair DROP CONSTRAINT IF EXISTS fk_pair_code2;
ALTER TABLE currency_pair ADD CONSTRAINT fk_pair_quote FOREIGN KEY (quote) REFERENCES currency(code);
ALTER TABLE currency_pair DROP CONSTRAINT IF EXISTS uq_currency_pair;
ALTER TABLE currency_pair ADD CONSTRAINT uq_symbol UNIQUE (symbol);
