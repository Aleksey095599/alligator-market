package com.alligator.market.domain.instrument.contract;

import java.util.Objects;

/**
 * Базовая модель инструмента с equals/hashCode по коду и типу инструмента.
 */
public abstract non-sealed class AbstractInstrument implements Instrument {

    /** Сравниваем инструменты по коду и типу. */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Instrument that)) {
            return false;
        }
        return code().equals(that.code()) && type() == that.type(); // Сравниваем код и тип
    }

    /** Хэш по коду и типу. */
    @Override
    public final int hashCode() {
        return Objects.hash(code(), type());
    }
}
