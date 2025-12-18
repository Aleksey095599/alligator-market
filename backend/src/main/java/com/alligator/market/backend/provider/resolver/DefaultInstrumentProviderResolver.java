package com.alligator.market.backend.provider.resolver;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssAdapter;
import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.provider.contract.resolver.InstrumentProviderResolver;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Реализация доменного контракта {@link InstrumentProviderResolver}, который разрешает соответствие
 * «финансовый инструмент → провайдер рыночных данных».
 *
 * <p>Временная реализация: для любых FX_SPOT инструментов --> провайдер MOEX ISS.
 * TODO: реализовать чтение правил соответствия финансовый инструмент --> провайдер из БД.
 */
@Service
public class DefaultInstrumentProviderResolver implements InstrumentProviderResolver {

    /* Код провайдера MOEX ISS. */
    private static final String MOEX_ISS_PROVIDER_CODE = MoexIssAdapter.PROVIDER_CODE;

    @Override
    public String resolveProvider(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        // Пока что вся FX_SPOT-линейка обслуживается только MOEX ISS.
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
