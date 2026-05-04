package com.alligator.market.domain.shared.vo;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Общий нормализатор строковых value objects доменного слоя.
 */
public final class StringValueNormalizer {

    private StringValueNormalizer() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Options.Builder options() {
        return Options.builder();
    }

    public static String normalize(String raw, String fieldName, Options options) {
        Objects.requireNonNull(fieldName, "fieldName must not be null");
        Objects.requireNonNull(options, "options must not be null");

        String normalized = requireNonBlank(raw, fieldName);
        normalized = applyCasePolicy(normalized, options);

        validateLength(normalized, fieldName, options);
        validateControlCharacters(normalized, fieldName, options);
        validatePattern(normalized, fieldName, options);

        return normalized;
    }

    private static String requireNonBlank(String raw, String fieldName) {
        Objects.requireNonNull(raw, fieldName + " must not be null");

        String normalized = raw.strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        return normalized;
    }

    private static String applyCasePolicy(String value, Options options) {
        if (options.uppercase()) {
            return value.toUpperCase(Locale.ROOT);
        }

        return value;
    }

    private static void validateLength(String value, String fieldName, Options options) {
        Integer exactLength = options.exactLength();
        if (exactLength != null && value.length() != exactLength) {
            throw new IllegalArgumentException(fieldName + " length must be " + exactLength);
        }

        Integer maxLength = options.maxLength();
        if (maxLength != null && value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " length must be <= " + maxLength);
        }
    }

    private static void validateControlCharacters(String value, String fieldName, Options options) {
        if (options.rejectControlCharacters() && containsControlCharacter(value)) {
            throw new IllegalArgumentException(fieldName + " must not contain control characters");
        }
    }

    private static void validatePattern(String value, String fieldName, Options options) {
        Pattern pattern = options.pattern();
        if (pattern != null && !pattern.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    fieldName + " must match pattern " + options.patternDescription() + ": '" + value + "'"
            );
        }
    }

    private static boolean containsControlCharacter(String value) {
        return value.chars().anyMatch(Character::isISOControl);
    }

    public record Options(
            Integer maxLength,
            Integer exactLength,
            Pattern pattern,
            String patternDescription,
            boolean uppercase,
            boolean rejectControlCharacters
    ) {

        public Options {
            if (maxLength != null && maxLength <= 0) {
                throw new IllegalArgumentException("maxLength must be positive");
            }

            if (exactLength != null && exactLength <= 0) {
                throw new IllegalArgumentException("exactLength must be positive");
            }

            if (maxLength != null && exactLength != null) {
                throw new IllegalArgumentException("maxLength and exactLength must not be set together");
            }

            if (pattern != null) {
                Objects.requireNonNull(patternDescription, "patternDescription must not be null");
                if (patternDescription.isBlank()) {
                    throw new IllegalArgumentException("patternDescription must not be blank");
                }
            } else if (patternDescription != null) {
                throw new IllegalArgumentException("patternDescription requires pattern");
            }
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {

            private Integer maxLength;
            private Integer exactLength;
            private Pattern pattern;
            private String patternDescription;
            private boolean uppercase;
            private boolean rejectControlCharacters;

            private Builder() {
            }

            public Builder maxLength(int value) {
                this.maxLength = value;
                return this;
            }

            public Builder exactLength(int value) {
                this.exactLength = value;
                return this;
            }

            public Builder pattern(String regex, String description) {
                Objects.requireNonNull(regex, "regex must not be null");
                this.pattern = Pattern.compile(regex);
                this.patternDescription = description;
                return this;
            }

            public Builder uppercase() {
                this.uppercase = true;
                return this;
            }

            public Builder rejectControlCharacters() {
                this.rejectControlCharacters = true;
                return this;
            }

            public Options build() {
                return new Options(
                        maxLength,
                        exactLength,
                        pattern,
                        patternDescription,
                        uppercase,
                        rejectControlCharacters
                );
            }
        }
    }
}
