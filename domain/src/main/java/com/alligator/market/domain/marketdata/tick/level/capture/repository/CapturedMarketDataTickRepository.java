package com.alligator.market.domain.marketdata.tick.level.capture.repository;

import com.alligator.market.domain.marketdata.tick.level.capture.CapturedMarketDataTick;

/**
 * Доменный порт сохранения captured-level рыночных тиков.
 */
public interface CapturedMarketDataTickRepository {

    void save(CapturedMarketDataTick tick);
}
