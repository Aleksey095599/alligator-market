package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SnapshotRuntimeSourceRegistry implements RuntimeSourceRegistry {

    private final Map<SourceCode, MarketDataSource> sourcesByCode;

    public SnapshotRuntimeSourceRegistry(List<? extends MarketDataSource> sources) {
        Objects.requireNonNull(sources, "sources must not be null");

        if (sources.isEmpty()) {
            throw new IllegalArgumentException("Market data source registry must contain at least one source");
        }

        Map<SourceCode, MarketDataSource> sourcesMap = new LinkedHashMap<>();

        for (MarketDataSource source : sources) {
            Objects.requireNonNull(source, "source must not be null");

            SourceCode code = Objects.requireNonNull(source.code(),
                    "source.sourceCode must not be null");

            Objects.requireNonNull(source.passport(),
                    "source.passport must not be null");

            MarketDataSource prev = sourcesMap.put(code, source);
            if (prev != null) {
                throw new IllegalArgumentException(
                        "Duplicate market data source code detected (sourceCode=" + code.value() + ")"
                );
            }
        }

        this.sourcesByCode = Collections.unmodifiableMap(sourcesMap);
    }

    @Override
    public Map<SourceCode, MarketDataSource> sourcesByCode() {
        return sourcesByCode;
    }
}
