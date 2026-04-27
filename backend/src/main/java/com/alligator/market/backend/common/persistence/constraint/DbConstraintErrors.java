package com.alligator.market.backend.common.persistence.constraint;

import org.hibernate.exception.ConstraintViolationException;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Утилита для распознавания нарушения именованного ограничения БД.
 *
 * <p>Используется на persistence-границе для перевода низкоуровневых DB errors
 * в application/domain-specific exceptions.</p>
 *
 * <p>Пример: Предположим, что в БД есть таблица, в которой задано ограничение (UNIQUE) на уникальность
 * значений в одной из колонок. В данном приложении, согласно лучшим практикам, каждому ограничению назначается
 * уникальное имя. Поэтому в нашем примере ограничению будет присвоено уникальное имя, например, "uq_some_column".
 * Если при операции сохранения записи в таблицу будет выброшено исключение, мы сможем с помощью данной утилиты
 * просканировать cause-цепочку исключения с целью найти в ней "uq_some_column". Если имя "uq_some_column"
 * найдено в cause-цепочке исключения, с высокой долей вероятности исключение вызвано нарушением именно этого
 * ограничения.</p>
 *
 * <p>Преимущества подхода: Коды ошибок и SQLState зависят от конкретной СУБД, а имя ограничения уникально
 * и стабильно.</p>
 *
 * <p>Ограничения: Метод опирается на наличие осмысленно заданного уникального имени ограничения.</p>
 */
public final class DbConstraintErrors {

    /**
     * Приватный конструктор: запрещаем создание экземпляров, так как класс является утилитой.
     */
    private DbConstraintErrors() {
        throw new UnsupportedOperationException("Utility class instantiation is not allowed");
    }

    /**
     * Ищет в cause-цепочке {@code ex} имя ограничения {@code constraintName}.
     *
     * @param ex             исключение, возникшее при операции с БД
     * @param constraintName имя ограничения в БД
     * @return {@code true}, если имя ограничения обнаружено; иначе {@code false}
     * @throws NullPointerException     если {@code ex} или {@code constraintName} равны {@code null}
     * @throws IllegalArgumentException если {@code constraintName} пустая строка или состоит только из пробелов
     */
    public static boolean isViolationOf(Throwable ex, String constraintName) {
        Objects.requireNonNull(ex, "ex must not be null");
        Objects.requireNonNull(constraintName, "constraintName must not be null");

        // 1) Нормализуем constraintName и проверяем, что не пустое
        final String needle = constraintName.strip();
        if (needle.isEmpty()) {
            throw new IllegalArgumentException("constraintName must not be blank");
        }

        // Флаг реализации варианта "фолбэк"
        boolean matchedByMessage = false;

        // Защита от потенциально циклической cause-цепочки (маловероятно, на крайний случай)
        final Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());

        // 2) Обходим cause-цепочку исключения
        for (Throwable t = ex; t != null && visited.add(t); t = t.getCause()) {

            // 3.1) "Идеальный" вариант – Hibernate смог поймать и обернуть исключение в ConstraintViolationException.
            //      Тогда извлекаем имя ConstraintViolationException и сравниваем с needle.
            if (t instanceof ConstraintViolationException cve) {
                final String name = cve.getConstraintName();
                if (name != null && name.strip().equalsIgnoreCase(needle)) {
                    return true;
                    // Сразу же выходим, так как это "идеальный" вариант
                }
            }

            // 3.2) "Фолбэк" вариант – некоторые драйверы/диалекты/обёртки не пробрасывают имя ограничения
            //      в getConstraintName(), но включают его в текст сообщения. Тогда ищем needle в сообщениях причин.
            if (!matchedByMessage && containsIgnoreCase(t.getMessage(), needle)) {
                matchedByMessage = true; // <-- Фиксируем, что вариант "фолбэк" сработал.
                // Не выходим, даём шанс сработать "идеальному" варианту глубже по цепочке
            }
        }
        // 4) Если идеальный вариант не сработал, возвращаем результат фолбэка
        return matchedByMessage;
    }

    /**
     * Проверяет, содержит ли строка {@code haystack} подстроку {@code needle} без учёта регистра.
     */
    private static boolean containsIgnoreCase(String haystack, String needle) {
        // needle (null/blank) считаем ошибкой — это эквивалент невалидного имени ограничения.
        Objects.requireNonNull(needle, "needle must not be null");
        if (needle.isBlank()) {
            throw new IllegalArgumentException("needle must not be blank");
        }

        // haystack может быть null (Throwable#getMessage() иногда возвращает null) — это не ошибка,
        // трактуем как отсутствие совпадения
        if (haystack == null) {
            return false;
        }

        final int hLen = haystack.length();
        final int nLen = needle.length();
        if (nLen > hLen) {
            return false;
        }
        for (int i = 0; i <= hLen - nLen; i++) {
            if (haystack.regionMatches(true, i, needle, 0, nLen)) {
                return true;
            }
        }
        return false;
    }
}
