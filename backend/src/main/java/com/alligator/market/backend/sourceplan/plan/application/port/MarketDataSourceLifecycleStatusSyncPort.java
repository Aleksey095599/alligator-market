package com.alligator.market.backend.sourceplan.plan.application.port;

/**
 * Port for synchronizing technical lifecycle status of source plan rows.
 */
public interface MarketDataSourceLifecycleStatusSyncPort {

    /**
     * Retire source plan rows whose source passport is not active anymore.
     */
    void retireSourcesWithoutActiveSourcePassports();

    /**
     * Retire source rows whose capture process passport is not active anymore.
     */
    void retireSourcesWithoutActiveCaptureProcessPassports();
}
