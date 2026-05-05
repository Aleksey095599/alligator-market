package com.alligator.market.domain.marketdata.capture.process.registry;

import com.alligator.market.domain.marketdata.capture.process.MarketDataCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.MarketDataCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessDisplayName;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация {@link MarketDataCaptureProcessRegistry} в виде неизменяемого snapshot набора процессов захвата рыночных данных.
 *
 * <p>Инварианты, заданные в {@link MarketDataCaptureProcessRegistry}, валидируются в конструкторе
 * и далее гарантируются на протяжении жизни экземпляра.</p>
 */
public final class SnapshotMarketDataCaptureProcessRegistry implements MarketDataCaptureProcessRegistry {

    private final Map<MarketDataCaptureProcessCode, MarketDataCaptureProcess> processesByCode;
    private final Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> passportsByCode;

    public SnapshotMarketDataCaptureProcessRegistry(List<? extends MarketDataCaptureProcess> processes) {
        Objects.requireNonNull(processes, "processes must not be null");

        if (processes.isEmpty()) {
            throw new IllegalArgumentException("Capture process registry must contain at least one process");
        }

        Map<MarketDataCaptureProcessCode, MarketDataCaptureProcess> processesMap = new LinkedHashMap<>();
        Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> passportsMap = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketDataCaptureProcess process : processes) {
            Objects.requireNonNull(process, "process must not be null");

            MarketDataCaptureProcessCode code = Objects.requireNonNull(process.processCode(),
                    "process.processCode must not be null");

            MarketDataCaptureProcessPassport passport = Objects.requireNonNull(process.passport(),
                    "process.passport must not be null");

            Objects.requireNonNull(process.policy(),
                    "process.policy must not be null");

            MarketDataCaptureProcess previous = processesMap.put(code, process);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate capture process code detected (code=" + code.value() + ")"
                );
            }

            MarketDataCaptureProcessDisplayName displayName = Objects.requireNonNull(passport.displayName(),
                    "process.passport.displayName must not be null");

            String displayNameValue = displayName.value();
            String displayNameLower = displayNameValue.toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(displayNameLower)) {
                throw new IllegalArgumentException(
                        "Duplicate capture process display name detected (displayName=" + displayNameValue + ")"
                );
            }

            passportsMap.put(code, passport);
        }

        this.processesByCode = Collections.unmodifiableMap(processesMap);
        this.passportsByCode = Collections.unmodifiableMap(passportsMap);
    }

    @Override
    public Map<MarketDataCaptureProcessCode, MarketDataCaptureProcess> processesByCode() {
        return processesByCode;
    }

    @Override
    public Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> passportsByCode() {
        return passportsByCode;
    }
}
