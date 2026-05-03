package com.alligator.market.domain.sourcing.plan;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.capture.vo.MarketDataCollectionProcessCode;
import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

import java.util.*;

/**
 * План источников рыночных данных для конкретного инструмента.
 *
 * <p>Назначение: Содержит упорядоченный по приоритету набор источников рыночных данных.</p>
 */
@SuppressWarnings("ClassCanBeRecord")
public final class MarketDataSourcePlan {

    /* Код процесса сбора рыночных данных. */
    private final MarketDataCollectionProcessCode collectionProcessCode;

    /* Код инструмента. */
    private final InstrumentCode instrumentCode;

    /* Список источников рыночных данных, отсортированный по приоритету. */
    private final List<MarketDataSource> sources;

    public MarketDataSourcePlan(
            MarketDataCollectionProcessCode collectionProcessCode,
            InstrumentCode instrumentCode,
            List<MarketDataSource> sources
    ) {
        this.collectionProcessCode = Objects.requireNonNull(
                collectionProcessCode,
                "collectionProcessCode must not be null"
        );
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sources, "sources must not be null");

        this.sources = copyAndValidateSources(sources);
    }

    public MarketDataCollectionProcessCode collectionProcessCode() {
        return collectionProcessCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public List<MarketDataSource> sources() {
        return sources;
    }

    /**
     * Создает копию списка источников и выполняет валидацию.
     */
    private static List<MarketDataSource> copyAndValidateSources(List<MarketDataSource> sources) {
        if (sources.isEmpty()) {
            throw new IllegalArgumentException("sources must not be empty");
        }

        List<MarketDataSource> sourceCopy = new ArrayList<>(sources.size());
        Set<ProviderCode> providerCodes = new HashSet<>();
        Set<Integer> priorities = new HashSet<>();

        for (MarketDataSource source : sources) {
            MarketDataSource sourceToCheck = Objects.requireNonNull(source, "source must not be null");

            if (!providerCodes.add(sourceToCheck.providerCode())) {
                throw new IllegalArgumentException(
                        "Market data source plan contains duplicate provider code '" +
                                sourceToCheck.providerCode().value() + "'"
                );
            }

            if (!priorities.add(sourceToCheck.priority())) {
                throw new IllegalArgumentException(
                        "Market data source plan contains duplicate priority '" +
                                sourceToCheck.priority() + "'"
                );
            }

            sourceCopy.add(sourceToCheck);
        }

        // Сортируем порядок источников по приоритету (чем выше приоритет источника, тем ниже значение priority)
        sourceCopy.sort(Comparator.comparingInt(MarketDataSource::priority));
        return List.copyOf(sourceCopy);
    }
}
