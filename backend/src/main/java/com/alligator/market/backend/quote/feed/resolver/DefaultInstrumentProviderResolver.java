package com.alligator.market.backend.quote.feed.resolver;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssProvider;
import com.alligator.market.domain.instrument.base.model.Instrument;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.FxSpot;
import com.alligator.market.domain.marketdata.provider.model.vo.ProviderCode;
import com.alligator.market.domain.quote.feed.InstrumentProviderResolver;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Реализация доменного контракта {@link InstrumentProviderResolver}, который разрешает соответствие
 * «финансовый инструмент → провайдер рыночных данных».
 *
 * <p>Временная реализация: для любых FOREX_SPOT инструментов --> провайдер MOEX ISS.
 * TODO: реализовать чтение правил соответствия финансовый инструмент --> провайдер из БД.
 */
@Service
public class DefaultInstrumentProviderResolver implements InstrumentProviderResolver {

    /* Код провайдера MOEX ISS. */
    private static final ProviderCode MOEX_ISS_PROVIDER_CODE = MoexIssProvider.PROVIDER_CODE;

    @Override
    public ProviderCode resolveProvider(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        // Пока что вся FOREX_SPOT-линейка обслуживается только MOEX ISS.
        if (instrument instanceof FxSpot) {
            return MOEX_ISS_PROVIDER_CODE;
        }

        // Для прочих типов инструментов провайдеры пока не настроены.
        throw new IllegalArgumentException(
                "No market data provider configured for instrument type: "
                        + instrument.getClass().getSimpleName()
        );
    }
}
