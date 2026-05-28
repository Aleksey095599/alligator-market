package com.alligator.market.domain.source.handler;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.exception.InstrumentNotSupportedByHandlerException;
import com.alligator.market.domain.source.handler.instrument.SupportedInstrumentsProfile;
import com.alligator.market.domain.source.handler.passport.SourceHandlerPassport;
import com.alligator.market.domain.source.handler.policy.SourceHandlerPolicy;
import com.alligator.market.domain.source.vo.HandlerCode;
import org.reactivestreams.Publisher;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractInstrumentHandler<P extends MarketDataSource, I extends Instrument>
        implements InstrumentHandler<P, I> {

    private final HandlerCode handlerCode;
    private final SourceHandlerPassport passport;

    private final SupportedInstrumentsProfile supportedInstrumentsProfile;
    private final SourceHandlerPolicy policy;

    private final AtomicReference<P> sourceRef = new AtomicReference<>();

    protected AbstractInstrumentHandler(
            HandlerCode handlerCode,
            SourceHandlerPassport passport,
            Class<I> instrumentClass,
            Set<? extends I> supportedInstruments,
            SourceHandlerPolicy policy
    ) {
        SupportedInstrumentsProfile profile = SupportedInstrumentsProfile.fromSupportedInstruments(
                handlerCode,
                instrumentClass,
                supportedInstruments
        );
        this.handlerCode = handlerCode;
        this.passport = Objects.requireNonNull(passport, "passport must not be null");
        this.supportedInstrumentsProfile = profile;
        this.policy = Objects.requireNonNull(policy, "policy must not be null");
    }

    @Override
    public final HandlerCode handlerCode() {
        return handlerCode;
    }

    @Override
    public final SourceHandlerPassport passport() {
        return passport;
    }

    @Override
    public final Set<InstrumentCode> supportedInstrumentCodes() {
        return supportedInstrumentsProfile.supportedInstrumentCodes();
    }

    @Override
    public final SourceHandlerPolicy policy() {
        return policy;
    }

    @Override
    public final void attachTo(P source) {
        Objects.requireNonNull(source, "source must not be null");

        if (!sourceRef.compareAndSet(null, source)) {
            throw new IllegalStateException(
                    "Market data source is already attached to handler '%s'".formatted(handlerCode.value())
            );
        }
    }

    @Override
    public final Publisher<SourceTick> streamSourceTicks(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        requireAttachedSource();

        InstrumentCode instrumentCode = requireInstrumentCode(instrument);
        requireInstrumentMatchesSupportedProfile(instrument, instrumentCode);

        return Objects.requireNonNull(
                doStreamSourceTicks(instrument),
                "source tick publisher must not be null"
        );
    }

    protected abstract Publisher<SourceTick> doStreamSourceTicks(I instrument);

    protected final P source() {
        return requireAttachedSource();
    }

    private void requireInstrumentMatchesSupportedProfile(I instrument, InstrumentCode instrumentCode) {
        if (!supportedInstrumentsProfile.instrumentClass().isInstance(instrument)) {
            throw InstrumentNotSupportedByHandlerException.instrumentClassMismatch(
                    instrumentCode,
                    handlerCode,
                    supportedInstrumentsProfile.instrumentClass(),
                    instrument.getClass()
            );
        }

        if (instrument.asset() != supportedInstrumentsProfile.asset()) {
            throw InstrumentNotSupportedByHandlerException.assetMismatch(
                    instrumentCode,
                    handlerCode,
                    supportedInstrumentsProfile.asset(),
                    instrument.asset()
            );
        }

        if (instrument.product() != supportedInstrumentsProfile.product()) {
            throw InstrumentNotSupportedByHandlerException.productMismatch(
                    instrumentCode,
                    handlerCode,
                    supportedInstrumentsProfile.product(),
                    instrument.product()
            );
        }

        if (!supportedInstrumentsProfile.supportedInstrumentCodes().contains(instrumentCode)) {
            throw InstrumentNotSupportedByHandlerException.instrumentCodeNotSupported(
                    instrumentCode,
                    handlerCode,
                    supportedInstrumentsProfile.supportedInstrumentCodes()
            );
        }
    }

    private P requireAttachedSource() {
        P source = sourceRef.get();
        if (source == null) {
            throw new IllegalStateException(
                    "Market data source is not attached to handler '%s'".formatted(handlerCode.value())
            );
        }
        return source;
    }

    private InstrumentCode requireInstrumentCode(I instrument) {
        return Objects.requireNonNull(instrument.instrumentCode(),
                "Instrument code is missing for handler '%s'".formatted(handlerCode.value())
        );
    }
}
