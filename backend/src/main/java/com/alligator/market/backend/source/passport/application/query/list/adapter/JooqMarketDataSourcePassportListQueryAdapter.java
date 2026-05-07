package com.alligator.market.backend.source.passport.application.query.list.adapter;

import com.alligator.market.backend.source.passport.application.query.list.model.MarketDataSourcePassportListItem;
import com.alligator.market.backend.source.passport.application.query.list.port.MarketDataSourcePassportListQueryPort;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePassport.MARKET_DATA_SOURCE_PASSPORT;

/**
 * jOOQ implementation of {@link MarketDataSourcePassportListQueryPort}.
 */
public final class JooqMarketDataSourcePassportListQueryAdapter implements MarketDataSourcePassportListQueryPort {

    private final DSLContext dsl;

    public JooqMarketDataSourcePassportListQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<MarketDataSourcePassportListItem> findAll() {
        return dsl.select(
                        MARKET_DATA_SOURCE_PASSPORT.SOURCE_CODE,
                        MARKET_DATA_SOURCE_PASSPORT.DISPLAY_NAME,
                        MARKET_DATA_SOURCE_PASSPORT.DELIVERY_MODE,
                        MARKET_DATA_SOURCE_PASSPORT.ACCESS_METHOD,
                        MARKET_DATA_SOURCE_PASSPORT.BULK_SUBSCRIPTION,
                        MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS
                )
                .from(MARKET_DATA_SOURCE_PASSPORT)
                .orderBy(MARKET_DATA_SOURCE_PASSPORT.SOURCE_CODE.asc())
                .fetch(record -> new MarketDataSourcePassportListItem(
                        record.get(MARKET_DATA_SOURCE_PASSPORT.SOURCE_CODE),
                        record.get(MARKET_DATA_SOURCE_PASSPORT.DISPLAY_NAME),
                        record.get(MARKET_DATA_SOURCE_PASSPORT.DELIVERY_MODE),
                        record.get(MARKET_DATA_SOURCE_PASSPORT.ACCESS_METHOD),
                        record.get(MARKET_DATA_SOURCE_PASSPORT.BULK_SUBSCRIPTION),
                        record.get(MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS)
                ));
    }
}
