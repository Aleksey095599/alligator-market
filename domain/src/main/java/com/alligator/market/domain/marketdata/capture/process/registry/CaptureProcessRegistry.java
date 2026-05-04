package com.alligator.market.domain.marketdata.capture.process.registry;

import com.alligator.market.domain.marketdata.capture.process.MarketDataCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Реестр процессов фиксации рыночных данных, зарегистрированных в приложении.
 *
 * <p>Назначение: единый источник истины о доступных {@link MarketDataCaptureProcess}
 * и точка валидации их консистентности.</p>
 *
 * <p>Типичный жизненный цикл: реестр формируется при старте приложения и далее используется
 * как неизменяемый snapshot.</p>
 */
public interface CaptureProcessRegistry {

    /**
     * Неизменяемая карта "код процесса фиксации → процесс фиксации".
     */
    Map<CaptureProcessCode, MarketDataCaptureProcess> processesByCode();

    /**
     * Производная проекция: неизменяемая карта "код процесса фиксации → паспорт процесса фиксации".
     */
    default Map<CaptureProcessCode, CaptureProcessPassport> passportsByCode() {
        Map<CaptureProcessCode, CaptureProcessPassport> map = new LinkedHashMap<>();

        for (Map.Entry<CaptureProcessCode, MarketDataCaptureProcess> entry : processesByCode().entrySet()) {
            CaptureProcessCode code = entry.getKey();
            MarketDataCaptureProcess process = entry.getValue();

            CaptureProcessPassport passport = Objects.requireNonNull(process.passport(),
                    "process.passport must not be null");

            map.put(code, passport);
        }

        return Collections.unmodifiableMap(map);
    }
}
