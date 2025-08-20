package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeWebConfig;
import com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.TwelveFreeFxOutrightHandler;
import com.alligator.market.domain.provider.contract.InstrumentHandler;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.profile.model.ProviderAccessMethod;
import com.alligator.market.domain.provider.profile.model.ProviderDeliveryMode;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
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

    /**
     * Конструктор адаптера TwelveFreeAdapterV2.
     * Используем конфигурационный класс веб-клиента {@link TwelveFreeWebConfig}
     * и конфигурационные настройки подключения {@link TwelveFreeConnectionProps}.
     * Добавляем обработчики для конкретных инструментов.
     */
    public TwelveFreeAdapterV2(
            TwelveFreeConnectionProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient
    ) {
        // Добавляем обработчики
        handlers.add(new TwelveFreeFxOutrightHandler(webClient, props, PROVIDER_CODE));
    }

    /** Возвращает профиль провайдера. */
    @Override
    public ProviderProfile profile() {
        return new ProviderProfile(
                PROVIDER_CODE,
                "TwelveData Free Plan",
                ProviderDeliveryMode.PULL,
                ProviderAccessMethod.API_POLL,
                false,
                60_000
        );
    }

    /** Возвращает набор обработчиков (handlers) данного провайдера. */
    @Override
    public Set<InstrumentHandler> handlers() {
        return Collections.unmodifiableSet(handlers);
    }
}

