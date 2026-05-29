package com.alligator.market.domain.marketdata.feed.resolver;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.feed.CapturerFeed;

import java.util.Optional;

public interface CapturerFeedResolver {

    Optional<CapturerFeed> resolve(CapturerCode capturerCode, InstrumentCode instrumentCode);
}
