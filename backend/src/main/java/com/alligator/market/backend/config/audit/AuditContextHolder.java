package com.alligator.market.backend.config.audit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * ThreadLocal-холдер контекста аудита.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) // ← Конструктор только внутри самого класса
public final class AuditContextHolder {

    /* Зарезервированный системный актор. */
    public static final String SYSTEM_ACTOR = "system";

    /* Фолбэки, если контекст не задан. */
    public static final String FALLBACK_ACTOR = "fallback_actor";
    public static final String FALLBACK_VIA   = "fallback_via";

    /* Готовый общий фолбэк-контекст. */
    private static final AuditContext FALLBACK_CTX =
            new AuditContext(FALLBACK_ACTOR, FALLBACK_VIA);

    /* Локальное хранилище контекста для текущего потока. */
    private static final ThreadLocal<AuditContext> CTX = new ThreadLocal<>();

    /** Установить контекст. */
    public static void set(@NonNull AuditContext ctx) {
        CTX.set(ctx);
    }

    /** Текущий контекст или фолбэк. */
    public static AuditContext currentOrFallback() {
        // Извлекаем контекст
        var ctx = CTX.get();

        // Если контекст null, возвращаем фолбэк контекст
        if (ctx == null) return FALLBACK_CTX;

        // (*) Если есть недостающие компоненты, заполняем их фолбэками
        String actor = (ctx.actorId() != null && !ctx.actorId().isBlank())
                ? ctx.actorId() : FALLBACK_ACTOR;
        String via = (ctx.via() != null && !ctx.via().isBlank())
                ? ctx.via() : FALLBACK_VIA;


        if (actor.equals(ctx.actorId()) && via.equals(ctx.via())) {
            return ctx; // Если шаг (*) ничего не изменил
        } else {
            return new AuditContext(actor, via); // Иначе создаем и возвращаем новый контекст
        }
    }

    /** Получить актора или фолбэк. */
    public static String actorOrFallback() { return currentOrFallback().actorId(); }

    /** Получить источник/канал или фолбэк. */
    public static String viaOrFallback() { return currentOrFallback().via(); }

    /** Сбросить контекст. */
    public static void clear() { CTX.remove(); }
}
