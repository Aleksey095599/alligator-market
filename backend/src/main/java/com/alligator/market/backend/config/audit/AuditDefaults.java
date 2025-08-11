package com.alligator.market.backend.config.audit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Набор дефолтных констант.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditDefaults {

    public static final String DEV_USER = "dev_admin";   // ← временная заглушка
    public static final String DEV_VIA = "rest-api-dev"; // ← источник по умолчанию для REST в деве
}
