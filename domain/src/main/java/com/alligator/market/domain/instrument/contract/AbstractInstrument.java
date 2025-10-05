package com.alligator.market.domain.instrument.contract;

import java.util.Objects;

/**
 * Абстрактный каркас модели финансового инструмента.
 * Задает метод сравнения инструментов и расчета хэш значения инструмента.
 */
public abstract non-sealed class AbstractInstrument implements Instrument {

    /** Сравниваем инструменты по коду. */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Instrument that)) {
            return false;
        }
        return code().equals(that.code());
    }

    /** Хэш по коду. */
    @Override
    public final int hashCode() {
        return Objects.hash(code());
    }
}
