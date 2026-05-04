package com.alligator.market.domain.marketdata.collection.process.policy;

import java.time.Duration;

/**
 * Общий контракт политики процесса сбора рыночных данных.
 */
public interface CollectionProcessPolicy {

    /**
     * Минимально допустимый интервал между фиксациями тиков этим процессом.
     */
    Duration minCaptureInterval();
}
