package com.alligator.market.backend.common.persistence.jpa.constraint;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты класса {@link DbErrors} для проверки распознавания нарушений ограничений БД.
 */
@Tag("dev")
class DbErrorsTest {

    /**
     * Должен вернуть true так как это “идеальный” вариант алгоритма:
     * в cause-цепочке есть Hibernate {@link ConstraintViolationException},
     * и имя ограничения доступно через {@link ConstraintViolationException#getConstraintName()}.
     */
    @Test
    void shouldDetectByHibernateConstraintName() {
        SQLException sqlEx = new SQLException("duplicate key");
        ConstraintViolationException cve =
                new ConstraintViolationException("violated", sqlEx, "uq_some_column");

        RuntimeException ex = new RuntimeException("wrap", cve);

        assertTrue(DbErrors.isViolationOf(ex, "UQ_SOME_COLUMN")); // <-- Заглавными буквами для проверки, что регистр не влияет
    }

    /**
     * Должен вернуть true по варианту "фолбэк" алгоритма:
     * имя ограничения не доступно структурно, но присутствует в тексте сообщения одной из причин в cause-цепочке.
     */
    @Test
    void shouldDetectByMessageFallback() {
        RuntimeException root = new RuntimeException("violates constraint uq_some_column");
        RuntimeException ex = new RuntimeException("wrap", root);

        assertTrue(DbErrors.isViolationOf(ex, "uq_some_column"));
    }

    /**
     * Должен вернуть false так как имя ограничения не встречается ни в {@link ConstraintViolationException#getConstraintName()},
     * ни в сообщениях исключений в cause-цепочке.
     */
    @Test
    void shouldReturnFalseWhenNotMatched() {
        RuntimeException ex = new RuntimeException("some error");
        assertFalse(DbErrors.isViolationOf(ex, "uq_some_column"));
    }

    /**
     * Должен корректно завершаться при циклической cause-цепочке (защита от зацикливания) и вернуть false,
     * если совпадение не найдено.
     */
    @SuppressWarnings("UnnecessaryInitCause")
    @Test
    void shouldNotHangOnCyclicCauseChain() {
        RuntimeException a = new RuntimeException("a");
        RuntimeException b = new RuntimeException("b");

        // Искусственно создаем циклическую cause-цепочку
        a.initCause(b);
        b.initCause(a);

        assertFalse(DbErrors.isViolationOf(a, "uq_some_column"));
    }
}
