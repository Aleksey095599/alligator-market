package com.alligator.market.domain.sourceplan.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlan;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for {@link MarketDataSourcePlan} aggregates.
 */
public interface MarketDataSourcePlanRepository {

    /**
     * Finds a source plan by {@code captureProcessCode} and {@code instrumentCode}.
     */
    Optional<MarketDataSourcePlan> findByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    );

    /**
     * Finds a source plan by {@code captureProcessCode} and {@code instrumentCode},
     * including only sources that are currently available for capture.
     */
    Optional<MarketDataSourcePlan> findActiveByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    );

    /**
     * Finds all source plans.
     */
    List<MarketDataSourcePlan> findAll();

    /**
     * Creates the source plan when it does not already exist.
     *
     * @return true when the plan was created; false when it already existed
     */
    boolean createIfAbsent(MarketDataSourcePlan plan);


    /**
     * Replaces the contents of an existing source plan.
     *
     * @return true when the plan existed and was updated; false when no matching plan exists
     */
    boolean replaceIfExists(MarketDataSourcePlan plan);

    /**
     * Deletes a source plan by {@code captureProcessCode} and {@code instrumentCode}.
     *
     * @return true when the plan existed and was deleted; false when no matching plan exists
     */
    boolean deleteIfExistsByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    );
}
