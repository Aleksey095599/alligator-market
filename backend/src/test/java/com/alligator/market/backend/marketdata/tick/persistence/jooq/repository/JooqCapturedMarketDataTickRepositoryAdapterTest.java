package com.alligator.market.backend.marketdata.tick.persistence.jooq.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.capture.CapturedMarketDataTick;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessCode;
import com.alligator.market.domain.marketdata.tick.level.source.classification.SourceMarketDataTickType;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;
import com.alligator.market.domain.provider.vo.ProviderCode;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.UUID;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.using;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("dev")
class JooqCapturedMarketDataTickRepositoryAdapterTest {

    private static final Table<?> CAPTURED_MARKET_DATA_TICK = table(name("captured_market_data_tick"));

    private static final Field<Long> ID = field(name("id"), Long.class);
    private static final Field<String> COLLECTION_PROCESS_CODE = field(name("collection_process_code"), String.class);
    private static final Field<String> INSTRUMENT_CODE = field(name("instrument_code"), String.class);
    private static final Field<String> PROVIDER_CODE = field(name("provider_code"), String.class);
    private static final Field<String> SOURCE_TICK_TYPE = field(name("source_tick_type"), String.class);
    private static final Field<String> SOURCE_INSTRUMENT_CODE = field(name("source_instrument_code"), String.class);
    private static final Field<OffsetDateTime> SOURCE_TIMESTAMP = field(name("source_timestamp"), OffsetDateTime.class);
    private static final Field<OffsetDateTime> RECEIVED_TIMESTAMP = field(name("received_timestamp"), OffsetDateTime.class);
    private static final Field<BigDecimal> LAST_PRICE = field(name("last_price"), BigDecimal.class);
    private static final Field<BigDecimal> BID_PRICE = field(name("bid_price"), BigDecimal.class);
    private static final Field<BigDecimal> ASK_PRICE = field(name("ask_price"), BigDecimal.class);

    @Test
    void shouldSaveCapturedLastPriceTick() throws Exception {
        String dbUrl = System.getProperty("db.url", "jdbc:postgresql://localhost:5432/postgres");
        String dbUser = System.getProperty("db.user", "postgres");
        String dbPassword = System.getProperty("db.password", "1234");

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            connection.setAutoCommit(false);

            try {
                DSLContext dsl = using(connection, SQLDialect.POSTGRES);
                JooqCapturedMarketDataTickRepositoryAdapter repository =
                        new JooqCapturedMarketDataTickRepositoryAdapter(dsl);

                String processCode = "TEST_TWAP_CNYRUB_" +
                        UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT);

                Instant sourceTimestamp = Instant.parse("2026-05-03T10:15:30Z");
                Instant receivedTimestamp = Instant.parse("2026-05-03T10:15:31Z");

                CapturedMarketDataTick tick = new CapturedMarketDataTick(
                        MarketDataCollectionProcessCode.of(processCode),
                        InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM"),
                        ProviderCode.of("MOEX_ISS"),
                        new SourceLastPriceTick(
                                SourceInstrumentCode.of("CNYRUB_TOM"),
                                new BigDecimal("10.9500"),
                                sourceTimestamp
                        ),
                        receivedTimestamp
                );

                repository.save(tick);

                Record record = dsl.select(
                                ID,
                                COLLECTION_PROCESS_CODE,
                                INSTRUMENT_CODE,
                                PROVIDER_CODE,
                                SOURCE_TICK_TYPE,
                                SOURCE_INSTRUMENT_CODE,
                                SOURCE_TIMESTAMP,
                                RECEIVED_TIMESTAMP,
                                LAST_PRICE,
                                BID_PRICE,
                                ASK_PRICE
                        )
                        .from(CAPTURED_MARKET_DATA_TICK)
                        .where(COLLECTION_PROCESS_CODE.eq(processCode))
                        .fetchOne();

                assertNotNull(record);
                assertNotNull(record.get(ID));

                assertEquals(processCode, record.get(COLLECTION_PROCESS_CODE));
                assertEquals("FOREX_SPOT_CNYRUB_TOM", record.get(INSTRUMENT_CODE));
                assertEquals("MOEX_ISS", record.get(PROVIDER_CODE));
                assertEquals(SourceMarketDataTickType.LAST_PRICE.name(), record.get(SOURCE_TICK_TYPE));
                assertEquals("CNYRUB_TOM", record.get(SOURCE_INSTRUMENT_CODE));

                assertEquals(sourceTimestamp, record.get(SOURCE_TIMESTAMP).toInstant());
                assertEquals(receivedTimestamp, record.get(RECEIVED_TIMESTAMP).toInstant());

                assertEquals(0, new BigDecimal("10.9500").compareTo(record.get(LAST_PRICE)));
                assertNull(record.get(BID_PRICE));
                assertNull(record.get(ASK_PRICE));
            } finally {
                connection.rollback();
            }
        }
    }
}
