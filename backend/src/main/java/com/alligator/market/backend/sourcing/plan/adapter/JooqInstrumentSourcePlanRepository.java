package com.alligator.market.backend.sourcing.plan.adapter;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.port.InstrumentSourcePlanRepository;
import com.alligator.market.domain.sourcing.source.InstrumentMarketDataSource;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentMarketDataSource.INSTRUMENT_MARKET_DATA_SOURCE;

/* jOOQ-адаптер репозитория планов источников. */
public final class JooqInstrumentSourcePlanRepository implements InstrumentSourcePlanRepository {

    /* DSLContext для выполнения SQL через jOOQ. */
    private final DSLContext dsl;

    public JooqInstrumentSourcePlanRepository(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Optional<InstrumentSourcePlan> findByInstrumentCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        List<InstrumentMarketDataSource> sources = dsl
                .select(
                        INSTRUMENT_MARKET_DATA_SOURCE.PROVIDER_CODE,
                        INSTRUMENT_MARKET_DATA_SOURCE.ACTIVE,
                        INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY
                )
                .from(INSTRUMENT_MARKET_DATA_SOURCE)
                .where(INSTRUMENT_MARKET_DATA_SOURCE.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .orderBy(INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY.asc())
                .fetch(record -> new InstrumentMarketDataSource(
                        new ProviderCode(record.get(INSTRUMENT_MARKET_DATA_SOURCE.PROVIDER_CODE)),
                        record.get(INSTRUMENT_MARKET_DATA_SOURCE.ACTIVE),
                        record.get(INSTRUMENT_MARKET_DATA_SOURCE.PRIORITY)
                ));

        if (sources.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new InstrumentSourcePlan(instrumentCode, sources));
    }

    @Override
    public List<InstrumentSourcePlan> findAll() {
        throw new UnsupportedOperationException("findAll is not implemented yet");
    }

    @Override
    public void save(InstrumentSourcePlan plan) {
        throw new UnsupportedOperationException("save is not implemented yet");
    }
}
