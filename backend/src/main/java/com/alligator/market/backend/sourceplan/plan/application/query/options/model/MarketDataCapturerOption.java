package com.alligator.market.backend.sourceplan.plan.application.query.options.model;

import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerDisplayName;

import java.util.Objects;

/**
 * Option процесса захвата рыночных данных для UI.
 */
public record MarketDataCapturerOption(
        MarketDataCapturerCode code,
        MarketDataCapturerDisplayName displayName
) {

    public MarketDataCapturerOption {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
