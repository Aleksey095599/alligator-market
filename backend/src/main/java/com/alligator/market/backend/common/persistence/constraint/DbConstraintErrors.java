package com.alligator.market.backend.common.persistence.constraint;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Утилита для распознавания нарушения именованного ограничения БД PostgreSQL.
 *
 * <p>Назначение: Используется на persistence-границе для перевода низкоуровневых DB errors в
 * application/domain-specific exceptions.</p>
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
 * <p>Ограничения: Утилита ориентирована на PostgreSQL; метод опирается на наличие осмысленно заданного уникального
 * имени ограничения.</p>
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

        // Флаг реализации варианта "fallback"
        boolean matchedByMessage = false;

        // Защита от потенциально циклической cause-цепочки (маловероятно, на крайний случай)
        final Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());

        // 2) Обходим cause-цепочку исключения
        for (Throwable t = ex; t != null && visited.add(t); t = t.getCause()) {

            // 3.1) Предпочтительный вариант для PostgreSQL+jOOQ:
            //      драйвер пробрасывает PSQLException, где имя ограничения доступно структурно.
            if (t instanceof PSQLException psqlEx) {
                final ServerErrorMessage serverErrorMessage = psqlEx.getServerErrorMessage();
                final String name = serverErrorMessage == null ? null : serverErrorMessage.getConstraint();
                if (name != null && name.strip().equalsIgnoreCase(needle)) {
                    return true;
                    // Сразу же выходим, так как это приоритетный структурный вариант
                }
            }

            // 3.2) Fallback-вариант – некоторые драйверы/диалекты/обёртки не пробрасывают имя ограничения
            //      в getConstraintName(), но включают его в текст сообщения. Тогда ищем needle в сообщениях причин.
            if (!matchedByMessage && containsConstraintName(t.getMessage(), needle)) {
                matchedByMessage = true; // <-- Фиксируем, что вариант "fallback" сработал.
                // Не выходим, даём шанс сработать "идеальному" варианту глубже по цепочке
            }
        }
        // 4) Если идеальный вариант не сработал, возвращаем результат fallback
        return matchedByMessage;
    }

    /**
     * Проверяет, содержит ли строка {@code haystack} имя ограничения {@code constraintName}
     * без учёта регистра и с границами SQL-идентификатора.
     */
    private static boolean containsConstraintName(String haystack, String constraintName) {
        // constraintName (null/blank) считаем ошибкой — это эквивалент невалидного имени ограничения.
        Objects.requireNonNull(constraintName, "constraintName must not be null");

        final String needle = constraintName.strip();
        if (needle.isEmpty()) {
            throw new IllegalArgumentException("constraintName must not be blank");
        }

        // haystack может быть null (Throwable#getMessage() иногда возвращает null) — это не ошибка,
        // трактуем как отсутствие совпадения
        if (haystack == null) {
            return false;
        }

        final int nLen = needle.length();
        for (int i = indexOfIgnoreCase(haystack, needle, 0); i >= 0; i = indexOfIgnoreCase(haystack, needle, i + 1)) {
            if (isIdentifierBoundary(haystack, i - 1) && isIdentifierBoundary(haystack, i + nLen)) {
                return true;
            }
        }
        return false;
    }

    private static int indexOfIgnoreCase(String haystack, String needle, int fromIndex) {
        final int hLen = haystack.length();
        final int nLen = needle.length();
        if (nLen > hLen) {
            return -1;
        }

        for (int i = Math.max(0, fromIndex); i <= hLen - nLen; i++) {
            if (haystack.regionMatches(true, i, needle, 0, nLen)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isIdentifierBoundary(String text, int index) {
        if (index < 0 || index >= text.length()) {
            return true;
        }

        char ch = text.charAt(index);
        return !Character.isLetterOrDigit(ch) && ch != '_' && ch != '$';
    }
}
