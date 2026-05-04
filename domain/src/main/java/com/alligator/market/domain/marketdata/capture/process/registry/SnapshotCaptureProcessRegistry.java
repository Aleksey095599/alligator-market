package com.alligator.market.domain.marketdata.capture.process.registry;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.MarketDataCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessDisplayName;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация {@link CaptureProcessRegistry} в виде неизменяемого snapshot набора процессов фиксации.
 *
 * <p>Инварианты, заданные в {@link CaptureProcessRegistry}, валидируются в конструкторе
 * и далее гарантируются на протяжении жизни экземпляра.</p>
 */
public final class SnapshotCaptureProcessRegistry implements CaptureProcessRegistry {

    private final Map<CaptureProcessCode, MarketDataCaptureProcess> processesByCode;
    private final Map<CaptureProcessCode, CaptureProcessPassport> passportsByCode;

    public SnapshotCaptureProcessRegistry(List<? extends MarketDataCaptureProcess> processes) {
        Objects.requireNonNull(processes, "processes must not be null");

        if (processes.isEmpty()) {
            throw new IllegalArgumentException("Capture process registry must contain at least one process");
        }

        Map<CaptureProcessCode, MarketDataCaptureProcess> processesMap = new LinkedHashMap<>();
        Map<CaptureProcessCode, CaptureProcessPassport> passportsMap = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketDataCaptureProcess process : processes) {
            Objects.requireNonNull(process, "process must not be null");

            CaptureProcessCode code = Objects.requireNonNull(process.processCode(),
                    "process.processCode must not be null");

            CaptureProcessPassport passport = Objects.requireNonNull(process.passport(),
                    "process.passport must not be null");

            Objects.requireNonNull(process.policy(),
                    "process.policy must not be null");

            validateSupportedInstrumentCodes(process.supportedInstrumentCodes());

            MarketDataCaptureProcess previous = processesMap.put(code, process);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate capture process code detected (code=" + code.value() + ")"
                );
            }

            CaptureProcessDisplayName displayName = Objects.requireNonNull(passport.displayName(),
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
    public Map<CaptureProcessCode, MarketDataCaptureProcess> processesByCode() {
        return processesByCode;
    }

    @Override
    public Map<CaptureProcessCode, CaptureProcessPassport> passportsByCode() {
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
