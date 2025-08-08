package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeWebConfig;
import com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.TwelveFreeCurrencyPairHandler;
import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.provider.model.InstrumentHandler;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.profile.model.AccessMethod;
import com.alligator.market.domain.provider.profile.model.DeliveryMode;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.EnumMap;
import java.util.Map;

/**
 * Адаптер для провайдера TwelveData (free).
 */
@Component
public class TwelveFreeAdapterV2 implements MarketDataProvider {

    // Код провайдера
    private static final String PROVIDER_CODE = "TWELVE_FREE";

    // Карта: тип инструмента → handler
    private final Map<InstrumentType, InstrumentHandler> handlers = new EnumMap<>(InstrumentType.class);

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
        // Добавляем обработчик для валютных пар
        handlers.put(
                InstrumentType.CURRENCY_PAIR,
                new TwelveFreeCurrencyPairHandler(webClient, props, PROVIDER_CODE)
        );
    }

    /** Возвращает профиль провайдера. */
    @Override
    public ProviderProfile profile() {
        return new ProviderProfile(
                PROVIDER_CODE,
                "TwelveData (free)",
                supportedInstrumentTypes(), // ← Получаем типы инструментов из карты обработчиков
                DeliveryMode.PULL,
                AccessMethod.API_POLL,
                false,
                60_000
        );
    }

    /** Возвращает карту обработчиков инструментов. */
    @Override
    public Map<InstrumentType, InstrumentHandler> instrumentHandlers() {
        return handlers;
    }
}

