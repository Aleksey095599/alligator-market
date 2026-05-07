package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.RETIRED;
import static com.alligator.market.backend.infra.jooq.generated.tables.CaptureProcessPassport.CAPTURE_PROCESS_PASSPORT;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSource.MARKET_DATA_SOURCE;
import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectOne;

/**
 * jOOQ implementation of {@link MarketDataSourceLifecycleStatusSyncPort}.
 *
 * <p>Синхронизация монотонная: переводит только ACTIVE source rows в RETIRED и никогда
 * не возвращает RETIRED строки обратно в ACTIVE. Если источник снова нужен, строку плана
 * лучше удалить и добавить заново.</p>
 */
public final class JooqMarketDataSourceLifecycleStatusSyncAdapter
        implements MarketDataSourceLifecycleStatusSyncPort {

    private final DSLContext dsl;

    public JooqMarketDataSourceLifecycleStatusSyncAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void retireSourcesWithoutActiveSourcePassports() {
        Condition sourcePassportIsNotActive = notExists(
                selectOne()
                        .from(PROVIDER_PASSPORT)
                        .where(PROVIDER_PASSPORT.PROVIDER_CODE.eq(MARKET_DATA_SOURCE.PROVIDER_CODE))
                        .and(PROVIDER_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );

        retireActiveSources(sourcePassportIsNotActive);
    }

    @Override
    public void retireSourcesWithoutActiveCaptureProcessPassports() {
        Condition captureProcessIsNotActive = notExists(
                selectOne()
                        .from(CAPTURE_PROCESS_PASSPORT)
                        .where(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE
                                .eq(MARKET_DATA_SOURCE.COLLECTION_PROCESS_CODE))
                        .and(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );

        retireActiveSources(captureProcessIsNotActive);
    }

    private void retireActiveSources(Condition invalidReference) {
        // RETIRED остается терминальным состоянием; здесь меняются только еще активные строки.
        dsl.update(MARKET_DATA_SOURCE)
                .set(MARKET_DATA_SOURCE.LIFECYCLE_STATUS, RETIRED.name())
                .where(MARKET_DATA_SOURCE.LIFECYCLE_STATUS.eq(ACTIVE.name()))
                .and(invalidReference)
                .execute();
    }
}
