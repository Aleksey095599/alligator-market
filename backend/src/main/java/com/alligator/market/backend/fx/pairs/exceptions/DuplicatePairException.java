package com.alligator.market.backend.fx.pairs.exceptions;

public class DuplicatePairException extends RuntimeException {
    public DuplicatePairException(String pair) {
        super("Pair '%s' already exists".formatted(pair));
    }
}
