package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public interface MarketDataSourceRegistry {

    Map<MarketDataSourceCode, MarketDataSource> sourcesByCode();

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
