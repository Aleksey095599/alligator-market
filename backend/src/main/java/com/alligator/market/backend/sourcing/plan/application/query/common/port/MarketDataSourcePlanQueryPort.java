package com.alligator.market.backend.sourcing.plan.application.query.common.port;

import com.alligator.market.backend.sourcing.plan.application.query.common.model.MarketDataSourcePlanQueryItem;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;

import java.util.List;
import java.util.Optional;

public interface MarketDataSourcePlanQueryPort {

    Optional<MarketDataSourcePlanQueryItem> findByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    );

    List<MarketDataSourcePlanQueryItem> findAll();
}
