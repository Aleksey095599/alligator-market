package com.alligator.market.backend.marketdata.tick.persistence.jooq.repository;

import com.alligator.market.domain.marketdata.tick.level.capture.CapturedMarketDataTick;
import com.alligator.market.domain.marketdata.tick.level.capture.repository.CapturedMarketDataTickRepository;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceTopOfBookQuoteTick;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

/**
 * jOOQ-адаптер append-only сохранения captured-level рыночных тиков.
 */
public final class JooqCapturedMarketDataTickRepositoryAdapter implements CapturedMarketDataTickRepository {

    private static final Table<?> CAPTURED_MARKET_DATA_TICK = table(name("captured_market_data_tick"));

    private static final Field<String> COLLECTION_STREAM_CODE = field(name("collection_stream_code"), String.class);
    private static final Field<String> INSTRUMENT_CODE = field(name("instrument_code"), String.class);
    private static final Field<String> PROVIDER_CODE = field(name("provider_code"), String.class);
    private static final Field<String> SOURCE_TICK_TYPE = field(name("source_tick_type"), String.class);
    private static final Field<String> SOURCE_INSTRUMENT_CODE = field(name("source_instrument_code"), String.class);
    private static final Field<OffsetDateTime> SOURCE_TIMESTAMP = field(name("source_timestamp"), OffsetDateTime.class);
    private static final Field<OffsetDateTime> RECEIVED_TIMESTAMP = field(name("received_timestamp"), OffsetDateTime.class);
    private static final Field<BigDecimal> LAST_PRICE = field(name("last_price"), BigDecimal.class);
    private static final Field<BigDecimal> BID_PRICE = field(name("bid_price"), BigDecimal.class);
    private static final Field<BigDecimal> ASK_PRICE = field(name("ask_price"), BigDecimal.class);

    private final DSLContext dsl;

    public JooqCapturedMarketDataTickRepositoryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void save(CapturedMarketDataTick tick) {
        Objects.requireNonNull(tick, "tick must not be null");

        SourceMarketDataTick sourceTick = tick.sourceTick();
        TickPrices prices = pricesOf(sourceTick);

        dsl.insertInto(CAPTURED_MARKET_DATA_TICK)
                .set(COLLECTION_STREAM_CODE, tick.collectionStreamCode().value())
                .set(INSTRUMENT_CODE, tick.instrumentCode().value())
                .set(PROVIDER_CODE, tick.providerCode().value())
                .set(SOURCE_TICK_TYPE, sourceTick.sourceTickType().name())
                .set(SOURCE_INSTRUMENT_CODE, sourceTick.sourceInstrumentCode().value())
                .set(SOURCE_TIMESTAMP, toOffsetDateTime(sourceTick.sourceTimestamp()))
                .set(RECEIVED_TIMESTAMP, toOffsetDateTime(tick.receivedTimestamp()))
                .set(LAST_PRICE, prices.lastPrice())
                .set(BID_PRICE, prices.bidPrice())
                .set(ASK_PRICE, prices.askPrice())
                .execute();
    }

    private static TickPrices pricesOf(SourceMarketDataTick sourceTick) {
        Objects.requireNonNull(sourceTick, "sourceTick must not be null");

        if (sourceTick instanceof SourceLastPriceTick lastPriceTick) {
            return new TickPrices(lastPriceTick.lastPrice(), null, null);
        }

        if (sourceTick instanceof SourceTopOfBookQuoteTick quoteTick) {
            return new TickPrices(null, quoteTick.bidPrice(), quoteTick.askPrice());
        }

        throw new IllegalArgumentException(
                "Unsupported source tick type: " + sourceTick.getClass().getName()
        );
    }

    private static OffsetDateTime toOffsetDateTime(Instant instant) {
        Objects.requireNonNull(instant, "instant must not be null");
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    private record TickPrices(
            BigDecimal lastPrice,
            BigDecimal bidPrice,
            BigDecimal askPrice
    ) {
    }
}
