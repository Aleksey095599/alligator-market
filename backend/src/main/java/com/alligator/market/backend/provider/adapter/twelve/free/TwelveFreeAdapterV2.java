package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeWebConfig;
import com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.TwelveFreeFxSpotHandler;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.AbstractMarketDataProvider;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.AccessMethod;
import com.alligator.market.domain.provider.profile.model.DeliveryMode;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Адаптер для провайдера рыночных данных TwelveData (бесплатная подписка).
 */
@Component
public class TwelveFreeAdapterV2 extends AbstractMarketDataProvider {

    // Переменная с кодом провайдера
    private static final String PROVIDER_CODE = "TWELVE_FREE";

    /**
     * Конструктор адаптера TwelveFreeAdapterV2.
     *
     * @param props     конфигурационные настройки подключения {@link TwelveFreeConnectionProps}
     * @param webClient конфигурационный класс веб-клиента {@link TwelveFreeWebConfig}
     */
    public TwelveFreeAdapterV2(
            TwelveFreeConnectionProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient
    ) {
        super(new HashSet<>()); // Передаём набор обработчиков в базовый класс
        // Добавляем обработчики
        handlers.add(new TwelveFreeFxSpotHandler(webClient, props, PROVIDER_CODE));
    }

    /** Возвращает профиль провайдера. */
    @Override
    public Profile getProfile() {
        Set<InstrumentType> instruments = handlers.stream()
                .map(InstrumentHandler::getSupportedInstrumentType)
                .collect(Collectors.toSet());
        return new Profile(
                ProfileStatus.ACTIVE,
                PROVIDER_CODE,
                "TwelveData Free Plan",
                instruments,
                DeliveryMode.PULL,
                AccessMethod.API_POLL,
                false,
                60_000
        );
    }

}

