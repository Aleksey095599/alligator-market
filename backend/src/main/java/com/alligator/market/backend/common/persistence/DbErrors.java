package com.alligator.market.backend.common.persistence;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.NestedExceptionUtils;

import java.util.Locale;
import java.util.Objects;

/**
 * Утилита для распознавания ошибок БД по имени ограничения.
 *
 * <p><b>Задача:</b> понять, что причиной исключения стало нарушение <i>конкретного</i> ограничения БД
 * (например, уникального ключа с именем {@code uq_instrument_code}).</p>
 *
 * <p><b>Как работает:</b></p>
 * <ol>
 *   <li>Проходит по всей цепочке причин исключения и ищет {@link ConstraintViolationException} Hibernate.
 *       Если Hibernate сообщает имя ограничения через {@link ConstraintViolationException#getConstraintName()},
 *       сравнивает его (без учёта регистра) с указанным именем — это самый надёжный признак.</li>
 *   <li>Если имени нет или тип причины другой — выполняет «фолбэк»:
 *       ищет в текстах сообщений причин вхождение имени ограничения (без учёта регистра).
 *       Это покрывает драйверы/диалекты, которые включают имя ограничения в текст ошибки
 *       (Oracle, MySQL и т.п.).</li>
 * </ol>
 *
 * <p><b>Зачем имя ограничения:</b> коды/SQLState различаются у разных СУБД,
 * а <i>имя вашего ограничения</i> — стабильно и контролируется вами (задавайте его явно в DDL/аннотациях).</p>
 *
 * <p><b>Типичное применение (в адаптере репозитория):</b></p>
 * <pre>{@code
 * try {
 *     var saved = jpaRepository.saveAndFlush(entity);
 *     return mapper.toDomain(saved);
 * } catch (DataIntegrityViolationException ex) {
 *     if (DbErrors.isViolationOf(ex, "uq_instrument_code")) {
 *         throw new FxSpotAlreadyExistsException(model.instrumentCode());
 *     }
 *     throw new FxSpotCreateException(model.instrumentCode(), ex);
 * }
 * }</pre>
 *
 * <p><b>Ограничения:</b> метод опирается на наличие осмысленного имени ограничения.
 * Если имя не задано/не попадает в исключение, возможен возврат {@code false} даже при нарушении,
 * и такой кейс должен обрабатываться как «техническая ошибка».</p>
 */
public final class DbErrors {

    /** Утилитарный класс — инстанцирование запрещено. */
    private DbErrors() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Возвращает {@code true}, если {@code ex} с высокой вероятностью вызван нарушением
     * ограничения с именем {@code constraintName}.
     *
     * <p>Алгоритм:
     * <ol>
     *   <li>По всей цепочке причин ищем {@link ConstraintViolationException} и сверяем {@code getConstraintName}.</li>
     *   <li>Если не найдено — ищем (без учёта регистра) вхождение {@code constraintName} в сообщениях причин,
     *       начиная с «самой специфичной» (root cause).</li>
     * </ol>
     *
     * @param ex             исключение, полученное при операции с БД (например, при saveAndFlush)
     * @param constraintName точное имя ограничения в БД (например, {@code uq_instrument_code})
     * @return {@code true}, если нарушение данного ограничения распознано; иначе {@code false}
     * @throws NullPointerException     если {@code ex} или {@code constraintName} равны {@code null}
     * @throws IllegalArgumentException если {@code constraintName} пустая строка
     */
    public static boolean isViolationOf(Throwable ex, String constraintName) {
        Objects.requireNonNull(ex, "ex must not be null");
        Objects.requireNonNull(constraintName, "constraintName must not be null");

        final String needle = constraintName.trim();
        if (needle.isEmpty()) {
            throw new IllegalArgumentException("constraintName must not be blank");
        }
        final String needleLower = needle.toLowerCase(Locale.ROOT);

        // 1) Ищем Hibernate ConstraintViolationException в цепочке причин и сверяем имя ограничения.
        for (Throwable t = ex; t != null; t = t.getCause()) {
            if (t instanceof ConstraintViolationException cve) {
                final String name = cve.getConstraintName();
                if (name != null && name.equalsIgnoreCase(needle)) {
                    return true; // ← самый надёжный путь: явное имя констрейнта от Hibernate
                }
            }
        }

        // 2) Фолбэк: ищем упоминание имени ограничения в сообщениях причин (root-first обычно информативней).
        for (Throwable t = NestedExceptionUtils.getMostSpecificCause(ex); t != null; t = t.getCause()) {
            final String msg = t.getMessage();
            if (containsIgnoreCase(msg, needleLower)) {
                return true; // ← имя констрейнта встретилось в тексте ошибки драйвера/диалекта
            }
        }

        // 3) Не распознали нарушение именно этого ограничения.
        return false;
    }

    /** Проверка "строка содержит подстроку" без учёта регистра; безопасна к null. */
    private static boolean containsIgnoreCase(String haystack, String needleLower) {
        return haystack != null && haystack.toLowerCase(Locale.ROOT).contains(needleLower);
    }
}
