package com.alligator.market.domain.sourceplan.vo;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

public record SourcePlanKey(
        CapturerCode capturerCode,
        InstrumentCode instrumentCode
) {
    public SourcePlanKey {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
    }
}
