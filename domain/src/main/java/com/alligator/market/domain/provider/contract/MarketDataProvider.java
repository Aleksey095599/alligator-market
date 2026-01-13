package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Провайдер рыночных данных.
 */
public sealed interface MarketDataProvider permits AbstractMarketDataProvider {

    /**
     * Код провайдера.
     */
    ProviderCode providerCode();

    /**
     * Паспорт провайдера.
     */
    ProviderPassport passport();

    /**
     * Политика провайдера.
     */
    ProviderPolicy policy();

    /**
     * Настройки провайдера.
     */
    @SuppressWarnings("unused")
    ProviderSettings settings(); // <-- Точка роста, без реализации (заглушка)

    /**
     * Иммутабельный набор кодов поддерживаемых инструментов.
     */
    @SuppressWarnings("unused")
    Set<InstrumentCode> instrumentsCodes();

    /**
     * Иммутабельный набор типов поддерживаемых инструментов.
     */
    @SuppressWarnings("unused")
    Set<InstrumentType> instrumentsTypes();

    /**
     * Унифицированная операция получения котировок для финансового инструмента.
     */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
