package com.alligator.market.domain.marketdata.capture.process.registry;

import com.alligator.market.domain.marketdata.capture.process.MDCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.MDCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessDisplayName;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация {@link MDCaptureProcessRegistry} в виде неизменяемого snapshot набора процессов захвата рыночных данных.
 *
 * <p>Инварианты, заданные в {@link MDCaptureProcessRegistry}, валидируются в конструкторе
 * и далее гарантируются на протяжении жизни экземпляра.</p>
 */
public final class SnapshotMDCaptureProcessRegistry implements MDCaptureProcessRegistry {

    private final Map<MDCaptureProcessCode, MDCaptureProcess> processesByCode;
    private final Map<MDCaptureProcessCode, MDCaptureProcessPassport> passportsByCode;

    public SnapshotMDCaptureProcessRegistry(List<? extends MDCaptureProcess> processes) {
        Objects.requireNonNull(processes, "processes must not be null");

        if (processes.isEmpty()) {
            throw new IllegalArgumentException("Capture process registry must contain at least one process");
        }

        Map<MDCaptureProcessCode, MDCaptureProcess> processesMap = new LinkedHashMap<>();
        Map<MDCaptureProcessCode, MDCaptureProcessPassport> passportsMap = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MDCaptureProcess process : processes) {
            Objects.requireNonNull(process, "process must not be null");

            MDCaptureProcessCode code = Objects.requireNonNull(process.processCode(),
                    "process.processCode must not be null");

            MDCaptureProcessPassport passport = Objects.requireNonNull(process.passport(),
                    "process.passport must not be null");

            Objects.requireNonNull(process.policy(),
                    "process.policy must not be null");

            MDCaptureProcess previous = processesMap.put(code, process);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate capture process code detected (code=" + code.value() + ")"
                );
            }

            MDCaptureProcessDisplayName displayName = Objects.requireNonNull(passport.displayName(),
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
    public Map<MDCaptureProcessCode, MDCaptureProcess> processesByCode() {
        return processesByCode;
    }

    @Override
    public Map<MDCaptureProcessCode, MDCaptureProcessPassport> passportsByCode() {
        return passportsByCode;
    }
}
