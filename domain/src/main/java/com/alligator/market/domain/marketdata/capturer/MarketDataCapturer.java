package com.alligator.market.domain.marketdata.capturer;

import com.alligator.market.domain.marketdata.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.marketdata.capturer.policy.MarketDataCapturerPolicy;
import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerCode;

/**
 * Application component that captures market data for a specific workflow.
 */
public interface MarketDataCapturer {

    /**
     * Unique capturer code.
     */
    MarketDataCapturerCode capturerCode();

    /**
     * Stable capturer metadata.
     */
    MarketDataCapturerPassport passport();

    /**
     * Capturer-specific policy.
     */
    MarketDataCapturerPolicy policy();
}
