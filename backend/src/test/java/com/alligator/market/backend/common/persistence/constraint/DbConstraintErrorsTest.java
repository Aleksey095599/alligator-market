package com.alligator.market.backend.common.persistence.constraint;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты класса {@link DbConstraintErrors} для проверки распознавания нарушений ограничений БД.
 */
@Tag("dev")
class DbConstraintErrorsTest {

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

        assertTrue(DbConstraintErrors.isViolationOf(ex, "UQ_SOME_COLUMN")); // <-- Заглавными буквами для проверки, что регистр не влияет
    }

    /**
     * Должен вернуть true по варианту "фолбэк" алгоритма:
     * имя ограничения не доступно структурно, но присутствует в тексте сообщения одной из причин в cause-цепочке.
     */
    @Test
    void shouldDetectByMessageFallback() {
        RuntimeException root = new RuntimeException("violates constraint uq_some_column");
        RuntimeException ex = new RuntimeException("wrap", root);

        assertTrue(DbConstraintErrors.isViolationOf(ex, "uq_some_column"));
    }

    /**
     * Должен вернуть false так как имя ограничения не встречается ни в {@link ConstraintViolationException#getConstraintName()},
     * ни в сообщениях исключений в cause-цепочке.
     */
    @Test
    void shouldReturnFalseWhenNotMatched() {
        RuntimeException ex = new RuntimeException("some error");
        assertFalse(DbConstraintErrors.isViolationOf(ex, "uq_some_column"));
    }

    /**
     * Должен вернуть true, если совпадение по сообщению найдено только в другом регистре.
     */
    @Test
    void shouldCompareConstraintNameCaseInsensitiveInMessages() {
        RuntimeException ex = new RuntimeException("violates constraint UQ_SOME_COLUMN");

        assertTrue(DbConstraintErrors.isViolationOf(ex, "uq_some_column"));
    }

    @Test
    void shouldThrowWhenExceptionIsNull() {
        assertThrows(NullPointerException.class, () -> DbConstraintErrors.isViolationOf(null, "uq_some_column"));
    }

    @Test
    void shouldThrowWhenConstraintNameIsNull() {
        RuntimeException ex = new RuntimeException("error");
        assertThrows(NullPointerException.class, () -> DbConstraintErrors.isViolationOf(ex, null));
    }

    @Test
    void shouldThrowWhenConstraintNameIsBlank() {
        RuntimeException ex = new RuntimeException("error");
        assertThrows(IllegalArgumentException.class, () -> DbConstraintErrors.isViolationOf(ex, "   "));
    }

    /**
     * Должен корректно завершаться при циклической cause-цепочке (защита от зацикливания).
     */
    @SuppressWarnings("UnnecessaryInitCause")
    @Test
    void shouldHandleCyclicCauseChainSafely() {
        RuntimeException a = new RuntimeException("a");
        RuntimeException b = new RuntimeException("b");

        // Искусственно создаем циклическую cause-цепочку
        a.initCause(b);
        b.initCause(a);

        assertDoesNotThrow(() -> DbConstraintErrors.isViolationOf(a, "uq_some_column"));
        assertFalse(DbConstraintErrors.isViolationOf(a, "uq_some_column"));
    }
}
