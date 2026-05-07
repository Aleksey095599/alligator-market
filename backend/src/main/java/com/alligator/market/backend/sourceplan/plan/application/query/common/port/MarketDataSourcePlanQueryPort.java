package com.alligator.market.backend.sourceplan.plan.application.query.common.port;

import com.alligator.market.backend.sourceplan.plan.application.query.common.model.MarketDataSourcePlanQueryItem;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;

import java.util.List;
import java.util.Optional;

/**
 * Read-порт административного отображения source plan.
 *
 * <p>В отличие от runtime-выбора источников, порт раскрывает retired строки для management UX.</p>
 */
public interface MarketDataSourcePlanQueryPort {

    Optional<MarketDataSourcePlanQueryItem> findByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    );

    List<MarketDataSourcePlanQueryItem> findAll();
}
