package com.alligator.market.backend.quote.streaming;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.contract.resolver.InstrumentProviderResolver;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * Сервис-оркестратор, который по инструменту строит поток котировок.
 */
@Service
public class QuoteStreamingOrchestrator {

    /* Резолвер соответствия инструмент → код провайдера. */
    private final InstrumentProviderResolver instrumentProviderResolver;

    /* Реестр провайдеров рыночных данных: beanName == providerCode. */
    private final Map<String, MarketDataProvider> providersByCode;

    public QuoteStreamingOrchestrator(InstrumentProviderResolver instrumentProviderResolver,
                                      Map<String, MarketDataProvider> providersByCode) {
        this.instrumentProviderResolver = instrumentProviderResolver;
        this.providersByCode = providersByCode;
    }

    /**
     * Строит поток котировок для одного инструмента.
     *
     * <p>Алгоритм:
     * <ol>
     *     <li>1) Определяем код провайдера через {@link InstrumentProviderResolver}.</li>
     *     <li>2) Находим бин провайдера по этому коду.</li>
     *     <li>3) Делегируем построение потока самому провайдеру.</li>
     * </ol>
     *
     * <p>Подписка на поток и отправка в Kafka будут реализованы отдельно.</p>
     */
    public Publisher<QuoteTick> buildQuoteStream(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        // 1. Определяем, какой провайдер назначен для инструмента.
        String providerCode = instrumentProviderResolver.resolveProvider(instrument);

        // 2. Находим бин провайдера по его коду.
        MarketDataProvider provider = providersByCode.get(providerCode);
        if (provider == null) {
            throw new IllegalStateException(
                    "No MarketDataProvider bean found for code: " + providerCode
            );
        }

        // 3. Строим поток котировок (Flux/Mono) для данного инструмента.
        return provider.quote(instrument);
    }
}
