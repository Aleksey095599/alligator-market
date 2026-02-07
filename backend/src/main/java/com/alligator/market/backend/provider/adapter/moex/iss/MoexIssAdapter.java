package com.alligator.market.backend.provider.adapter.moex.iss;

import com.alligator.market.backend.provider.adapter.moex.iss.handler.instrument.forex.spot.MoexIssFxSpotHandler;
import com.alligator.market.backend.provider.adapter.common.SpringMarketDataProvider;
import com.alligator.market.backend.provider.adapter.moex.iss.properties.MoexIssConnectionProperties;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.model.passport.AccessMethod;
import com.alligator.market.domain.provider.model.passport.DeliveryMode;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.policy.ProviderPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

/* TODO: есть такая проблема с обработчиками, нужно разобраться:
Меня немного смущает что переданные в конструктор
        MoexIssConnectionProperties connectionProps,
        @Qualifier("moexIssWebClient") WebClient webClient
        как бы насквозь переходят а обработчик: Set.of(new MoexIssFxSpotHandler(connectionProps, webClient))
        А что если у меня будет несколько обработчиков для которых нужны разные connectionProps и webClient. Конечно же, можно на входе конструктора передать несколько вариантов connectionProps и webClient, но мне не кажется это хорошим решением.
*/
/**
 * Адаптер провайдера рыночных данных MOEX ISS.
 */
@Component("MOEX_ISS")
public class MoexIssAdapter extends SpringMarketDataProvider<MoexIssAdapter> {

    /* Код провайдера. */
    public static final ProviderCode PROVIDER_CODE = ProviderCode.of("MOEX_ISS");

    /* Отображаемое имя провайдера. */
    private static final String DISPLAY_NAME = "MOEX Informational & Statistical Server";

    /* Статический паспорт провайдера. */
    private static final ProviderPassport PASSPORT = new ProviderPassport(
            DISPLAY_NAME,
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false
    );

    /* Политика провайдера. */
    private static final ProviderPolicy POLICY = ProviderPolicy.ofSeconds(1); // <-- интервал запросов 1 сек

    /**
     * Конструктор адаптера MOEX ISS.
     *
     * <p>Входные параметры {@code connectionProps} и {@code webClient} передаются конструктору
     * для сборки обработчика {@link MoexIssFxSpotHandler}.</p>
     *
     * @param connectionProps параметры подключения к провайдеру {@see MoexIssConnectionProperties}
     * @param webClient web-клиент, настроенный для данного провайдера {@see MoexIssWebConfig}
     */
    public MoexIssAdapter(
        MoexIssConnectionProperties connectionProps,
        @Qualifier("moexIssWebClient") WebClient webClient
    ) {
        // Инициализируем базовый класс
        super(
                PROVIDER_CODE,
                PASSPORT,
                POLICY,
                Set.of(new MoexIssFxSpotHandler(connectionProps, webClient))
        );
    }

    @Override
    protected MoexIssAdapter self() {
        return this;
    }
}
