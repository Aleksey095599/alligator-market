package com.alligator.market.backend.sourcing.plan.persistence.jooq.repository;

import com.alligator.market.backend.common.persistence.constraint.DbConstraintErrors;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSource.MARKET_DATA_SOURCE;
import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePlan.SOURCE_PLAN;

/**
 * jOOQ-адаптер репозитория планов источников.
 */
public final class JooqInstrumentSourcePlanRepository implements InstrumentSourcePlanRepository {

    /* Имя FK-ограничения source_plan -> instrument_registry по коду инструмента. */
    private static final String FK_SOURCE_PLAN_INSTRUMENT = "fk_instr_source_plan_instrument";

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
                        MARKET_DATA_SOURCE.PROVIDER_CODE,
                        MARKET_DATA_SOURCE.ACTIVE,
                        MARKET_DATA_SOURCE.PRIORITY
                )
                .from(MARKET_DATA_SOURCE)
                .where(MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .orderBy(MARKET_DATA_SOURCE.PRIORITY.asc())
                .fetch(record -> toSource(
                        record.get(MARKET_DATA_SOURCE.PROVIDER_CODE),
                        record.get(MARKET_DATA_SOURCE.ACTIVE),
                        record.get(MARKET_DATA_SOURCE.PRIORITY)
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
                        MARKET_DATA_SOURCE.INSTRUMENT_CODE,
                        MARKET_DATA_SOURCE.PROVIDER_CODE,
                        MARKET_DATA_SOURCE.ACTIVE,
                        MARKET_DATA_SOURCE.PRIORITY
                )
                .from(MARKET_DATA_SOURCE)
                .orderBy(
                        MARKET_DATA_SOURCE.INSTRUMENT_CODE.asc(),
                        MARKET_DATA_SOURCE.PRIORITY.asc()
                )
                .fetch()
                .forEach(record -> {
                    InstrumentCode instrumentCode =
                            new InstrumentCode(record.get(MARKET_DATA_SOURCE.INSTRUMENT_CODE));

                    MarketDataSource source = toSource(
                            record.get(MARKET_DATA_SOURCE.PROVIDER_CODE),
                            record.get(MARKET_DATA_SOURCE.ACTIVE),
                            record.get(MARKET_DATA_SOURCE.PRIORITY)
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

        try {
            return dsl.transactionResult(configuration -> {
                DSLContext tx = configuration.dsl();

                int insertedPlans = tx.insertInto(SOURCE_PLAN)
                        .set(SOURCE_PLAN.INSTRUMENT_CODE, plan.instrumentCode().value())
                        .onConflict(SOURCE_PLAN.INSTRUMENT_CODE)
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
        } catch (DataIntegrityViolationException ex) {
            if (DbConstraintErrors.isViolationOf(ex, FK_SOURCE_PLAN_INSTRUMENT)) {
                throw new InstrumentCodeNotFoundException(plan.instrumentCode());
            }

            throw ex;
        }
    }


    @Override
    public boolean replaceIfExists(InstrumentSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        return dsl.transactionResult(configuration -> {
            DSLContext tx = configuration.dsl();

            boolean planExists = tx.selectOne()
                    .from(SOURCE_PLAN)
                    .where(SOURCE_PLAN.INSTRUMENT_CODE.eq(plan.instrumentCode().value()))
                    .forUpdate()
                    .fetchOptional()
                    .isPresent();

            if (!planExists) {
                return false;
            }

            tx.deleteFrom(MARKET_DATA_SOURCE)
                    .where(MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(plan.instrumentCode().value()))
                    .execute();

            for (MarketDataSource source : plan.sources()) {
                insertSource(tx, plan.instrumentCode(), source);
            }

            return true;
        });
    }

    @Override
    public boolean deleteIfExistsByInstrumentCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        int deletedRows = dsl.deleteFrom(SOURCE_PLAN)
                .where(SOURCE_PLAN.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .execute();

        return deletedRows > 0;
    }

    /* Вставляет один источник инструмента. */
    private void insertSource(
            DSLContext dsl,
            InstrumentCode instrumentCode,
            MarketDataSource source
    ) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(source, "source must not be null");

        dsl.insertInto(MARKET_DATA_SOURCE)
                .set(MARKET_DATA_SOURCE.INSTRUMENT_CODE, instrumentCode.value())
                .set(MARKET_DATA_SOURCE.PROVIDER_CODE, source.providerCode().value())
                .set(MARKET_DATA_SOURCE.ACTIVE, source.active())
                .set(MARKET_DATA_SOURCE.PRIORITY, source.priority())
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
