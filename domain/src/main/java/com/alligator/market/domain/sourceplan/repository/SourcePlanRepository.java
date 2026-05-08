package com.alligator.market.domain.sourceplan.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for {@link SourcePlan} aggregates.
 */
public interface SourcePlanRepository {

    /**
     * Finds a source plan by {@code capturerCode} and {@code instrumentCode}.
     */
    Optional<SourcePlan> findByMarketDataCapturerCodeAndInstrumentCode(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    );

    /**
     * Finds a source plan by {@code capturerCode} and {@code instrumentCode},
     * including only sources that are currently available for capture.
     */
    Optional<SourcePlan> findActiveByMarketDataCapturerCodeAndInstrumentCode(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    );

    /**
     * Finds all source plans.
     */
    List<SourcePlan> findAll();

    /**
     * Creates the source plan when it does not already exist.
     *
     * @return true when the plan was created; false when it already existed
     */
    boolean createIfAbsent(SourcePlan plan);


    /**
     * Replaces the contents of an existing source plan.
     *
     * @return true when the plan existed and was updated; false when no matching plan exists
     */
    boolean replaceIfExists(SourcePlan plan);

    /**
     * Deletes a source plan by {@code capturerCode} and {@code instrumentCode}.
     *
     * @return true when the plan existed and was deleted; false when no matching plan exists
     */
    boolean deleteIfExistsByMarketDataCapturerCodeAndInstrumentCode(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    );
}
