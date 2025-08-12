-- Исправляем временные зоны в колонках аудита.
ALTER TABLE currency
    ALTER COLUMN created_timestamp TYPE TIMESTAMP WITH TIME ZONE USING created_timestamp AT TIME ZONE 'UTC',
    ALTER COLUMN updated_timestamp TYPE TIMESTAMP WITH TIME ZONE USING updated_timestamp AT TIME ZONE 'UTC';

ALTER TABLE currency_pair
    ALTER COLUMN created_timestamp TYPE TIMESTAMP WITH TIME ZONE USING created_timestamp AT TIME ZONE 'UTC',
    ALTER COLUMN updated_timestamp TYPE TIMESTAMP WITH TIME ZONE USING updated_timestamp AT TIME ZONE 'UTC';

ALTER TABLE fx_spot_instruments
    ALTER COLUMN created_timestamp TYPE TIMESTAMP WITH TIME ZONE USING created_timestamp AT TIME ZONE 'UTC',
    ALTER COLUMN updated_timestamp TYPE TIMESTAMP WITH TIME ZONE USING updated_timestamp AT TIME ZONE 'UTC';

ALTER TABLE provider_profile
    ALTER COLUMN created_timestamp TYPE TIMESTAMP WITH TIME ZONE USING created_timestamp AT TIME ZONE 'UTC',
    ALTER COLUMN updated_timestamp TYPE TIMESTAMP WITH TIME ZONE USING updated_timestamp AT TIME ZONE 'UTC';
