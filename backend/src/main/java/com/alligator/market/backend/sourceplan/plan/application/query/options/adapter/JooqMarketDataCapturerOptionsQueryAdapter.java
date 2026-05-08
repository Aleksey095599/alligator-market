package com.alligator.market.backend.sourceplan.plan.application.query.options.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.options.model.MarketDataCapturerOption;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.MarketDataCapturerOptionsQueryPort;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerDisplayName;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataCapturerPassport.MARKET_DATA_CAPTURER_PASSPORT;

public final class JooqMarketDataCapturerOptionsQueryAdapter implements MarketDataCapturerOptionsQueryPort {
    private final DSLContext dsl;

    public JooqMarketDataCapturerOptionsQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<MarketDataCapturerOption> findAllMarketDataCapturers() {
        return dsl.select(
                        MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE,
                        MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME
                )
                .from(MARKET_DATA_CAPTURER_PASSPORT)
                .where(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
                .orderBy(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE.asc())
                .fetch(record -> new MarketDataCapturerOption(
                        new MarketDataCapturerCode(record.get(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE)),
                        new MarketDataCapturerDisplayName(record.get(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME))
                ));
    }
}
