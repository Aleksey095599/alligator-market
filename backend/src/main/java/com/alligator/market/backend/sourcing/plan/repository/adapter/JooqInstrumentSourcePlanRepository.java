package com.alligator.market.backend.sourcing.plan.repository.adapter;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.jooq.DSLContext;

import java.util.*;

import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentMarketDataSource.INSTRUMENT_MARKET_DATA_SOURCE;
import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentSourcePlan.INSTRUMENT_SOURCE_PLAN;

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

        List<MarketDataSource> sources = dsl
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
        Map<InstrumentCode, List<MarketDataSource>> groupedSources = new LinkedHashMap<>();

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

                    MarketDataSource source = toSource(
                            record.get(INSTRUMENT_MARKET_DATA_SOURCE.PROVIDER_CODE),
                            record.get(INSTRUMENT_MARKET_DATA_SOURCE.ACTIVE),
                            record.get(INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY)
                    );

                    groupedSources
                            .computeIfAbsent(instrumentCode, ignored -> new ArrayList<>())
                            .add(source);
                });

        List<InstrumentSourcePlan> plans = new ArrayList<>(groupedSources.size());

        for (Map.Entry<InstrumentCode, List<MarketDataSource>> entry : groupedSources.entrySet()) {
            plans.add(new InstrumentSourcePlan(entry.getKey(), entry.getValue()));
        }

        return List.copyOf(plans);
    }

    @Override
    public boolean createIfAbsent(InstrumentSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        return dsl.transactionResult(configuration -> {
            DSLContext tx = configuration.dsl();

            int insertedPlans = tx.insertInto(INSTRUMENT_SOURCE_PLAN)
                    .set(INSTRUMENT_SOURCE_PLAN.INSTRUMENT_CODE, plan.instrumentCode().value())
                    .onConflict(INSTRUMENT_SOURCE_PLAN.INSTRUMENT_CODE)
                    .doNothing()
                    .execute();

            if (insertedPlans == 0) {
                return false;
            }

            for (MarketDataSource source : plan.sources()) {
                insertSource(tx, plan.instrumentCode(), source);
            }

            return true;
        });
    }

    @Override
    public void deleteByInstrumentCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        dsl.deleteFrom(INSTRUMENT_SOURCE_PLAN)
                .where(INSTRUMENT_SOURCE_PLAN.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .execute();
    }

    /* Вставляет один источник инструмента. */
    private void insertSource(
            DSLContext dsl,
            InstrumentCode instrumentCode,
            MarketDataSource source
    ) {
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
    private MarketDataSource toSource(
            String providerCode,
            Boolean active,
            Integer priority
    ) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(active, "active must not be null");
        Objects.requireNonNull(priority, "priority must not be null");

        return new MarketDataSource(
                new ProviderCode(providerCode),
                active,
                priority
        );
    }
}
