-- Возвращаем типы аудита к TIMESTAMP без указания часового пояса.
ALTER TABLE currency
    ALTER COLUMN created_timestamp TYPE TIMESTAMP WITHOUT TIME ZONE USING created_timestamp AT TIME ZONE 'UTC',
    ALTER COLUMN updated_timestamp TYPE TIMESTAMP WITHOUT TIME ZONE USING updated_timestamp AT TIME ZONE 'UTC';

ALTER TABLE currency_pair
    ALTER COLUMN created_timestamp TYPE TIMESTAMP WITHOUT TIME ZONE USING created_timestamp AT TIME ZONE 'UTC',
    ALTER COLUMN updated_timestamp TYPE TIMESTAMP WITHOUT TIME ZONE USING updated_timestamp AT TIME ZONE 'UTC';

ALTER TABLE fx_outright_instruments
    ALTER COLUMN created_timestamp TYPE TIMESTAMP WITHOUT TIME ZONE USING created_timestamp AT TIME ZONE 'UTC',
    ALTER COLUMN updated_timestamp TYPE TIMESTAMP WITHOUT TIME ZONE USING updated_timestamp AT TIME ZONE 'UTC';

ALTER TABLE provider_profile
    ALTER COLUMN created_timestamp TYPE TIMESTAMP WITHOUT TIME ZONE USING created_timestamp AT TIME ZONE 'UTC',
    ALTER COLUMN updated_timestamp TYPE TIMESTAMP WITHOUT TIME ZONE USING updated_timestamp AT TIME ZONE 'UTC';
