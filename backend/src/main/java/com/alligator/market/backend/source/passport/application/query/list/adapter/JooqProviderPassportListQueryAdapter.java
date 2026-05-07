package com.alligator.market.backend.source.passport.application.query.list.adapter;

import com.alligator.market.backend.source.passport.application.query.list.model.ProviderPassportListItem;
import com.alligator.market.backend.source.passport.application.query.list.port.ProviderPassportListQueryPort;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;

/**
 * jOOQ implementation of {@link ProviderPassportListQueryPort}.
 */
public final class JooqProviderPassportListQueryAdapter implements ProviderPassportListQueryPort {

    private final DSLContext dsl;

    public JooqProviderPassportListQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<ProviderPassportListItem> findAll() {
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
                .fetch(record -> new ProviderPassportListItem(
                        record.get(PROVIDER_PASSPORT.PROVIDER_CODE),
                        record.get(PROVIDER_PASSPORT.DISPLAY_NAME),
                        record.get(PROVIDER_PASSPORT.DELIVERY_MODE),
                        record.get(PROVIDER_PASSPORT.ACCESS_METHOD),
                        record.get(PROVIDER_PASSPORT.BULK_SUBSCRIPTION),
                        record.get(PROVIDER_PASSPORT.LIFECYCLE_STATUS)
                ));
    }
}
