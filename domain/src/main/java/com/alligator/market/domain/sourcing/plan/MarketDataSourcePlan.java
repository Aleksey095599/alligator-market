package com.alligator.market.domain.sourcing.plan;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.domain.sourcing.source.MarketDataSource;

import java.util.*;

/**
 * План источников рыночных данных для конкретного процесса сбора рыночных данных и конкретного инструмента.
 *
 * <p>Назначение: Позволяет по коду процесса и коду инструмента получить список источников
 * рыночных данных {@link MarketDataSource}.</p>
 */
@SuppressWarnings("ClassCanBeRecord")
public final class MarketDataSourcePlan {

    private final CaptureProcessCode collectionProcessCode;
    private final InstrumentCode instrumentCode;
    private final List<MarketDataSource> sources;

    /**
     * Конструктор для создания плана источников рыночных данных.
     *
     * @param collectionProcessCode Код процесса сбора рыночных данных
     * @param instrumentCode        Код инструмента
     * @param sources               Список источников рыночных данных
     */
    public MarketDataSourcePlan(
            CaptureProcessCode collectionProcessCode,
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

    public CaptureProcessCode collectionProcessCode() {
        return collectionProcessCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public List<MarketDataSource> sources() {
        return sources;
    }

    /* Создает копию списка источников и выполняет валидацию. */
    private static List<MarketDataSource> copyAndValidateSources(List<MarketDataSource> sources) {
        if (sources.isEmpty()) {
            throw new IllegalArgumentException("sources must not be empty");
        }

        List<MarketDataSource> sourcesValidated = new ArrayList<>(sources.size());
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

            sourcesValidated.add(sourceToCheck);
        }

        return List.copyOf(sourcesValidated);
    }
}
