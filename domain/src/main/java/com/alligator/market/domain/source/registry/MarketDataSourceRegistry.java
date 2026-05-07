package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Registry of runtime market data sources available in the application.
 */
public interface MarketDataSourceRegistry {

    /**
     * Immutable map "source code -> source".
     */
    Map<MarketDataSourceCode, MarketDataSource> sourcesByCode();

    /**
     * Derived immutable map "source code -> source passport".
     */
    default Map<MarketDataSourceCode, MarketDataSourcePassport> passportsByCode() {
        Map<MarketDataSourceCode, MarketDataSourcePassport> map = new LinkedHashMap<>();

        for (Map.Entry<MarketDataSourceCode, MarketDataSource> entry : sourcesByCode().entrySet()) {
            MarketDataSourceCode code = entry.getKey();
            MarketDataSource source = entry.getValue();

            MarketDataSourcePassport passport = Objects.requireNonNull(source.passport(),
                    "source.passport must not be null");

            map.put(code, passport);
        }

        return Collections.unmodifiableMap(map);
    }
}
