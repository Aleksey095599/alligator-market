package com.alligator.market.domain.sourcing.plan;

import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

import java.util.*;

/**
 * План источников рыночных данных для конкретного инструмента (aggregate-root).
 * Содержит упорядоченный по приоритету набор источников рыночных данных.
 */
@SuppressWarnings("ClassCanBeRecord")
public final class InstrumentSourcePlan {

    /* Код инструмента. */
    private final InstrumentCode instrumentCode;

    /* Список источников рыночных данных, отсортированный по приоритету. */
    private final List<MarketDataSource> sources;

    public InstrumentSourcePlan(
            InstrumentCode instrumentCode,
            List<MarketDataSource> sources
    ) {
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sources, "sources must not be null");

        if (sources.isEmpty()) {
            throw new IllegalArgumentException("sources must not be empty");
        }

        // Безопасная копия источников
        List<MarketDataSource> sourceCopy = new ArrayList<>(sources.size());

        // Набор кодов провайдеров для проверки дубликатов
        Set<ProviderCode> providerCodes = new HashSet<>();

        // Набор приоритетов для проверки дубликатов
        Set<Integer> priorities = new HashSet<>();

        for (MarketDataSource source : sources) {
            MarketDataSource sourceToCheck = Objects.requireNonNull(source,
                    "source must not be null");

            if (!providerCodes.add(sourceToCheck.providerCode())) {
                throw new IllegalArgumentException(
                        "Instrument source plan contains duplicate provider code '" +
                                sourceToCheck.providerCode().value() + "'"
                );
            }

            if (!priorities.add(sourceToCheck.priority())) {
                throw new IllegalArgumentException(
                        "Instrument source plan contains duplicate priority '" +
                                sourceToCheck.priority() + "'"
                );
            }

            sourceCopy.add(sourceToCheck);
        }

        // Сортируем порядок источников по приоритету (чем выше приоритет источника, тем ниже значение priority)
        sourceCopy.sort(Comparator.comparingInt(MarketDataSource::priority));

        this.sources = List.copyOf(sourceCopy);
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public List<MarketDataSource> sources() {
        return sources;
    }
}
