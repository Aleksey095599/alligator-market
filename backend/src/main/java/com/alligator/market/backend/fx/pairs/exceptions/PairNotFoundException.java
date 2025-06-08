package com.alligator.market.backend.fx.pairs.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class PairNotFoundException extends EntityNotFoundException {
    public PairNotFoundException(String pair) {
        super("Pair '%s' not found".formatted(pair));
    }
}
