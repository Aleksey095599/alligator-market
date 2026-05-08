package com.alligator.market.domain.marketdata.tick.level.captured.repository;

import com.alligator.market.domain.marketdata.tick.level.captured.CapturedMarketDataTick;

/**
 * Доменный порт сохранения captured-level рыночных тиков.
 */
public interface CapturedMarketDataTickRepository {

    void save(CapturedMarketDataTick tick);
}
