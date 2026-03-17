package com.alligator.market.domain.sourcing.pool;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.source.InstrumentMarketDataSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * План набора источников рыночных данных для конкретного инструмента.
 *
 * <p>Примечание: Порядок элементов в {@code sources} определяет приоритет источников.</p>
 */
@SuppressWarnings("ClassCanBeRecord")
public final class InstrumentSourcesPool {

    /* Код инструмента. */
    private final InstrumentCode instrumentCode;

    /* Список источников рыночных данных. */
    private final List<InstrumentMarketDataSource> sources;

    public InstrumentSourcesPool(
            InstrumentCode instrumentCode,
            List<InstrumentMarketDataSource> sources
    ) {
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sources, "sources must not be null");

        if (sources.isEmpty()) {
            throw new IllegalArgumentException("sources must not be empty");
        }

        // Безопасная копия источников
        List<InstrumentMarketDataSource> sourceCopy = new ArrayList<>(sources.size());

        // Набор кодов провайдеров для проверки дубликатов
        Set<ProviderCode> providerCodes = new HashSet<>();

        for (InstrumentMarketDataSource source : sources) {
            InstrumentMarketDataSource checkedSource = Objects.requireNonNull(source, "source must not be null");

            if (!providerCodes.add(checkedSource.providerCode())) {
                throw new IllegalArgumentException(
                        "Instrument source configuration contains duplicate provider code '" +
                                checkedSource.providerCode().value() + "'"
                );
            }

            sourceCopy.add(checkedSource);
        }

        this.sources = List.copyOf(sourceCopy);
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public List<InstrumentMarketDataSource> sources() {
        return sources;
    }
}
