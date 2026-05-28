package com.alligator.market.domain.source.handler;

import com.alligator.market.domain.instrument.Asset;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.Product;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.AbstractMarketDataSource;
import com.alligator.market.domain.source.exception.InstrumentNotSupportedByHandlerException;
import com.alligator.market.domain.source.handler.passport.AccessMethod;
import com.alligator.market.domain.source.handler.passport.DeliveryMode;
import com.alligator.market.domain.source.handler.passport.SourceHandlerPassport;
import com.alligator.market.domain.source.handler.policy.SourceHandlerPolicy;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.vo.HandlerCode;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractInstrumentHandlerTest {

    @Test
    void rejectsInstrumentOutsideSupportedProfileWithDomainException() {
        InstrumentCode supportedCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        InstrumentCode unsupportedCode = InstrumentCode.of("FOREX_SPOT_USDRUB_TOM");
        TestInstrumentHandler handler = new TestInstrumentHandler(Set.of(new TestInstrument(supportedCode)));
        new TestMarketDataSource(handler);

        InstrumentNotSupportedByHandlerException ex = assertThrows(
                InstrumentNotSupportedByHandlerException.class,
                () -> handler.streamSourceTicks(new TestInstrument(unsupportedCode))
        );

        assertEquals(unsupportedCode, ex.instrumentCode());
        assertEquals(TestInstrumentHandler.HANDLER_CODE, ex.handlerCode());
        assertEquals(
                InstrumentNotSupportedByHandlerException.Reason.INSTRUMENT_CODE_NOT_SUPPORTED,
                ex.reason()
        );
        assertEquals(supportedCode.value(), ex.expectedValue());
        assertEquals(unsupportedCode.value(), ex.actualValue());
    }

    @Test
    void rejectsInstrumentWithUnexpectedAsset() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        TestInstrumentHandler handler = new TestInstrumentHandler(Set.of(new TestInstrument(instrumentCode)));
        new TestMarketDataSource(handler);

        InstrumentNotSupportedByHandlerException ex = assertThrows(
                InstrumentNotSupportedByHandlerException.class,
                () -> handler.streamSourceTicks(new TestInstrument(
                        instrumentCode,
                        Asset.EQUITY,
                        Product.SPOT
                ))
        );

        assertEquals(InstrumentNotSupportedByHandlerException.Reason.ASSET_MISMATCH, ex.reason());
        assertEquals(Asset.FOREX.code(), ex.expectedValue());
        assertEquals(Asset.EQUITY.code(), ex.actualValue());
    }

    @Test
    void rejectsInstrumentWithUnexpectedProduct() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        TestInstrumentHandler handler = new TestInstrumentHandler(Set.of(new TestInstrument(instrumentCode)));
        new TestMarketDataSource(handler);

        InstrumentNotSupportedByHandlerException ex = assertThrows(
                InstrumentNotSupportedByHandlerException.class,
                () -> handler.streamSourceTicks(new TestInstrument(
                        instrumentCode,
                        Asset.FOREX,
                        Product.FORWARD
                ))
        );

        assertEquals(InstrumentNotSupportedByHandlerException.Reason.PRODUCT_MISMATCH, ex.reason());
        assertEquals(Product.SPOT.code(), ex.expectedValue());
        assertEquals(Product.FORWARD.code(), ex.actualValue());
    }

    private static final class TestMarketDataSource extends AbstractMarketDataSource<TestMarketDataSource> {
        private TestMarketDataSource(TestInstrumentHandler handler) {
            super(
                    SourceCode.of("TEST_SOURCE"),
                    new SourcePassport(SourceDisplayName.of("Test Source")),
                    Set.of(handler)
            );
        }

        @Override
        protected TestMarketDataSource self() {
            return this;
        }
    }

    private static final class TestInstrumentHandler
            extends AbstractInstrumentHandler<TestMarketDataSource, TestInstrument> {
        private static final HandlerCode HANDLER_CODE = HandlerCode.of("TEST_HANDLER");
        private static final SourceHandlerPassport PASSPORT = new TestSourceHandlerPassport();
        private static final SourceHandlerPolicy POLICY = new SourceHandlerPolicy() {
        };

        private TestInstrumentHandler(Set<TestInstrument> supportedInstruments) {
            super(
                    HANDLER_CODE,
                    PASSPORT,
                    TestInstrument.class,
                    supportedInstruments,
                    POLICY
            );
        }

        @Override
        protected Publisher<SourceTick> doStreamSourceTicks(TestInstrument instrument) {
            return Subscriber::onComplete;
        }
    }

    private record TestInstrument(
            InstrumentCode instrumentCode,
            Asset asset,
            Product product
    ) implements Instrument {
        private TestInstrument(InstrumentCode instrumentCode) {
            this(instrumentCode, Asset.FOREX, Product.SPOT);
        }

        @Override
        public InstrumentSymbol instrumentSymbol() {
            return InstrumentSymbol.of(instrumentCode.value());
        }
    }

    private static final class TestSourceHandlerPassport implements SourceHandlerPassport {
        @Override
        public DeliveryMode deliveryMode() {
            return DeliveryMode.PULL;
        }

        @Override
        public AccessMethod accessMethod() {
            return AccessMethod.API_POLL;
        }
    }
}
