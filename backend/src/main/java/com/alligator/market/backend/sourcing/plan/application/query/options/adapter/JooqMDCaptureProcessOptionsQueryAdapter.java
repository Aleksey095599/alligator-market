package com.alligator.market.backend.sourcing.plan.application.query.options.adapter;

import com.alligator.market.backend.sourcing.plan.application.query.options.model.MDCaptureProcessOption;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.MDCaptureProcessOptionsQueryPort;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessDisplayName;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.CaptureProcessPassport.CAPTURE_PROCESS_PASSPORT;

/**
 * jOOQ-адаптер порта получения доступных процессов фиксации.
 */
public final class JooqMDCaptureProcessOptionsQueryAdapter implements MDCaptureProcessOptionsQueryPort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqMDCaptureProcessOptionsQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<MDCaptureProcessOption> findAllMDCaptureProcesses() {
        return dsl.select(
                        CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE,
                        CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME
                )
                .from(CAPTURE_PROCESS_PASSPORT)
                .where(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
                .orderBy(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE.asc())
                .fetch(record -> new MDCaptureProcessOption(
                        new MDCaptureProcessCode(record.get(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE)),
                        new MDCaptureProcessDisplayName(record.get(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME))
                ));
    }
}
