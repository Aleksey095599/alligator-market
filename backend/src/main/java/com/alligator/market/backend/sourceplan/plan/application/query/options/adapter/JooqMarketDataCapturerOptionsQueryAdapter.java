package com.alligator.market.backend.sourceplan.plan.application.query.options.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.options.model.MarketDataCapturerOption;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.MarketDataCapturerOptionsQueryPort;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.capturer.vo.CapturerDisplayName;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.CapturerPassport.CAPTURER_PASSPORT;
import static com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassportRegistryStatus.ACTIVE;

public final class JooqMarketDataCapturerOptionsQueryAdapter implements MarketDataCapturerOptionsQueryPort {
    private final DSLContext dsl;

    public JooqMarketDataCapturerOptionsQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<MarketDataCapturerOption> findAllMarketDataCapturers() {
        return dsl.select(
                        CAPTURER_PASSPORT.CAPTURER_CODE,
                        CAPTURER_PASSPORT.DISPLAY_NAME
                )
                .from(CAPTURER_PASSPORT)
                .where(CAPTURER_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
                .orderBy(CAPTURER_PASSPORT.CAPTURER_CODE.asc())
                .fetch(record -> new MarketDataCapturerOption(
                        new CapturerCode(record.get(CAPTURER_PASSPORT.CAPTURER_CODE)),
                        new CapturerDisplayName(record.get(CAPTURER_PASSPORT.DISPLAY_NAME))
                ));
    }
}
