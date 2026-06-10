package com.alligator.market.backend.source.handler.passport.application.query.list.adapter;

import com.alligator.market.backend.source.handler.passport.application.query.list.model.SourceHandlerPassportListItem;
import com.alligator.market.backend.source.handler.passport.application.query.list.port.SourceHandlerPassportListQueryPort;
import com.alligator.market.domain.source.handler.passport.AccessMethod;
import com.alligator.market.domain.source.handler.passport.DeliveryMode;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.util.List;
import java.util.Objects;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

public final class JooqSourceHandlerPassportListQueryAdapter
        implements SourceHandlerPassportListQueryPort {
    private static final Table<?> SOURCE_HANDLER_PASSPORT =
            table(name("source_handler_passport"));
    private static final Field<String> SOURCE_HANDLER_PASSPORT_SOURCE_CODE =
            field(name("source_handler_passport", "source_code"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_HANDLER_CODE =
            field(name("source_handler_passport", "handler_code"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_DELIVERY_MODE =
            field(name("source_handler_passport", "delivery_mode"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_ACCESS_METHOD =
            field(name("source_handler_passport", "access_method"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_REGISTRY_STATUS =
            field(name("source_handler_passport", "registry_status"), String.class);

    private final DSLContext dsl;

    public JooqSourceHandlerPassportListQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<SourceHandlerPassportListItem> findAll() {
        return dsl.select(
                        SOURCE_HANDLER_PASSPORT_SOURCE_CODE,
                        SOURCE_HANDLER_PASSPORT_HANDLER_CODE,
                        SOURCE_HANDLER_PASSPORT_DELIVERY_MODE,
                        SOURCE_HANDLER_PASSPORT_ACCESS_METHOD,
                        SOURCE_HANDLER_PASSPORT_REGISTRY_STATUS
                )
                .from(SOURCE_HANDLER_PASSPORT)
                .orderBy(
                        SOURCE_HANDLER_PASSPORT_SOURCE_CODE.asc(),
                        SOURCE_HANDLER_PASSPORT_HANDLER_CODE.asc()
                )
                .fetch(record -> new SourceHandlerPassportListItem(
                        record.get(SOURCE_HANDLER_PASSPORT_SOURCE_CODE),
                        record.get(SOURCE_HANDLER_PASSPORT_HANDLER_CODE),
                        DeliveryMode.valueOf(record.get(SOURCE_HANDLER_PASSPORT_DELIVERY_MODE)),
                        AccessMethod.valueOf(record.get(SOURCE_HANDLER_PASSPORT_ACCESS_METHOD)),
                        SourceHandlerPassportRecord.RegistryStatus.valueOf(
                                record.get(SOURCE_HANDLER_PASSPORT_REGISTRY_STATUS)
                        )
                ));
    }
}
