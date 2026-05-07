package com.alligator.market.backend.source.passport.application.query.list.adapter;

import com.alligator.market.backend.source.passport.application.query.list.model.MarketDataSourcePassportListItem;
import com.alligator.market.backend.source.passport.application.query.list.port.MarketDataSourcePassportListQueryPort;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;

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
                        PROVIDER_PASSPORT.PROVIDER_CODE,
                        PROVIDER_PASSPORT.DISPLAY_NAME,
                        PROVIDER_PASSPORT.DELIVERY_MODE,
                        PROVIDER_PASSPORT.ACCESS_METHOD,
                        PROVIDER_PASSPORT.BULK_SUBSCRIPTION,
                        PROVIDER_PASSPORT.LIFECYCLE_STATUS
                )
                .from(PROVIDER_PASSPORT)
                .orderBy(PROVIDER_PASSPORT.PROVIDER_CODE.asc())
                .fetch(record -> new MarketDataSourcePassportListItem(
                        record.get(PROVIDER_PASSPORT.PROVIDER_CODE),
                        record.get(PROVIDER_PASSPORT.DISPLAY_NAME),
                        record.get(PROVIDER_PASSPORT.DELIVERY_MODE),
                        record.get(PROVIDER_PASSPORT.ACCESS_METHOD),
                        record.get(PROVIDER_PASSPORT.BULK_SUBSCRIPTION),
                        record.get(PROVIDER_PASSPORT.LIFECYCLE_STATUS)
                ));
    }
}
