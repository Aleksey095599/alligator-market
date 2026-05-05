package com.alligator.market.backend.sourcing.plan.application.port;

/**
 * Port for synchronizing technical lifecycle status of source plan rows.
 */
public interface MarketDataSourceLifecycleStatusSyncPort {

    /**
     * Retire source rows whose provider passport is not active anymore.
     */
    void retireSourcesWithoutActiveProviderPassports();

    /**
     * Retire source rows whose capture process passport is not active anymore.
     */
    void retireSourcesWithoutActiveCaptureProcessPassports();
}
