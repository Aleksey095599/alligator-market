package com.alligator.market.domain.process.twap.repository;

import com.alligator.market.domain.marketdata.tick.level.captured.CapturedMarketDataTick;

/**
 * Domain port for storing FX Spot ticks captured for the TWAP process.
 */
public interface FxSpotTwapCapturedTicksRepository {

    void save(CapturedMarketDataTick tick);
}
