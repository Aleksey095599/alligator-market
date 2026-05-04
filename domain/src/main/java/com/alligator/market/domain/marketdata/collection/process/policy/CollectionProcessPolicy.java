package com.alligator.market.domain.marketdata.collection.process.policy;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;
import java.util.Set;

/**
 * Политика процесса сбора рыночных данных: иммутабельные параметры для бизнес-логики.
 *
 * @param supportedInstrumentCodes внутренние инструменты приложения, которые может обслуживать процесс
 */
public record CollectionProcessPolicy(
        Set<InstrumentCode> supportedInstrumentCodes
) {

    public CollectionProcessPolicy {
        supportedInstrumentCodes = copySupportedInstrumentCodes(supportedInstrumentCodes);
    }

    private static Set<InstrumentCode> copySupportedInstrumentCodes(Set<InstrumentCode> raw) {
        Objects.requireNonNull(raw, "supportedInstrumentCodes must not be null");

        if (raw.isEmpty()) {
            throw new IllegalArgumentException("supportedInstrumentCodes must not be empty");
        }

        for (InstrumentCode instrumentCode : raw) {
            Objects.requireNonNull(instrumentCode, "supportedInstrumentCodes must not contain null");
        }

        return Set.copyOf(raw);
    }
}
