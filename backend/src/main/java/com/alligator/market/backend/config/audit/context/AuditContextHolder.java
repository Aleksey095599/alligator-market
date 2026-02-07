package com.alligator.market.backend.config.audit.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * ThreadLocal-холдер контекста аудита.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditContextHolder {

    /* Зарезервированный системный актор для внутренних процессов. */
    public static final String SYSTEM_ACTOR = "system";

    /* Фолбэки для актора и "via" при отсутствующем/пустом контексте. */
    public static final String FALLBACK_ACTOR = "fallback_actor";
    public static final String FALLBACK_VIA = "fallback_via";

    /* Пред-созданный фолбэк‑контекст (record иммутабелен). */
    private static final AuditContext FALLBACK_CTX = new AuditContext(FALLBACK_ACTOR, FALLBACK_VIA);

    /* Поток для хранения текущего контекста аудита. */
    private static final ThreadLocal<AuditContext> CTX = new ThreadLocal<>();

    /**
     * Устанавливает контекст для текущего потока.
     */
    public static void set(AuditContext ctx) {
        CTX.set(Objects.requireNonNull(ctx, "Audit context must not be null"));
    }

    /**
     * Возвращает текущий контекст или фолбэк‑контекст (при отсутствующем/пустом контексте).
     */
    public static AuditContext currentOrFallback() {
        // Извлекаем контекст
        final AuditContext ctx = CTX.get();

        // Если контекст null, возвращаем фолбэк контекст
        if (ctx == null) return FALLBACK_CTX;

        // (*) Если есть недостающие компоненты в контексте (актор или "via"), заполняем их фолбэками
        String actor = (ctx.actorId() != null && !ctx.actorId().isBlank())
                ? ctx.actorId() : FALLBACK_ACTOR;
        String via = (ctx.via() != null && !ctx.via().isBlank())
                ? ctx.via() : FALLBACK_VIA;

        if (actor.equals(ctx.actorId()) && via.equals(ctx.via())) {
            return ctx; // Если шаг (*) ничего не изменил
        } else {
            // Иначе создаем и возвращаем новый контекст
            return new AuditContext(actor, via);
        }
    }

    /**
     * Возвращает текущего актора или фолбэк.
     */
    public static String actorOrFallback() {
        return currentOrFallback().actorId();
    }

    /**
     * Возвращает текущее via или фолбэк.
     */
    public static String viaOrFallback() {
        return currentOrFallback().via();
    }

    /**
     * Очищает контекст текущего потока.
     */
    public static void clear() {
        CTX.remove();
    }

    /**
     * Выполняет функцию с заданным {@link AuditContext} и возвращает её результат.
     *
     * <p>Гарантированно восстанавливает прежний контекст (или очищает, если его не было).</p>
     *
     * @param ctx    временный контекст для текущего потока
     * @param action функция, выполняемая в этом контексте
     * @param <T>    тип результата
     * @return результат, возвращённый {@code action}
     */
    public static <T> T runWith(AuditContext ctx, Supplier<T> action) {
        Objects.requireNonNull(ctx, "ctx must not be null");
        Objects.requireNonNull(action, "action must not be null");

        final AuditContext prev = CTX.get();

        try {
            set(ctx);
            return action.get();
        } finally {
            if (prev != null) CTX.set(prev);
            else clear();
        }
    }

    /**
     * Выполняет действие с заданным {@link AuditContext} без результата.
     *
     * <p>Гарантированно восстанавливает прежний контекст (или очищает, если его не было).</p>
     *
     * @param ctx    временный контекст для текущего потока
     * @param action действие, выполняемое в этом контексте
     */
    public static void runWith(AuditContext ctx, Runnable action) {
        Objects.requireNonNull(ctx, "ctx must not be null");
        Objects.requireNonNull(action, "action must not be null");

        final AuditContext prev = CTX.get();

        try {
            set(ctx);
            action.run();
        } finally {
            if (prev != null) CTX.set(prev);
            else clear();
        }
    }
}
