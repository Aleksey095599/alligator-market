package com.alligator.market.domain.instrument.contract;

import java.util.Objects;

/**
 * Абстрактный каркас модели финансового инструмента.
 * Задает метод сравнения инструментов и расчета хэш значения инструмента.
 */
public abstract non-sealed class AbstractInstrument implements Instrument {

    /**
     * Сравниваем инструменты по коду и типу.
     */
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

    /**
     * Хэш по коду и типу.
     */
    @Override
    public final int hashCode() {
        return Objects.hash(code(), type());
    }
}
