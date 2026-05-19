package com.alligator.market.domain.source;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.exception.HandlerNotFoundException;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.HandlerCode;
import com.alligator.market.domain.source.vo.SourceCode;
import org.reactivestreams.Publisher;

import java.util.*;

public abstract class AbstractMarketSource<P extends MarketSource>
        implements MarketSource {

    protected final SourceCode sourceCode;
    protected final SourcePassport passport;

    private final Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> instrumentHandlerMap;

    protected abstract P self();

    protected AbstractMarketSource(
            SourceCode sourceCode,
            SourcePassport passport,
            Set<? extends InstrumentHandler<P, ? extends Instrument>> handlers
    ) {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(handlers, "handlers must not be null");

        if (handlers.isEmpty()) {
            throw new IllegalArgumentException("handlers must not be empty");
        }

        this.sourceCode = sourceCode;
        this.passport = passport;

        this.instrumentHandlerMap = buildInstrumentHandlerMap(sourceCode, handlers);

        for (InstrumentHandler<P, ? extends Instrument> h : handlers) {
            h.attachTo(self());
        }
    }

    @Override
    public SourceCode code() {
        return sourceCode;
    }

    @Override
    public SourcePassport passport() {
        return passport;
    }

    @Override
    public final <I extends Instrument> Publisher<SourceTick> streamSourceTicks(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        InstrumentHandler<P, I> handler = findHandlerOrThrow(instrument);

        return handler.streamSourceTicks(instrument);
    }

    private static <P extends MarketSource> Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>>
    buildInstrumentHandlerMap(
            SourceCode sourceCode,
            Set<? extends InstrumentHandler<P, ? extends Instrument>> handlers
    ) {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(handlers, "handlers must not be null");

        Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> map = new LinkedHashMap<>();
        Set<HandlerCode> handlerCodes = new HashSet<>();

        for (InstrumentHandler<P, ? extends Instrument> h : handlers) {
            Objects.requireNonNull(h, "handler must not be null");

            HandlerCode handlerCode = Objects.requireNonNull(h.handlerCode(), "handlerCode must not be null");
            Objects.requireNonNull(h.passport(), "handler.passport must not be null");
            Objects.requireNonNull(h.policy(), "handler.policy must not be null");
            if (!handlerCodes.add(handlerCode)) {
                throw new IllegalStateException("Market source '" + sourceCode.value() +
                        "' contains multiple handlers with the same code '" + handlerCode.value() + "'");
            }

            Set<InstrumentCode> codes = Objects.requireNonNull(h.supportedInstrumentCodes(),
                    "supportedInstrumentCodes must not be null");

            for (InstrumentCode instrumentCode : codes) {
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

                InstrumentHandler<P, ? extends Instrument> prev = map.putIfAbsent(instrumentCode, h);
                if (prev != null) {
                    throw new IllegalStateException("Market source '" + sourceCode.value() +
                            "' contains instrument code '" + instrumentCode.value() +
                            "' that is supported by multiple handlers ('" + prev.handlerCode().value() +
                            "', '" + handlerCode.value() + "')");
                }
            }
        }

        return Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("unchecked")
    protected final <I extends Instrument> InstrumentHandler<P, I> findHandlerOrThrow(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        InstrumentCode code = Objects.requireNonNull(instrument.instrumentCode(),
                "instrumentCode must not be null");

        InstrumentHandler<P, I> handler =
                (InstrumentHandler<P, I>) instrumentHandlerMap.get(code);

        if (handler == null) {
            throw new HandlerNotFoundException(code, sourceCode);
        }

        return handler;
    }
}
