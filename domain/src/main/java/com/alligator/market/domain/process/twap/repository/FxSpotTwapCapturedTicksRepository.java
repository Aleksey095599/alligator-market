package com.alligator.market.domain.process.twap.repository;

import com.alligator.market.domain.marketdata.tick.level.captured.CapturedMarketDataTick;

public interface FxSpotTwapCapturedTicksRepository {

    void save(CapturedMarketDataTick tick);
}
