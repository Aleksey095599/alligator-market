package com.alligator.market.backend.quote.streaming;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.quote.feed.InstrumentProviderResolver;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * Сервис-оркестратор: собирает вместе в единую логическую цепочку все необходимые методы/сервисы для получения
 * потока котировок заданного инструмента.
 */
@Service
public class QuoteStreamingOrchestrator {

    /* Сервис, который разрешает соответствие «финансовый инструмент → провайдер рыночных данных». */
    private final InstrumentProviderResolver instrumentProviderResolver;

    /* Реестр провайдеров рыночных данных. */
    private final Map<String, MarketDataProvider> providersByCode; // <-- Spring по умолчанию назначит имя бина
                                                                   //     MarketDataProvider в качестве индекса

    /**
     * Конструктор.
     */
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
     *     <li>Определяем код провайдера через {@link InstrumentProviderResolver};</li>
     *     <li>Находим бин провайдера по этому коду;</li>
     *     <li>Делегируем построение потока самому провайдеру.</li>
     * </ol>
     *
     * <p>Подписка на поток и отправка в Kafka будут реализованы отдельно.</p>
     */
    public Publisher<QuoteTick> buildQuoteStream(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        // 1. Определяем, какой провайдер назначен для инструмента.
        ProviderCode providerCode = instrumentProviderResolver.resolveProvider(instrument);

        // 2. Находим бин провайдера по его коду.
        MarketDataProvider provider = providersByCode.get(providerCode.value());
        if (provider == null) {
            throw new IllegalStateException(
                    "No MarketDataProvider bean found for code: " + providerCode.value()
            );
        }

        // 3. Строим поток котировок (Flux/Mono) для данного инструмента.
        return provider.quote(instrument);
    }
}
