package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.backend.provider.adapter.twelve.free.handler.TwelveFreeCurrencyPairHandler;
import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.provider.model.InstrumentHandler;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.profile.AccessMethod;
import com.alligator.market.domain.provider.profile.DeliveryMode;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Адаптер для провайдера TwelveData (free).
 */
@Component
public class TwelveFreeAdapterV2 implements MarketDataProvider {

    // Код провайдера
    private static final String PROVIDER_CODE = "TWELVE_FREE";

    // Карта соответствия: тип инструмента → handler
    private final Map<InstrumentType, InstrumentHandler> handlerMap = new EnumMap<>(InstrumentType.class);

    /** Конструктор адаптера TwelveFreeAdapterV2. */
    public TwelveFreeAdapterV2(
            TwelveFreeConnectionProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient // Инъекция нужного веб-клиента
    ) {
        handlerMap.put(
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
                Set.of(InstrumentType.CURRENCY_PAIR),
                DeliveryMode.PULL,
                AccessMethod.API_POLL,
                false,
                60_000
        );
    }

    /** Возвращает карту обработчиков инструментов. */
    @Override
    public Map<InstrumentType, InstrumentHandler> instrumentHandlers() {
        return handlerMap;
    }
}

