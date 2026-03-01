package com.alligator.market.backend.provider.readmodel.passport.persistence;

/**
 * Контракт таблицы provider_passport.
 *
 * <p>Единый источник истины для JDBC-адаптеров (SQL/DAO), чтобы не дублировать имена
 * таблиц/колонок строковыми литералами по проекту.</p>
 */
public final class ProviderPassportTable {

    public static final String TABLE = "provider_passport";

    public static final String COL_ID = "id";
    public static final String COL_PROVIDER_CODE = "provider_code";
    public static final String COL_DISPLAY_NAME = "display_name";
    public static final String COL_DELIVERY_MODE = "delivery_mode";
    public static final String COL_ACCESS_METHOD = "access_method";
    public static final String COL_BULK_SUBSCRIPTION = "bulk_subscription";

    public static final String COL_VERSION = "version";
    public static final String COL_CREATED_TS = "created_timestamp";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_VIA = "created_via";
    public static final String COL_UPDATED_TS = "updated_timestamp";
    public static final String COL_UPDATED_BY = "updated_by";
    public static final String COL_UPDATED_VIA = "updated_via";

    /**
     * PostgreSQL-тип для createArrayOf(...) при передаче списка provider_code как массива.
     */
    public static final String PG_ARRAY_VARCHAR = "varchar";

    private ProviderPassportTable() {
        throw new UnsupportedOperationException("Utility class");
    }
}
