package com.alligator.market.backend.sourcing.plan.repository.adapter;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import com.alligator.market.domain.sourcing.source.InstrumentMarketDataSource;
import org.jooq.DSLContext;

import java.util.*;

import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentMarketDataSource.INSTRUMENT_MARKET_DATA_SOURCE;

/**
 * jOOQ-адаптер репозитория планов источников.
 */
public final class JooqInstrumentSourcePlanRepository implements InstrumentSourcePlanRepository {

    /* DSLContext для выполнения SQL через jOOQ. */
    private final DSLContext dsl;

    public JooqInstrumentSourcePlanRepository(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Optional<InstrumentSourcePlan> findByInstrumentCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        List<InstrumentMarketDataSource> sources = dsl
                .select(
                        INSTRUMENT_MARKET_DATA_SOURCE.PROVIDER_CODE,
                        INSTRUMENT_MARKET_DATA_SOURCE.ACTIVE,
                        INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY
                )
                .from(INSTRUMENT_MARKET_DATA_SOURCE)
                .where(INSTRUMENT_MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .orderBy(INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY.asc())
                .fetch(record -> toSource(
                        record.get(INSTRUMENT_MARKET_DATA_SOURCE.PROVIDER_CODE),
                        record.get(INSTRUMENT_MARKET_DATA_SOURCE.ACTIVE),
                        record.get(INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY)
                ));

        if (sources.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new InstrumentSourcePlan(instrumentCode, sources));
    }

    @Override
    public List<InstrumentSourcePlan> findAll() {
        Map<InstrumentCode, List<InstrumentMarketDataSource>> groupedSources = new LinkedHashMap<>();

        dsl.select(
                        INSTRUMENT_MARKET_DATA_SOURCE.INSTRUMENT_CODE,
                        INSTRUMENT_MARKET_DATA_SOURCE.PROVIDER_CODE,
                        INSTRUMENT_MARKET_DATA_SOURCE.ACTIVE,
                        INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY
                )
                .from(INSTRUMENT_MARKET_DATA_SOURCE)
                .orderBy(
                        INSTRUMENT_MARKET_DATA_SOURCE.INSTRUMENT_CODE.asc(),
                        INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY.asc()
                )
                .fetch()
                .forEach(record -> {
                    InstrumentCode instrumentCode =
                            new InstrumentCode(record.get(INSTRUMENT_MARKET_DATA_SOURCE.INSTRUMENT_CODE));

                    InstrumentMarketDataSource source = toSource(
                            record.get(INSTRUMENT_MARKET_DATA_SOURCE.PROVIDER_CODE),
                            record.get(INSTRUMENT_MARKET_DATA_SOURCE.ACTIVE),
                            record.get(INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY)
                    );

                    groupedSources
                            .computeIfAbsent(instrumentCode, ignored -> new ArrayList<>())
                            .add(source);
                });

        List<InstrumentSourcePlan> plans = new ArrayList<>(groupedSources.size());

        for (Map.Entry<InstrumentCode, List<InstrumentMarketDataSource>> entry : groupedSources.entrySet()) {
            plans.add(new InstrumentSourcePlan(entry.getKey(), entry.getValue()));
        }

        return List.copyOf(plans);
    }

    @Override
    public void create(InstrumentSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        dsl.transaction(configuration -> {
            DSLContext tx = configuration.dsl();

            if (planExists(tx, plan.instrumentCode())) {
                throw new IllegalStateException(
                        "Instrument source plan for instrument '"
                                + plan.instrumentCode().value()
                                + "' already exists"
                );
            }

            for (InstrumentMarketDataSource source : plan.sources()) {
                insertSource(tx, plan.instrumentCode(), source);
            }
        });
    }

    @Override
    public void deleteByInstrumentCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        dsl.deleteFrom(INSTRUMENT_MARKET_DATA_SOURCE)
                .where(INSTRUMENT_MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .execute();
    }

    /* Проверяет, существует ли уже план для инструмента. */
    private boolean planExists(DSLContext dsl, InstrumentCode instrumentCode) {
        Objects.requireNonNull(dsl, "dsl must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return dsl.select(INSTRUMENT_MARKET_DATA_SOURCE.INSTRUMENT_CODE)
                .from(INSTRUMENT_MARKET_DATA_SOURCE)
                .where(INSTRUMENT_MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .limit(1)
                .fetchOptional()
                .isPresent();
    }

    /* Вставляет один источник инструмента. */
    private void insertSource(
            DSLContext dsl,
            InstrumentCode instrumentCode,
            InstrumentMarketDataSource source
    ) {
        Objects.requireNonNull(dsl, "dsl must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(source, "source must not be null");

        dsl.insertInto(INSTRUMENT_MARKET_DATA_SOURCE)
                .set(INSTRUMENT_MARKET_DATA_SOURCE.INSTRUMENT_CODE, instrumentCode.value())
                .set(INSTRUMENT_MARKET_DATA_SOURCE.PROVIDER_CODE, source.providerCode().value())
                .set(INSTRUMENT_MARKET_DATA_SOURCE.ACTIVE, source.active())
                .set(INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY, source.priority())
                .execute();
    }

    /* Маппинг строки БД в доменный источник. */
    private InstrumentMarketDataSource toSource(
            String providerCode,
            Boolean active,
            Integer priority
    ) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(active, "active must not be null");
        Objects.requireNonNull(priority, "priority must not be null");

        return new InstrumentMarketDataSource(
                new ProviderCode(providerCode),
                active,
                priority
        );
    }
}
