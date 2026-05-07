package com.alligator.market.backend.sourceplan.plan.application.query.options.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.options.model.MarketDataCaptureProcessOption;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.MarketDataCaptureProcessOptionsQueryPort;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessDisplayName;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.CaptureProcessPassport.CAPTURE_PROCESS_PASSPORT;

/**
 * jOOQ-адаптер порта получения доступных процессов захвата.
 */
public final class JooqMarketDataCaptureProcessOptionsQueryAdapter implements MarketDataCaptureProcessOptionsQueryPort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqMarketDataCaptureProcessOptionsQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<MarketDataCaptureProcessOption> findAllMarketDataCaptureProcesses() {
        return dsl.select(
                        CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE,
                        CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME
                )
                .from(CAPTURE_PROCESS_PASSPORT)
                .where(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
                .orderBy(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE.asc())
                .fetch(record -> new MarketDataCaptureProcessOption(
                        new MarketDataCaptureProcessCode(record.get(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE)),
                        new MarketDataCaptureProcessDisplayName(record.get(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME))
                ));
    }
}
