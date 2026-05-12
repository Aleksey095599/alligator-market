package com.alligator.market.domain.sourceplan.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.sourceplan.SourcePlan;

import java.util.List;
import java.util.Optional;

public interface SourcePlanRepository {

    Optional<SourcePlan> findByMarketDataCapturerCodeAndInstrumentCode(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    );

    Optional<SourcePlan> findExecutableByMarketDataCapturerCodeAndInstrumentCode(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    );

    List<SourcePlan> findExecutableByMarketDataCapturerCode(
            CapturerCode capturerCode
    );

    List<SourcePlan> findAll();

    boolean createIfAbsent(SourcePlan plan);

    boolean replaceIfExists(SourcePlan plan);

    boolean deleteIfExistsByMarketDataCapturerCodeAndInstrumentCode(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode
    );
}
