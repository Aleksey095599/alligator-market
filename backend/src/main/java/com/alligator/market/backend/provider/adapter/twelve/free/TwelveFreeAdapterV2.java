package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeWebConfig;
import com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.TwelveFreeFxOutrightHandler;
import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.provider.contract.InstrumentHandler;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.profile.model.ProviderAccessMethod;
import com.alligator.market.domain.provider.profile.model.ProviderDeliveryMode;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.EnumMap;
import java.util.Map;

/**
 * Адаптер для провайдера рыночных данных (далее - провайдера) TwelveData (бесплатная подписка).
 */
@Component
public class TwelveFreeAdapterV2 implements MarketDataProvider<InstrumentHandler<TwelveFreeAdapterV2>> {

    // Код провайдера
    private static final String PROVIDER_CODE = "TWELVE_FREE";

    // Карта: тип инструмента → handler
    private final Map<InstrumentType, InstrumentHandler<TwelveFreeAdapterV2>> handlers = new EnumMap<>(InstrumentType.class);

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
        // Добавляем обработчик для FX-спот
        handlers.put(
                InstrumentType.FX_OUTRIGHT,
                new TwelveFreeFxOutrightHandler(webClient, props, PROVIDER_CODE)
        );
    }

    /** Возвращает профиль провайдера. */
    @Override
    public ProviderProfile profile() {
        return new ProviderProfile(
                PROVIDER_CODE,
                "TwelveData Free Plan",
                supportedInstrumentTypes(), // ← Получаем типы инструментов из карты обработчиков
                ProviderDeliveryMode.PULL,
                ProviderAccessMethod.API_POLL,
                false,
                60_000
        );
    }

    /** Возвращает карту обработчиков инструментов. */
    @Override
    public Map<InstrumentType, InstrumentHandler<TwelveFreeAdapterV2>> instrumentHandlers() {
        return handlers;
    }
}

