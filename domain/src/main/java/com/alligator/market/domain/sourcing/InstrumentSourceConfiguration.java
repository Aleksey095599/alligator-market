package com.alligator.market.domain.sourcing;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.*;

/**
 * Конфигурация источников рыночных данных для конкретного инструмента.
 *
 * <p>Примечание: Порядок элементов в {@code sources} = приоритет источников.</p>
 */
public class InstrumentSourceConfiguration {

    /* Код инструмента. */
    private final InstrumentCode instrumentCode;

    /* Список источников рыночных данных. */
    private final List<InstrumentMarketDataSource> sources;

    public InstrumentSourceConfiguration(
            InstrumentCode instrumentCode,
            List<InstrumentMarketDataSource> sources
    ) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
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
                throw new IllegalStateException(
                        "Instrument sourcing contains duplicate provider code '" +
                                checkedSource.providerCode().value() + "'"
                );
            }

            sourceCopy.add(checkedSource);
        }

        this.instrumentCode = instrumentCode;
        this.sources = List.copyOf(sourceCopy);
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public List<InstrumentMarketDataSource> sources() {
        return sources;
    }
}
