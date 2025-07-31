-- Переименовывает столбец symbol в pair_code
ALTER TABLE currency_pair RENAME COLUMN symbol TO pair_code;

-- Переименовывает уникальное ограничение
ALTER TABLE currency_pair DROP CONSTRAINT IF EXISTS uq_symbol;
ALTER TABLE currency_pair ADD CONSTRAINT uq_pair_code UNIQUE (pair_code);
