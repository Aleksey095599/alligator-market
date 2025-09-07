package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeWebConfig;
import com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.TwelveFreeFxSpotHandler;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.provider.contract.AbstractMarketDataProvider;
import com.alligator.market.domain.provider.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.AccessMethod;
import com.alligator.market.domain.provider.profile.model.DeliveryMode;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Set;

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
     * @param fxSpotRepository репозиторий инструментов FX_SPOT
     */
    public TwelveFreeAdapterV2(
            TwelveFreeConnectionProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient,
            FxSpotRepository fxSpotRepository
    ) {
        Set<FxSpot> instruments = Set.copyOf(fxSpotRepository.findAll());
        Map<InstrumentType, InstrumentHandler<TwelveFreeAdapterV2, ? extends Instrument>> handlers = Map.of(
                InstrumentType.FX_SPOT,
                new TwelveFreeFxSpotHandler(webClient, props, instruments)
        );
        super(
                handlers,
                new Profile(
                        ProfileStatus.ACTIVE,
                        PROVIDER_CODE,
                        "TwelveData Free Plan",
                        Set.of(InstrumentType.FX_SPOT),
                        DeliveryMode.PULL,
                        AccessMethod.API_POLL,
                        false,
                        60_000
                )
        );
    }

}

