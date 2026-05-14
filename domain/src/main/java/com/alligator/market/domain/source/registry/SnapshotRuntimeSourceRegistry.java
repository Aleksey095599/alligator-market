package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SnapshotRuntimeSourceRegistry implements RuntimeSourceRegistry {

    private final Map<SourceCode, MarketSource> sourcesByCode;

    public SnapshotRuntimeSourceRegistry(List<? extends MarketSource> sources) {
        Objects.requireNonNull(sources, "sources must not be null");

        if (sources.isEmpty()) {
            throw new IllegalArgumentException("Market source registry must contain at least one source");
        }

        Map<SourceCode, MarketSource> sourcesMap = new LinkedHashMap<>();

        for (MarketSource source : sources) {
            Objects.requireNonNull(source, "source must not be null");

            SourceCode code = Objects.requireNonNull(source.code(),
                    "source.sourceCode must not be null");

            Objects.requireNonNull(source.passport(),
                    "source.passport must not be null");

            Objects.requireNonNull(source.policy(),
                    "source.policy must not be null");

            MarketSource prev = sourcesMap.put(code, source);
            if (prev != null) {
                throw new IllegalArgumentException(
                        "Duplicate market source code detected (sourceCode=" + code.value() + ")"
                );
            }
        }

        this.sourcesByCode = Collections.unmodifiableMap(sourcesMap);
    }

    @Override
    public Map<SourceCode, MarketSource> sourcesByCode() {
        return sourcesByCode;
    }
}
