package com.alligator.market.backend.common.persistence.jpa.constraint;

import org.hibernate.exception.ConstraintViolationException;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Утилита для распознавания нарушения конкретного ограничения БД по имени этого ограничения.
 *
 * <p><b>Пример:</b> Предположим, что в БД есть таблица, в которой задано ограничение (UNIQUE) на уникальность
 * значений в одной из колонок. В данном приложении, согласно лучшим практикам, каждому ограничению назначается
 * уникальное имя. Поэтому в нашем примере ограничению будет задано уникальное имя, например, "uq_some_column".
 * Если при операции сохранения записи в таблицу было выброшено исключение, мы можем с помощью данной утилиты
 * просканировать cause-цепочку данного исключения с целью найти в ней "uq_some_column". Если имя "uq_some_column"
 * найдено в cause-цепочке исключения, с высокой долей вероятности исключение вызвано нарушением именно этого
 * ограничения.</p>
 *
 * <p><b>Преимущества подхода:</b> Коды ошибок и SQLState зависят от конкретной СУБД, а имя ограничения уникально
 * и стабильно.</p>
 *
 * <p><b>Потокобезопасность:</b> Класс stateless, методы не имеют побочных эффектов и потокобезопасны.</p>
 *
 * <p><b>Ограничения:</b> Метод опирается на наличие осмысленно заданного уникального имени ограничения.</p>
 */
public final class DbErrors {

    /* Конструктор: утилитарный класс — инстанцирование запрещено. */
    private DbErrors() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Возвращает {@code true}, если в cause-цепочке {@code ex} обнаружено имя ограничения {@code constraintName}.
     *
     * <p><b>Алгоритм поиска:</b></p>
     * <ol>
     *   <li>1) Нормализует {@code constraintName} (обрезает пробелы) и проверяет, что оно не пустое.</li>
     *   <li>2) Обходит cause-цепочку исключения (с защитой от циклов).</li>
     *   <li>3) Для каждого элемента цепочки:
     *       <br/>3.1) “идеальный” вариант: если доступно имя ограничения через
     *       {@link ConstraintViolationException#getConstraintName()}, сравнивает его с {@code constraintName}
     *       без учёта регистра и при совпадении сразу возвращает {@code true};
     *       <br/>3.2) фолбэк: ищет {@code constraintName} (без учёта регистра) в {@link Throwable#getMessage()},
     *       запоминает совпадение и продолжает обход цепочки.</li>
     *   <li>4) Если “идеальный” вариант не сработал, возвращает результат фолбэка.</li>
     * </ol>
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

        // 1) Нормализуем {@code constraintName} и проверяем, что оно не пустое.
        final String needle = constraintName.trim();
        if (needle.isEmpty()) {
            throw new IllegalArgumentException("constraintName must not be blank");
        }

        // Флаг совпадения по сообщениям (фолбэк)
        boolean matchedByMessage = false;

        // Защита от потенциально циклической cause-цепочки (маловероятно, но на крайний случай)
        final Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());

        // 2) Обходим cause-цепочку исключения
        for (Throwable t = ex; t != null && visited.add(t); t = t.getCause()) {
            // 3) На каждом шаге пытаемся распознать нарушение ограничения:

            // 3.1) "Идеальный" вариант – Hibernate смог поймать и обернуть исключение в ConstraintViolationException =>
            //      Перебираем узлы исключения, ищем ConstraintViolationException, извлекаем имя ConstraintViolationException
            //      и сравниваем его с needle.
            if (t instanceof ConstraintViolationException cve) {
                final String name = cve.getConstraintName();
                if (name != null && name.trim().equalsIgnoreCase(needle)) {
                    return true;
                    // Сразу же выходим, так как это "идеальный" вариант
                }
            }

            // 3.2) Фолбэк – некоторые драйверы/диалекты/обёртки не пробрасывают имя ограничения в getConstraintName(),
            //    но включают его в текст сообщения => Ищем имя ограничения в сообщениях причин
            if (!matchedByMessage && containsIgnoreCase(t.getMessage(), needle)) {
                matchedByMessage = true;
                // Не выходим, даём шанс найти "идеальный" вариант глубже по цепочке
            }
        }
        // 4) Если идеальный вариант не сработал, возвращаем результат фолбэка
        return matchedByMessage;
    }

    /**
     * Проверяет, содержит ли строка {@code haystack} подстроку {@code needle} без учёта регистра.
     *
     * <p>Безопасен к {@code null}: если любой входящий аргумент равен {@code null}, возвращает {@code false}.</p>
     */
    private static boolean containsIgnoreCase(String haystack, String needle) {
        if (haystack == null || needle == null) {
            return false;
        }
        final int hLen = haystack.length();
        final int nLen = needle.length();
        if (nLen == 0 || nLen > hLen) {
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
