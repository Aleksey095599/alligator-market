package com.alligator.market.domain.marketdata.capture.process.registry;

import com.alligator.market.domain.marketdata.capture.process.MDCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.MDCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Реестр процессов захвата рыночных данных, зарегистрированных в приложении.
 *
 * <p>Назначение: единый источник истины о доступных {@link MDCaptureProcess}
 * и точка валидации их консистентности.</p>
 *
 * <p>Типичный жизненный цикл: реестр формируется при старте приложения и далее используется
 * как неизменяемый snapshot.</p>
 */
public interface MDCaptureProcessRegistry {

    /**
     * Неизменяемая карта "код процесса захвата → процесс захвата".
     */
    Map<MDCaptureProcessCode, MDCaptureProcess> processesByCode();

    /**
     * Производная проекция: неизменяемая карта "код процесса захвата → паспорт процесса захвата".
     */
    default Map<MDCaptureProcessCode, MDCaptureProcessPassport> passportsByCode() {
        Map<MDCaptureProcessCode, MDCaptureProcessPassport> map = new LinkedHashMap<>();

        for (Map.Entry<MDCaptureProcessCode, MDCaptureProcess> entry : processesByCode().entrySet()) {
            MDCaptureProcessCode code = entry.getKey();
            MDCaptureProcess process = entry.getValue();

            MDCaptureProcessPassport passport = Objects.requireNonNull(process.passport(),
                    "process.passport must not be null");

            map.put(code, passport);
        }

        return Collections.unmodifiableMap(map);
    }
}
