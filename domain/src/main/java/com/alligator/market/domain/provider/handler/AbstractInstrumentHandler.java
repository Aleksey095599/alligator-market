package com.alligator.market.domain.provider.handler;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.model.QuoteTick;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.handler.instrument.SupportedInstrumentsProfile;
import com.alligator.market.domain.provider.vo.HandlerCode;
import org.reactivestreams.Publisher;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Базовая реализация обработчика {@link InstrumentHandler}.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    private final HandlerCode handlerCode;

    /* Сводный профиль поддерживаемых инструментов. */
    private final SupportedInstrumentsProfile supportedInstrumentsProfile;

    /* Ссылка на провайдера (однократная потокобезопасная привязка). */
    private final AtomicReference<P> providerRef = new AtomicReference<>();

    /**
     * Конструктор: обработчик сам выводит свой профиль из набора поддерживаемых инструментов.
     * <ul>
     *     <li>Собирает сводный профиль инструментов, поддерживаемых обработчиком;</li>
     *     <li>Передает валидацию и построение профиля в {@link SupportedInstrumentsProfile};</li>
     * </ul>
     */
    protected AbstractInstrumentHandler(
            HandlerCode handlerCode,
            Class<I> instrumentClass,
            Set<? extends I> supportedInstruments
    ) {
        SupportedInstrumentsProfile profile = SupportedInstrumentsProfile.fromSupportedInstruments(
                handlerCode,
                instrumentClass,
                supportedInstruments
        );
        this.handlerCode = handlerCode;
        this.supportedInstrumentsProfile = profile;
    }

    @Override
    public final HandlerCode handlerCode() {
        return handlerCode;
    }

    @Override
    public final Set<InstrumentCode> supportedInstrumentCodes() {
        return supportedInstrumentsProfile.supportedInstrumentCodes();
    }

    @Override
    public final void attachTo(P provider) {
        Objects.requireNonNull(provider, "provider must not be null");

        if (!providerRef.compareAndSet(null, provider)) {
            throw new IllegalStateException(
                    "Provider is already attached to handler '%s'".formatted(handlerCode.value())
            );
        }
    }

    @Override
    public final Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        requireAttachedProvider();

        InstrumentCode instrumentCode = requireInstrumentCode(instrument);
        requireInstrumentMatchesSupportedProfile(instrument, instrumentCode);

        return Objects.requireNonNull(doQuote(instrument), "quote publisher must not be null");
    }

    /**
     * Точка расширения метода {@link #quote(Instrument)}:
     * Чистая логика получения потока котировок для переданного инструмента.
     */
    protected abstract Publisher<QuoteTick> doQuote(I instrument);

    /**
     * Возвращает провайдера, к которому прикреплен обработчик.
     */
    protected final P provider() {
        return requireAttachedProvider();
    }

    /*
     * Проверка соответствия инструмента сводному профилю поддерживаемых инструментов.
     */
    private void requireInstrumentMatchesSupportedProfile(I instrument, InstrumentCode instrumentCode) {
        if (!supportedInstrumentsProfile.instrumentClass().isInstance(instrument)) {
            throw new IllegalArgumentException(
                    "Instrument '%s' has java class '%s', but handler '%s' expects '%s'"
                            .formatted(
                                    instrumentCode.value(),
                                    instrument.getClass().getName(),
                                    handlerCode.value(),
                                    supportedInstrumentsProfile.instrumentClass().getName()
                            )
            );
        }

        if (instrument.asset() != supportedInstrumentsProfile.asset()) {
            throw new IllegalArgumentException(
                    "Instrument '%s' has asset '%s', but handler '%s' expects '%s'"
                            .formatted(
                                    instrumentCode.value(),
                                    instrument.asset(),
                                    handlerCode.value(),
                                    supportedInstrumentsProfile.asset()
                            )
            );
        }

        if (instrument.product() != supportedInstrumentsProfile.product()) {
            throw new IllegalArgumentException(
                    "Instrument '%s' has product '%s', but handler '%s' expects '%s'"
                            .formatted(
                                    instrumentCode.value(),
                                    instrument.product(),
                                    handlerCode.value(),
                                    supportedInstrumentsProfile.product()
                            )
            );
        }

        if (!supportedInstrumentsProfile.supportedInstrumentCodes().contains(instrumentCode)) {
            throw new IllegalArgumentException(
                    "Instrument '%s' is not supported by handler '%s'"
                            .formatted(instrumentCode.value(), handlerCode.value())
            );
        }
    }

    /*
     * Проверка, что обработчик прикреплён к провайдеру.
     */
    private P requireAttachedProvider() {
        P provider = providerRef.get();
        if (provider == null) {
            throw new IllegalStateException(
                    "Provider is not attached to handler '%s'".formatted(handlerCode.value())
            );
        }
        return provider;
    }

    /*
     * Извлекает и валидирует код инструмента.
     */
    private InstrumentCode requireInstrumentCode(I instrument) {
        return Objects.requireNonNull(instrument.instrumentCode(),
                "Instrument code is missing for handler '%s'".formatted(handlerCode.value())
        );
    }

}
