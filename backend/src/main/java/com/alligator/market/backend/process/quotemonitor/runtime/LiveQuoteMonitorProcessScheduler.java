package com.alligator.market.backend.process.quotemonitor.runtime;

import java.time.Duration;

public interface LiveQuoteMonitorProcessScheduler {

    LiveQuoteMonitorScheduledTask scheduleAtFixedRate(Duration interval, Runnable task);
}
