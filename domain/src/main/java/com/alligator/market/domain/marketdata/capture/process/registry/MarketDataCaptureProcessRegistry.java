package com.alligator.market.domain.marketdata.capture.process.registry;

import com.alligator.market.domain.marketdata.capture.process.MarketDataCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.passport.MarketDataCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Реестр процессов захвата рыночных данных, зарегистрированных в приложении.
 *
 * <p>Назначение: единый источник истины о доступных {@link MarketDataCaptureProcess}
 * и точка валидации их консистентности.</p>
 *
 * <p>Типичный жизненный цикл: реестр формируется при старте приложения и далее используется
 * как неизменяемый snapshot.</p>
 */
public interface MarketDataCaptureProcessRegistry {

    /**
     * Неизменяемая карта "код процесса захвата → процесс захвата".
     */
    Map<MarketDataCaptureProcessCode, MarketDataCaptureProcess> processesByCode();

    /**
     * Производная проекция: неизменяемая карта "код процесса захвата → паспорт процесса захвата".
     */
    default Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> passportsByCode() {
        Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> map = new LinkedHashMap<>();

        for (Map.Entry<MarketDataCaptureProcessCode, MarketDataCaptureProcess> entry : processesByCode().entrySet()) {
            MarketDataCaptureProcessCode code = entry.getKey();
            MarketDataCaptureProcess process = entry.getValue();

            MarketDataCaptureProcessPassport passport = Objects.requireNonNull(process.passport(),
                    "process.passport must not be null");

            map.put(code, passport);
        }

        return Collections.unmodifiableMap(map);
    }
}
