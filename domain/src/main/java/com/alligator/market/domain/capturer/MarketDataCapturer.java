package com.alligator.market.domain.capturer;

import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.policy.MarketDataCapturerPolicy;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

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
