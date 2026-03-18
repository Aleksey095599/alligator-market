package com.alligator.market.domain.sourcing.plan;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.source.InstrumentMarketDataSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * План источников рыночных данных для конкретного инструмента.
 *
 * <p>Приоритет источников определяется полем {@code priority}.
 * Конструктор нормализует список {@code sources} по возрастанию приоритета.</p>
 */
@SuppressWarnings("ClassCanBeRecord")
public final class InstrumentSourcePlan {

    /* Код инструмента. */
    private final InstrumentCode instrumentCode;

    /* Список источников рыночных данных, отсортированный по приоритету. */
    private final List<InstrumentMarketDataSource> sources;

    public InstrumentSourcePlan(
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

        // Набор приоритетов для проверки дубликатов
        Set<Integer> priorities = new HashSet<>();

        for (InstrumentMarketDataSource source : sources) {
            InstrumentMarketDataSource checkedSource = Objects.requireNonNull(source, "source must not be null");

            if (!providerCodes.add(checkedSource.providerCode())) {
                throw new IllegalArgumentException(
                        "Instrument source plan contains duplicate provider code '" +
                                checkedSource.providerCode().value() + "'"
                );
            }

            if (!priorities.add(checkedSource.priority())) {
                throw new IllegalArgumentException(
                        "Instrument source plan contains duplicate priority '" +
                                checkedSource.priority() + "'"
                );
            }

            sourceCopy.add(checkedSource);
        }

        // Нормализуем порядок источников по приоритету
        sourceCopy.sort(Comparator.comparingInt(InstrumentMarketDataSource::priority));

        this.sources = List.copyOf(sourceCopy);
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public List<InstrumentMarketDataSource> sources() {
        return sources;
    }
}
