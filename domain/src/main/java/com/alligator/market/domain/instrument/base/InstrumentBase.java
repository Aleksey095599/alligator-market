package com.alligator.market.domain.instrument.base;

import com.alligator.market.domain.instrument.contract.Instrument;

/**
 * Базовая модель инструмента с equals/hashCode по внутреннему коду инструмента.
 */
public abstract class InstrumentBase implements Instrument {

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false; // Разные типы
        }
        Instrument that = (Instrument) o;
        return code().equals(that.code()); // Сравниваем коды
    }

    @Override
    public final int hashCode() {
        return code().hashCode();
    }
}
