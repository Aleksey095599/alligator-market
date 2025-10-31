package com.alligator.market.backend.config.audit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Optional;

/**
 * ThreadLocal-холдер контекста аудита.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) // ← Конструктор только внутри самого класса
public final class AuditContextHolder {

    /* Зарезервированный системный актор. */
    public static final String SYSTEM_ACTOR = "system";

    /* Фолбэки, если контекст не задан. */
    public static final String FALLBACK_ACTOR = "default";
    public static final String FALLBACK_VIA   = "default";

    /* Локальное хранилище контекста для текущего потока. */
    private static final ThreadLocal<AuditContext> CTX = new ThreadLocal<>();

    /** Установить контекст. */
    public static void set(@NonNull AuditContext ctx) {
        CTX.set(ctx);
    }

    /** Получить актора или фолбэк. */
    public static String actorOrFallback() {
        return getOptional().map(AuditContext::actorId)
                .filter(a -> !a.isBlank())
                .orElse(FALLBACK_ACTOR);
    }

    /** Получить источник/канал или фолбэк. */
    public static String viaOrFallback() {
        return getOptional().map(AuditContext::via)
                .filter(v -> !v.isBlank())
                .orElse(FALLBACK_VIA);
    }

    /** Сбросить контекст. */
    public static void clear() {
        CTX.remove();
    }

    /** Получить текущий контекст как Optional (может отсутствовать). */
    private static Optional<AuditContext> getOptional() {
        return Optional.ofNullable(CTX.get());
    }
}
