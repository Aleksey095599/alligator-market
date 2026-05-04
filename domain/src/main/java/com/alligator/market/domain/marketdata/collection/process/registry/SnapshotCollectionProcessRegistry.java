package com.alligator.market.domain.marketdata.collection.process.registry;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.collection.process.MarketDataCollectionProcess;
import com.alligator.market.domain.marketdata.collection.process.passport.CollectionProcessPassport;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessCode;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessDisplayName;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация {@link CollectionProcessRegistry} в виде неизменяемого snapshot набора процессов сбора.
 *
 * <p>Инварианты, заданные в {@link CollectionProcessRegistry}, валидируются в конструкторе
 * и далее гарантируются на протяжении жизни экземпляра.</p>
 */
public final class SnapshotCollectionProcessRegistry implements CollectionProcessRegistry {

    private final Map<MarketDataCollectionProcessCode, MarketDataCollectionProcess> processesByCode;
    private final Map<MarketDataCollectionProcessCode, CollectionProcessPassport> passportsByCode;

    public SnapshotCollectionProcessRegistry(List<? extends MarketDataCollectionProcess> processes) {
        Objects.requireNonNull(processes, "processes must not be null");

        if (processes.isEmpty()) {
            throw new IllegalArgumentException("Collection process registry must contain at least one process");
        }

        Map<MarketDataCollectionProcessCode, MarketDataCollectionProcess> processesMap = new LinkedHashMap<>();
        Map<MarketDataCollectionProcessCode, CollectionProcessPassport> passportsMap = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketDataCollectionProcess process : processes) {
            Objects.requireNonNull(process, "process must not be null");

            MarketDataCollectionProcessCode code = Objects.requireNonNull(process.processCode(),
                    "process.processCode must not be null");

            CollectionProcessPassport passport = Objects.requireNonNull(process.passport(),
                    "process.passport must not be null");

            Objects.requireNonNull(process.policy(),
                    "process.policy must not be null");

            validateSupportedInstrumentCodes(process.supportedInstrumentCodes());

            MarketDataCollectionProcess previous = processesMap.put(code, process);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate collection process code detected (code=" + code.value() + ")"
                );
            }

            MarketDataCollectionProcessDisplayName displayName = Objects.requireNonNull(passport.displayName(),
                    "process.passport.displayName must not be null");

            String displayNameValue = displayName.value();
            String displayNameLower = displayNameValue.toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(displayNameLower)) {
                throw new IllegalArgumentException(
                        "Duplicate collection process display name detected (displayName=" + displayNameValue + ")"
                );
            }

            passportsMap.put(code, passport);
        }

        this.processesByCode = Collections.unmodifiableMap(processesMap);
        this.passportsByCode = Collections.unmodifiableMap(passportsMap);
    }

    @Override
    public Map<MarketDataCollectionProcessCode, MarketDataCollectionProcess> processesByCode() {
        return processesByCode;
    }

    @Override
    public Map<MarketDataCollectionProcessCode, CollectionProcessPassport> passportsByCode() {
        return passportsByCode;
    }

    private static void validateSupportedInstrumentCodes(Set<InstrumentCode> supportedInstrumentCodes) {
        Objects.requireNonNull(supportedInstrumentCodes, "process.supportedInstrumentCodes must not be null");

        if (supportedInstrumentCodes.isEmpty()) {
            throw new IllegalArgumentException("process.supportedInstrumentCodes must not be empty");
        }

        for (InstrumentCode instrumentCode : supportedInstrumentCodes) {
            Objects.requireNonNull(instrumentCode, "process.supportedInstrumentCodes must not contain null");
        }
    }
}
