package com.alligator.market.backend.config.audit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * ThreadLocal-холдер контекста аудита.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditContextHolder {

    // Дефолты дев-режима (TODO: временная заглушка до реализации аутентификации)
    private static final String DEV_ACTOR = "dev_admin";
    private static final String DEV_VIA   = "rest-api-dev";
    private static final AuditContext DEV_DEFAULTS = new AuditContext(DEV_ACTOR, DEV_VIA);

    /** Локальное хранилище контекста для текущего потока. */
    private static final ThreadLocal<AuditContext> contextThreadLocal = new ThreadLocal<>();

    /** Установить контекст. */
    public static void set(@NonNull AuditContext ctx) {
        contextThreadLocal.set(ctx);
    }

    /** Получить текущий контекст. Если контекст пуст, вернет дефолтный контекст дев-режима. */
    public static AuditContext get() {
        AuditContext ctx = contextThreadLocal.get();
        return (ctx != null) ? ctx : DEV_DEFAULTS;
    }

    /** Очистить контекст. */
    public static void clear() {
        contextThreadLocal.remove();
    }
}
