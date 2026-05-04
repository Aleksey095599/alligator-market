package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.create;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Use-case сервис создания инструмента FOREX_SPOT.
 */
@Slf4j
public final class CreateFxSpotService {

    private final FxSpotRepository fxSpotRepository;
    private final CurrencyRepository currencyRepository;

    public CreateFxSpotService(
            FxSpotRepository fxSpotRepository,
            CurrencyRepository currencyRepository
    ) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository,
                "fxSpotRepository must not be null");
        this.currencyRepository = Objects.requireNonNull(currencyRepository,
                "currencyRepository must not be null");
    }

    public FxSpot create(CreateFxSpotCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        CurrencyCode baseCode = command.baseCurrencyCode();
        CurrencyCode quoteCode = command.quoteCurrencyCode();

        Currency base = currencyRepository.findByCode(baseCode)
                .orElseThrow(() -> new CurrencyNotFoundException(baseCode));
        Currency quote = currencyRepository.findByCode(quoteCode)
                .orElseThrow(() -> new CurrencyNotFoundException(quoteCode));

        FxSpot created = fxSpotRepository.create(new FxSpot(
                base,
                quote,
                command.tenor(),
                command.defaultQuoteFractionDigits()
        ));

        log.info("FX_SPOT instrument {} created", created.instrumentCode().value());
        return created;
    }
}
