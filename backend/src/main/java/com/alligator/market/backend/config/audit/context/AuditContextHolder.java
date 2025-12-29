package com.alligator.market.backend.config.audit.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * ThreadLocal-холдер контекста аудита.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) // <-- Конструктор только внутри самого класса
public final class AuditContextHolder {

    /* Зарезервированный системный актор для внутренних процессов. */
    public static final String SYSTEM_ACTOR = "system";

    /* Фолбэк для актора при отсутствующем/пустом контексте. */
    public static final String FALLBACK_ACTOR = "fallback_actor";

    /* Фолбэк для via при отсутствующем/пустом контексте. */
    public static final String FALLBACK_VIA = "fallback_via";

    /* Пред-созданный фолбэк‑контекст (record иммутабелен). */
    private static final AuditContext FALLBACK_CTX =
            new AuditContext(FALLBACK_ACTOR, FALLBACK_VIA);

    /* Переменная на поток для хранения текущего {@link AuditContext}. */
    private static final ThreadLocal<AuditContext> CTX = new ThreadLocal<>();

    /**
     * Устанавливает контекст для текущего потока.
     */
    public static void set(@lombok.NonNull AuditContext ctx) {
        CTX.set(ctx);
    }

    /**
     * Возвращает текущий контекст, подставляя фолбэки для пустых компонентов.
     * Никогда не возвращает {@code null}.
     */
    public static AuditContext currentOrFallback() {
        // Извлекаем контекст
        final AuditContext ctx = CTX.get();

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
     * Выполняет функцию в заданном {@link AuditContext} и возвращает её результат.
     * Гарантированно восстанавливает прежний контекст (или очищает, если его не было).
     *
     * @param ctx    временный контекст для текущего потока
     * @param action функция, выполняемая в этом контексте
     * @param <T>    тип результата
     * @return результат, возвращённый {@code action}
     */
    @SuppressWarnings("unused")
    public static <T> T runWith(AuditContext ctx, java.util.function.Supplier<T> action) {
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
     * Выполняет действие в заданном {@link AuditContext} без результата.
     * Гарантированно восстанавливает прежний контекст (или очищает, если его не было).
     *
     * @param ctx    временный контекст для текущего потока
     * @param action действие, выполняемое в этом контексте
     */
    public static void runWith(AuditContext ctx, Runnable action) {
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
