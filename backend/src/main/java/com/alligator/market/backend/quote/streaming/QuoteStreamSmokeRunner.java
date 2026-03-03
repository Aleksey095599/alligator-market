package com.alligator.market.backend.quote.streaming;

import com.alligator.market.domain.instrument.asset.forex.support.currency.model.Currency;
import com.alligator.market.domain.instrument.asset.forex.support.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.FxSpotTenor;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Временный smoke-тест: подписка на поток котировок для одного инструмента и получение пяти котировок.
 */
@Component
@ConditionalOnProperty(name = "quotes.smoke.enabled", havingValue = "true")
@Slf4j
public class QuoteStreamSmokeRunner {

    /* Оркестратор построения потока котировок. */
    private final QuoteStreamingOrchestrator orchestrator;

    /* Храним подписку, чтобы корректно остановить при выключении приложения. */
    private Disposable subscription;

    public QuoteStreamSmokeRunner(QuoteStreamingOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        // Строим доменную модель инструмента, поддерживаемого MOEX ISS.
        Currency cny = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);
        Currency rub = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
        FxSpot instrument = new FxSpot(cny, rub, FxSpotTenor.TOM, 4);

        subscription = Flux.from(orchestrator.buildQuoteStream(instrument))
                // Безопасность: если долго нет ни одного тика, завершаем (иначе runner может висеть бесконечно).
                .timeout(Duration.ofSeconds(30))
                // Показываем интервалы между тиками (полезно для проверки "1 сек" + время запроса).
                .elapsed()
                .take(5)
                .doOnNext(t -> log.info("Tick received after {} ms: {}", t.getT1(), t.getT2()))
                .doOnComplete(() -> log.info("Quote stream smoke test completed"))
                .doOnError(ex -> log.error("Quote stream smoke test failed", ex))
                .subscribe();
    }

    @PreDestroy
    public void shutdown() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }
}
