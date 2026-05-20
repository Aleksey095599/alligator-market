package com.alligator.market.domain.instrument.registry.runtime;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class SnapshotRuntimeInstrumentRegistry implements RuntimeInstrumentRegistry {
    private final Map<InstrumentCode, Instrument> instrumentsByCode;

    public SnapshotRuntimeInstrumentRegistry(List<? extends Instrument> instruments) {
        Objects.requireNonNull(instruments, "instruments must not be null");

        Map<InstrumentCode, Instrument> instrumentsMap = new LinkedHashMap<>();

        for (Instrument instrument : instruments) {
            Instrument instrumentToRegister = Objects.requireNonNull(
                    instrument,
                    "instrument must not be null"
            );
            InstrumentCode instrumentCode = Objects.requireNonNull(
                    instrumentToRegister.instrumentCode(),
                    "instrument.instrumentCode must not be null"
            );

            Instrument previous = instrumentsMap.put(instrumentCode, instrumentToRegister);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate runtime instrument code detected (instrumentCode=%s)"
                                .formatted(instrumentCode.value())
                );
            }
        }

        this.instrumentsByCode = Collections.unmodifiableMap(instrumentsMap);
    }

    @Override
    public Optional<Instrument> findByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return Optional.ofNullable(instrumentsByCode.get(instrumentCode));
    }

    @Override
    public Map<InstrumentCode, Instrument> instrumentsByCode() {
        return instrumentsByCode;
    }
}
