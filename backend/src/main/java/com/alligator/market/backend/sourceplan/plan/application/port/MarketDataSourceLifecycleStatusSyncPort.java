package com.alligator.market.backend.sourceplan.plan.application.port;

public interface MarketDataSourceLifecycleStatusSyncPort {
    void retireSourcesWithoutActiveSourcePassports();

    void retireSourcesWithoutActiveMarketDataCapturerPassports();
}
