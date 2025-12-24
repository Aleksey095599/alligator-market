package com.alligator.market.backend.common.persistence.jpa.constraint;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* Тесты для проверки распознавания нарушений ограничений БД. */
class DbErrorsTest {

    @Test
    void shouldDetectByHibernateConstraintName() {
        SQLException sqlEx = new SQLException("duplicate key");
        ConstraintViolationException cve =
                new ConstraintViolationException("violated", sqlEx, "uq_some_column");

        RuntimeException ex = new RuntimeException("wrap", cve);

        assertTrue(DbErrors.isViolationOf(ex, "UQ_SOME_COLUMN"));
    }

    @Test
    void shouldDetectByMessageFallback() {
        RuntimeException root = new RuntimeException("violates constraint uq_some_column");
        RuntimeException ex = new RuntimeException("wrap", root);

        assertTrue(DbErrors.isViolationOf(ex, "uq_some_column"));
    }

    @Test
    void shouldReturnFalseWhenNotMatched() {
        RuntimeException ex = new RuntimeException("some error");
        assertFalse(DbErrors.isViolationOf(ex, "uq_some_column"));
    }

    @Test
    void shouldNotHangOnCyclicCauseChain() {
        RuntimeException a = new RuntimeException("a");
        RuntimeException b = new RuntimeException("b");

        a.initCause(b);
        b.initCause(a); // цикл

        assertFalse(DbErrors.isViolationOf(a, "uq_some_column"));
    }
}
