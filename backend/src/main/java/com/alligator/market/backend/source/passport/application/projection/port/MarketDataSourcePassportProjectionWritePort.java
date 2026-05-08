package com.alligator.market.backend.source.passport.application.projection.port;

import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;

import java.util.Map;
import java.util.Set;

/**
 * Write port for synchronizing market data source passport projection rows.
 *
 * <p>Keeps the materialized source passport view consistent with {@link MarketDataSourceRegistry}.</p>
 */
public interface MarketDataSourcePassportProjectionWritePort {

    /**
     * Retires all projection rows except the current source codes.
     *
     * @param currentCodes source codes from the current registry
     */
    void retireAllExcept(Set<MarketDataSourceCode> currentCodes);

    /**
     * Inserts or updates source passports in the projection.
     *
     * <p>For every {@link MarketDataSourceCode} in {@code passports}, the projection must contain exactly one
     * current row with values from the input passport.</p>
     *
     * @param passports map of source code to source passport
     */
    void upsertAll(Map<MarketDataSourceCode, MarketDataSourcePassport> passports);
}
