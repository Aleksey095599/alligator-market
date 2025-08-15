-- Переименование колонки количества знаков после запятой.
ALTER TABLE currency
    RENAME COLUMN decimal TO decimal_digits;
