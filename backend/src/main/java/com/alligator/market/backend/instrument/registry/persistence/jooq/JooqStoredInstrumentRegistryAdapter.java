package com.alligator.market.backend.instrument.registry.persistence.jooq;

import com.alligator.market.domain.instrument.registry.StoredInstrumentRegistry;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentRegistry.INSTRUMENT_REGISTRY;

public final class JooqStoredInstrumentRegistryAdapter implements StoredInstrumentRegistry {
    private final DSLContext dsl;

    public JooqStoredInstrumentRegistryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean contains(InstrumentCode code) {
        Objects.requireNonNull(code, "code must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(INSTRUMENT_REGISTRY)
                        .where(INSTRUMENT_REGISTRY.INSTRUMENT_CODE.eq(code.value()))
        );
    }

    @Override
    public List<InstrumentCode> registeredCodes() {
        return dsl.select(INSTRUMENT_REGISTRY.INSTRUMENT_CODE)
                .from(INSTRUMENT_REGISTRY)
                .orderBy(INSTRUMENT_REGISTRY.INSTRUMENT_CODE.asc())
                .fetch(INSTRUMENT_REGISTRY.INSTRUMENT_CODE)
                .stream()
                .map(InstrumentCode::new)
                .toList();
    }
}
