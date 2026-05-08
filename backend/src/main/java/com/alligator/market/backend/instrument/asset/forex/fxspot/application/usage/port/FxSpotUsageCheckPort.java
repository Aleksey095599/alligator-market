package com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

public interface FxSpotUsageCheckPort {
    boolean isUsed(InstrumentCode instrumentCode);
}
