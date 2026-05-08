package com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

public interface FxSpotUsageContributor {
    boolean isUsed(InstrumentCode instrumentCode);
}
