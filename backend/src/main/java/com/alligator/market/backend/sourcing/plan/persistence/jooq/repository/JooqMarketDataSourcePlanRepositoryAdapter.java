package com.alligator.market.backend.sourcing.plan.persistence.jooq.repository;

import com.alligator.market.backend.common.persistence.constraint.DbConstraintErrors;
import com.alligator.market.backend.sourcing.plan.application.exception.CaptureProcessCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSource.MARKET_DATA_SOURCE;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePlan.MARKET_DATA_SOURCE_PLAN;

/**
 * jOOQ-реализация репозитория планов источников.
 */
public final class JooqMarketDataSourcePlanRepositoryAdapter implements MarketDataSourcePlanRepository {

    /* Имя FK-ограничения market_data_source_plan -> instrument_registry по коду инструмента. */
    private static final String FK_MARKET_DATA_SOURCE_PLAN_INSTRUMENT =
            "fk_market_data_source_plan_instrument";
    /* Имя FK-ограничения market_data_source_plan -> capture_process_passport по коду процесса фиксации. */
    private static final String FK_MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS =
            "fk_market_data_source_plan_capture_process";
    private static final Field<String> MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE =
            MARKET_DATA_SOURCE.COLLECTION_PROCESS_CODE;
    private static final Field<String> MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS_CODE =
            MARKET_DATA_SOURCE_PLAN.COLLECTION_PROCESS_CODE;

    /* DSLContext для выполнения SQL через jOOQ. */
    private final DSLContext dsl;

    public JooqMarketDataSourcePlanRepositoryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Optional<MarketDataSourcePlan> findByCaptureProcessCodeAndInstrumentCode(
            CaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Читаем все источники инструмента из БД в порядке приоритета.
        List<MarketDataSource> sources = dsl
                .select(
                        MARKET_DATA_SOURCE.PROVIDER_CODE,
                        MARKET_DATA_SOURCE.PRIORITY
                )
                .from(MARKET_DATA_SOURCE)
                .where(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE.eq(captureProcessCode.value()))
                .and(MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .orderBy(MARKET_DATA_SOURCE.PRIORITY.asc())
                .fetch(record -> toSource(
                        record.get(MARKET_DATA_SOURCE.PROVIDER_CODE),
                        record.get(MARKET_DATA_SOURCE.PRIORITY)
                ));

        // Если источников нет — плана тоже нет.
        if (sources.isEmpty()) {
            return Optional.empty();
        }

        // Собираем доменный план и возвращаем его.
        return Optional.of(new MarketDataSourcePlan(captureProcessCode, instrumentCode, sources));
    }

    @Override
    public List<MarketDataSourcePlan> findAll() {
        // Группируем источники по идентичности плана.
        Map<PlanKey, List<MarketDataSource>> groupedSources = new LinkedHashMap<>();

        dsl.select(
                        MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE,
                        MARKET_DATA_SOURCE.INSTRUMENT_CODE,
                        MARKET_DATA_SOURCE.PROVIDER_CODE,
                        MARKET_DATA_SOURCE.PRIORITY
                )
                .from(MARKET_DATA_SOURCE)
                .orderBy(
                        MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE.asc(),
                        MARKET_DATA_SOURCE.INSTRUMENT_CODE.asc(),
                        MARKET_DATA_SOURCE.PRIORITY.asc()
                )
                .fetch()
                .forEach(record -> {
                    PlanKey planKey = new PlanKey(
                            new CaptureProcessCode(record.get(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE)),
                            new InstrumentCode(record.get(MARKET_DATA_SOURCE.INSTRUMENT_CODE))
                    );

                    MarketDataSource source = toSource(
                            record.get(MARKET_DATA_SOURCE.PROVIDER_CODE),
                            record.get(MARKET_DATA_SOURCE.PRIORITY)
                    );

                    groupedSources
                            .computeIfAbsent(planKey, ignored -> new ArrayList<>())
                            .add(source);
                });

        // Преобразуем каждую группу в доменный план.
        List<MarketDataSourcePlan> plans = new ArrayList<>(groupedSources.size());

        for (Map.Entry<PlanKey, List<MarketDataSource>> entry : groupedSources.entrySet()) {
            PlanKey planKey = entry.getKey();
            plans.add(new MarketDataSourcePlan(
                    planKey.captureProcessCode(),
                    planKey.instrumentCode(),
                    entry.getValue()
            ));
        }

        // Возвращаем неизменяемый список.
        return List.copyOf(plans);
    }

    @Override
    public boolean createIfAbsent(MarketDataSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        try {
            // В транзакции пробуем создать запись плана.
            return dsl.transactionResult(configuration -> {
                DSLContext tx = configuration.dsl();

                int insertedPlans = tx.insertInto(MARKET_DATA_SOURCE_PLAN)
                        .set(MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS_CODE, plan.captureProcessCode().value())
                        .set(MARKET_DATA_SOURCE_PLAN.INSTRUMENT_CODE, plan.instrumentCode().value())
                        .onConflict(
                                MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS_CODE,
                                MARKET_DATA_SOURCE_PLAN.INSTRUMENT_CODE
                        )
                        .doNothing()
                        .execute();

                if (insertedPlans == 0) {
                    // План уже существует.
                    return false;
                }

                // План создан — добавляем его источники.
                for (MarketDataSource source : plan.sources()) {
                    insertSource(tx, plan.captureProcessCode(), plan.instrumentCode(), source);
                }

                // Успешное создание.
                return true;
            });
        } catch (DataIntegrityViolationException ex) {
            // FK-ошибка => такого процесса фиксации нет в passport projection.
            if (DbConstraintErrors.isViolationOf(ex, FK_MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS)) {
                throw new CaptureProcessCodeNotFoundException(plan.captureProcessCode());
            }

            // FK-ошибка => такого инструмента нет в реестре.
            if (DbConstraintErrors.isViolationOf(ex, FK_MARKET_DATA_SOURCE_PLAN_INSTRUMENT)) {
                throw new InstrumentCodeNotFoundException(plan.instrumentCode());
            }

            throw ex;
        }
    }


    @Override
    public boolean replaceIfExists(MarketDataSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        return dsl.transactionResult(configuration -> {
            DSLContext tx = configuration.dsl();

            // Проверяем, что план есть, и блокируем его до конца транзакции.
            boolean planExists = tx.selectOne()
                    .from(MARKET_DATA_SOURCE_PLAN)
                    .where(MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS_CODE.eq(plan.captureProcessCode().value()))
                    .and(MARKET_DATA_SOURCE_PLAN.INSTRUMENT_CODE.eq(plan.instrumentCode().value()))
                    .forUpdate()
                    .fetchOptional()
                    .isPresent();

            if (!planExists) {
                return false;
            }

            // Удаляем старые источники.
            tx.deleteFrom(MARKET_DATA_SOURCE)
                    .where(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE.eq(plan.captureProcessCode().value()))
                    .and(MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(plan.instrumentCode().value()))
                    .execute();

            // Вставляем новый набор источников.
            for (MarketDataSource source : plan.sources()) {
                insertSource(tx, plan.captureProcessCode(), plan.instrumentCode(), source);
            }

            return true;
        });
    }

    @Override
    public boolean deleteIfExistsByCaptureProcessCodeAndInstrumentCode(
            CaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Удаляем план; связанные источники удаляются по каскаду в БД.
        int deletedRows = dsl.deleteFrom(MARKET_DATA_SOURCE_PLAN)
                .where(MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS_CODE.eq(captureProcessCode.value()))
                .and(MARKET_DATA_SOURCE_PLAN.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .execute();

        return deletedRows > 0;
    }

    /* Вставляет один источник инструмента. */
    private void insertSource(
            DSLContext dsl,
            CaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode,
            MarketDataSource source
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(source, "source must not be null");

        dsl.insertInto(MARKET_DATA_SOURCE)
                .set(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE, captureProcessCode.value())
                .set(MARKET_DATA_SOURCE.INSTRUMENT_CODE, instrumentCode.value())
                .set(MARKET_DATA_SOURCE.PROVIDER_CODE, source.providerCode().value())
                .set(MARKET_DATA_SOURCE.PRIORITY, source.priority())
                .execute();
    }

    /* Маппинг строки БД в доменный источник. */
    private MarketDataSource toSource(
            String providerCode,
            Integer priority
    ) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(priority, "priority must not be null");

        return new MarketDataSource(
                new ProviderCode(providerCode),
                priority
        );
    }

    private record PlanKey(
            CaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
    }
}
