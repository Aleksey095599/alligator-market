package com.alligator.market.backend.common.persistence.jpa.constraint;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.NestedExceptionUtils;

import java.util.Locale;
import java.util.Objects;

/**
 * Утилита для распознавания нарушений конкретных ограничений БД по имени ограничения.
 *
 * <p><b>Назначение:</b></p>
 * <p>Определить, что брошенное исключение обусловлено нарушением
 * <i>конкретного</i> ограничения (например, уникального ключа с именем {@code uq_instrument_code}),
 * чтобы корректно транслировать инфраструктурную ошибку БД в доменную (например, {@code AlreadyExists}).</p>
 *
 * <p><b>Алгоритм распознавания:</b></p>
 * <ol>
 *   <li>Проходит по всей cause-цепочке исключения и ищет {@link ConstraintViolationException} (Hibernate).
 *       Если найдено и {@link ConstraintViolationException#getConstraintName()} возвращает имя ограничения,
 *       сравнивает его с ожидаемым без учёта регистра — это самый надёжный признак.</li>
 *   <li>Если имя не доступно или тип причины иной — используется фолбэк:
 *       анализируются сообщения причин (начиная с наиболее специфичной — {@link NestedExceptionUtils#getMostSpecificCause(Throwable)}),
 *       выполняется поиск вхождения имени ограничения без учёта регистра. Это покрывает диалекты/драйверы,
 *       которые включают имя ограничения в текст ошибки (Oracle, MySQL и др.).</li>
 * </ol>
 *
 * <p><b>Преимущества подхода:</b></p>
 * <p>Коды ошибок и SQLState зависят от СУБД, а имя ограничения
 * стабильно и контролируется вами. Рекомендуется явно задавать имена ограничений в DDL/аннотациях.</p>
 *
 * <p><b>Потокобезопасность:</b></p>
 * <p>Класс stateless, методы не имеют побочных эффектов и потокобезопасны.</p>
 *
 * <p><b>Ограничения:</b></p>
 * <p>Метод опирается на наличие осмысленного имени ограничения. Если драйвер/диалект
 * не передаёт имя и оно не фигурирует в сообщении исключения, метод вернёт {@code false};
 * такой случай следует трактовать как техническую ошибку сохранения.</p>
 *
 * @see ConstraintViolationException
 * @see NestedExceptionUtils
 */
public final class DbErrors {

    /**
     * Утилитарный класс — инстанцирование запрещено.
     */
    private DbErrors() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Возвращает {@code true}, если {@code ex} с высокой вероятностью вызван нарушением
     * ограничения с именем {@code constraintName}.
     *
     * <p><b>Порядок распознавания:</b></p>
     * <ol>
     *   <li>(1) поиск {@link ConstraintViolationException} в cause-цепочке и сравнение имени ограничения;</li>
     *   <li>(2) фолбэк — поиск имени ограничения (без учёта регистра) в сообщениях причин, начиная с root cause.</li>
     * </ol>
     *
     * @param ex             исключение, возникшее при операции с БД (например, при {@code saveAndFlush})
     * @param constraintName точное имя ограничения в БД (например, {@code uq_instrument_code})
     * @return {@code true}, если распознано нарушение данного ограничения; иначе {@code false}
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

        // 1) Поиск Hibernate-исключения с явным именем ограничение в цепочке причин
        for (Throwable t = ex; t != null; t = t.getCause()) {
            if (t instanceof ConstraintViolationException cve) {
                final String name = cve.getConstraintName();
                if (name != null && name.equalsIgnoreCase(needle)) {
                    return true; // <-- Самый надёжный признак: имя констрейнта от Hibernate
                }
            }
        }

        // 2) Фолбэк: поиск упоминания имени ограничения в текстах сообщений причин (root-first)
        for (Throwable t = NestedExceptionUtils.getMostSpecificCause(ex); t != null; t = t.getCause()) {
            final String msg = t.getMessage();
            if (containsIgnoreCase(msg, needleLower)) {
                return true;
            }
        }

        // 3) Не удалось однозначно распознать нарушение указанного ограничения
        return false;
    }

    /**
     * Проверка "строка содержит подстроку" без учёта регистра; безопасна к {@code null}.
     */
    private static boolean containsIgnoreCase(String haystack, String needleLower) {
        return haystack != null && haystack.toLowerCase(Locale.ROOT).contains(needleLower);
        // Примечание: Locale.ROOT гарантирует детерминированное сравнение для латинских имён констрейнтов.
    }
}
