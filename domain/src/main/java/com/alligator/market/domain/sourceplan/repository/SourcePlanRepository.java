package com.alligator.market.domain.sourceplan.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;

import java.util.List;
import java.util.Optional;

public interface SourcePlanRepository {

    Optional<SourcePlan> findByMarketDataCapturerCodeAndInstrumentCode(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    );

    /**
     * Includes only sources that are currently available for capture.
     */
    Optional<SourcePlan> findActiveByMarketDataCapturerCodeAndInstrumentCode(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    );

    List<SourcePlan> findAll();

    /**
     * Returns true when created, false when the plan already exists.
     */
    boolean createIfAbsent(SourcePlan plan);

    /**
     * Returns true when updated, false when no matching plan exists.
     */
    boolean replaceIfExists(SourcePlan plan);

    /**
     * Returns true when deleted, false when no matching plan exists.
     */
    boolean deleteIfExistsByMarketDataCapturerCodeAndInstrumentCode(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    );
}
