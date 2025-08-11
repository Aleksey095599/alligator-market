package com.alligator.market.backend.config.audit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * ThreadLocal-холдер контекста аудита с явными дефолтами для дев-режима.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditContextHolder {

    /** Локальное хранилище контекста для текущего потока. */
    private static final ThreadLocal<AuditContext> contextThreadLocal = new ThreadLocal<>();

    /** Дефолтный контекст для дев-режима. */
    private static final AuditContext DEFAULTS =
            new AuditContext(AuditDefaults.DEV_USER, AuditDefaults.DEV_VIA);

    /** Установить контекст. */
    public static void set(@NonNull AuditContext ctx) {
        contextThreadLocal.set(ctx);
    }

    /** Получить текущий контекст. Если контекст пуст, вернет дефолтный контекст. */
    public static AuditContext get() {
        AuditContext ctx = contextThreadLocal.get();
        return (ctx != null) ? ctx : DEFAULTS;
    }

    /** Очистить контекст. */
    public static void clear() {
        contextThreadLocal.remove();
    }
}
