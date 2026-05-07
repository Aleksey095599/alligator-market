package com.alligator.market.backend.sourceplan.plan.persistence.jooq.repository;

import com.alligator.market.backend.common.persistence.constraint.DbConstraintErrors;
import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataCaptureProcessCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlan;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlanEntry;
import com.alligator.market.domain.sourceplan.repository.MarketDataSourcePlanRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSource.MARKET_DATA_SOURCE;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePlan.MARKET_DATA_SOURCE_PLAN;

/**
 * jOOQ-реализация репозитория планов источников.
 */
public final class JooqMarketDataSourcePlanRepositoryAdapter implements MarketDataSourcePlanRepository {

    /* Имя FK-ограничения market_data_source_plan -> instrument_registry по коду инструмента. */
    private static final String FK_MARKET_DATA_SOURCE_PLAN_INSTRUMENT =
            "fk_market_data_source_plan_instrument";
    /* Имя FK-ограничения market_data_source_plan -> capture_process_passport по коду процесса захвата. */
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
    public Optional<MarketDataSourcePlan> findByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        return findByMarketDataCaptureProcessCodeAndInstrumentCode(captureProcessCode, instrumentCode, false);
    }

    @Override
    public Optional<MarketDataSourcePlan> findActiveByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        return findByMarketDataCaptureProcessCodeAndInstrumentCode(captureProcessCode, instrumentCode, true);
    }

    private Optional<MarketDataSourcePlan> findByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode,
            boolean activeOnly
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        Condition condition = MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE.eq(captureProcessCode.value())
                .and(MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(instrumentCode.value()));

        if (activeOnly) {
            condition = condition.and(MARKET_DATA_SOURCE.LIFECYCLE_STATUS.eq(ACTIVE.name()));
        }

        // Читаем строки плана из БД в порядке приоритета.
        List<MarketDataSourcePlanEntry> entries = dsl
                .select(
                        MARKET_DATA_SOURCE.SOURCE_CODE,
                        MARKET_DATA_SOURCE.PRIORITY
                )
                .from(MARKET_DATA_SOURCE)
                .where(condition)
                .orderBy(MARKET_DATA_SOURCE.PRIORITY.asc())
                .fetch(record -> toEntry(
                        record.get(MARKET_DATA_SOURCE.SOURCE_CODE),
                        record.get(MARKET_DATA_SOURCE.PRIORITY)
                ));

        // Если источников нет — плана тоже нет.
        if (entries.isEmpty()) {
            return Optional.empty();
        }

        // Собираем доменный план и возвращаем его.
        return Optional.of(new MarketDataSourcePlan(captureProcessCode, instrumentCode, entries));
    }

    @Override
    public List<MarketDataSourcePlan> findAll() {
        // Группируем строки market_data_source по идентичности плана.
        Map<PlanKey, List<MarketDataSourcePlanEntry>> groupedEntries = new LinkedHashMap<>();

        dsl.select(
                        MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE,
                        MARKET_DATA_SOURCE.INSTRUMENT_CODE,
                        MARKET_DATA_SOURCE.SOURCE_CODE,
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
                            new MarketDataCaptureProcessCode(record.get(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE)),
                            new InstrumentCode(record.get(MARKET_DATA_SOURCE.INSTRUMENT_CODE))
                    );

                    MarketDataSourcePlanEntry entry = toEntry(
                            record.get(MARKET_DATA_SOURCE.SOURCE_CODE),
                            record.get(MARKET_DATA_SOURCE.PRIORITY)
                    );

                    groupedEntries
                            .computeIfAbsent(planKey, ignored -> new ArrayList<>())
                            .add(entry);
                });

        // Преобразуем каждую группу в доменный план.
        List<MarketDataSourcePlan> plans = new ArrayList<>(groupedEntries.size());

        for (Map.Entry<PlanKey, List<MarketDataSourcePlanEntry>> groupedEntry : groupedEntries.entrySet()) {
            PlanKey planKey = groupedEntry.getKey();
            plans.add(new MarketDataSourcePlan(
                    planKey.captureProcessCode(),
                    planKey.instrumentCode(),
                    groupedEntry.getValue()
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

                // План создан — добавляем его строки.
                for (MarketDataSourcePlanEntry entry : plan.entries()) {
                    insertEntry(tx, plan.captureProcessCode(), plan.instrumentCode(), entry);
                }

                // Успешное создание.
                return true;
            });
        } catch (DataIntegrityViolationException ex) {
            // FK-ошибка => такого процесса захвата нет в passport projection.
            if (DbConstraintErrors.isViolationOf(ex, FK_MARKET_DATA_SOURCE_PLAN_CAPTURE_PROCESS)) {
                throw new MarketDataCaptureProcessCodeNotFoundException(plan.captureProcessCode());
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

            // Удаляем старые строки плана.
            tx.deleteFrom(MARKET_DATA_SOURCE)
                    .where(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE.eq(plan.captureProcessCode().value()))
                    .and(MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(plan.instrumentCode().value()))
                    .execute();

            // Вставляем новый набор строк плана.
            for (MarketDataSourcePlanEntry entry : plan.entries()) {
                insertEntry(tx, plan.captureProcessCode(), plan.instrumentCode(), entry);
            }

            return true;
        });
    }

    @Override
    public boolean deleteIfExistsByMarketDataCaptureProcessCodeAndInstrumentCode(
            MarketDataCaptureProcessCode captureProcessCode,
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

    /* Вставляет одну строку плана источников в таблицу market_data_source. */
    private void insertEntry(
            DSLContext dsl,
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode,
            MarketDataSourcePlanEntry entry
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(entry, "entry must not be null");

        dsl.insertInto(MARKET_DATA_SOURCE)
                .set(MARKET_DATA_SOURCE_CAPTURE_PROCESS_CODE, captureProcessCode.value())
                .set(MARKET_DATA_SOURCE.INSTRUMENT_CODE, instrumentCode.value())
                .set(MARKET_DATA_SOURCE.SOURCE_CODE, entry.sourceCode().value())
                .set(MARKET_DATA_SOURCE.PRIORITY, entry.priority())
                .set(MARKET_DATA_SOURCE.LIFECYCLE_STATUS, ACTIVE.name())
                .execute();
    }

    /* Маппинг строки market_data_source в доменную строку плана. */
    private MarketDataSourcePlanEntry toEntry(
            String sourceCode,
            Integer priority
    ) {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(priority, "priority must not be null");

        return new MarketDataSourcePlanEntry(
                new MarketDataSourceCode(sourceCode),
                priority
        );
    }

    private record PlanKey(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
    }
}
