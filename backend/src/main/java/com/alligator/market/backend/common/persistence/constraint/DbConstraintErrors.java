package com.alligator.market.backend.common.persistence.constraint;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;

public final class DbConstraintErrors {
    private DbConstraintErrors() {
        throw new UnsupportedOperationException("Utility class instantiation is not allowed");
    }

    public static boolean isViolationOf(Throwable ex, String constraintName) {
        Objects.requireNonNull(ex, "ex must not be null");
        Objects.requireNonNull(constraintName, "constraintName must not be null");

        final String needle = constraintName.strip();
        if (needle.isEmpty()) {
            throw new IllegalArgumentException("constraintName must not be blank");
        }

        boolean matchedByMessage = false;

        final Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());

        for (Throwable t = ex; t != null && visited.add(t); t = t.getCause()) {
            // Prefer PostgreSQL's structured constraint name when the driver exposes it.
            if (t instanceof PSQLException psqlEx) {
                final ServerErrorMessage serverErrorMessage = psqlEx.getServerErrorMessage();
                final String name = serverErrorMessage == null ? null : serverErrorMessage.getConstraint();
                if (name != null && name.strip().equalsIgnoreCase(needle)) {
                    return true;
                }
            }

            // Some wrappers only preserve the constraint name in the message text.
            if (!matchedByMessage && containsConstraintName(t.getMessage(), needle)) {
                matchedByMessage = true;
            }
        }

        return matchedByMessage;
    }

    private static boolean containsConstraintName(String haystack, String constraintName) {
        Objects.requireNonNull(constraintName, "constraintName must not be null");

        final String needle = constraintName.strip();
        if (needle.isEmpty()) {
            throw new IllegalArgumentException("constraintName must not be blank");
        }

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
