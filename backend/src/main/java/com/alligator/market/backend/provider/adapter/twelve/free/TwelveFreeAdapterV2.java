package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeWebConfig;
import com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.TwelveFreeFxSpotHandler;
import com.alligator.market.domain.provider.contract.InstrumentHandler;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.service.ProviderCare;
import com.alligator.market.domain.provider.profile.model.AccessMethod;
import com.alligator.market.domain.provider.profile.model.DeliveryMode;
import com.alligator.market.domain.provider.profile.model.Profile;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Адаптер для провайдера рыночных данных (далее - провайдера) TwelveData (бесплатная подписка).
 */
@Component
public class TwelveFreeAdapterV2 implements MarketDataProvider {

    // Переменная с кодом провайдера
    private static final String PROVIDER_CODE = "TWELVE_FREE";

    // Список для набора обработчиков
    private final Set<InstrumentHandler> handlers = new HashSet<>();

    /** Сервис проверки обработчиков. */
    private static final ProviderCare SERVICE = new ProviderCare();

    /**
     * Конструктор адаптера TwelveFreeAdapterV2.
     *
     * @param props       конфигурационные настройки подключения {@link TwelveFreeConnectionProps}
     * @param webClient   конфигурационный класс веб-клиента {@link TwelveFreeWebConfig}
     */
    public TwelveFreeAdapterV2(
            TwelveFreeConnectionProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient
    ) {
        // Добавляем обработчики
        handlers.add(new TwelveFreeFxSpotHandler(webClient, props, PROVIDER_CODE));
    }

    /** Возвращает профиль провайдера. */
    @Override
    public Profile getProfile() {
        return new Profile(
                PROVIDER_CODE,
                "TwelveData Free Plan",
                DeliveryMode.PULL,
                AccessMethod.API_POLL,
                false,
                60_000
        );
    }

    /** Возвращает набор обработчиков (handlers) данного провайдера. */
    @Override
    public Set<InstrumentHandler> getHandlers() {
        return Collections.unmodifiableSet(handlers);
    }

    /** Проверяет корректность набора обработчиков после создания компонента. */
    @PostConstruct
    public void validateHandlers() {
        validateHandlers(SERVICE);
    }
}
