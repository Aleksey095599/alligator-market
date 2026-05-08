package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.delete;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyInUseException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.port.CurrencyUsageCheckPort;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public final class DeleteCurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyUsageCheckPort currencyUsageCheckPort;

    public DeleteCurrencyService(
            CurrencyRepository currencyRepository,
            CurrencyUsageCheckPort currencyUsageCheckPort
    ) {
        this.currencyRepository = Objects.requireNonNull(currencyRepository,
                "currencyRepository must not be null");
        this.currencyUsageCheckPort = Objects.requireNonNull(currencyUsageCheckPort,
                "currencyUsageCheckPort must not be null");
    }

    public void delete(CurrencyCode code) {
        Objects.requireNonNull(code, "code must not be null");

        if (currencyUsageCheckPort.isUsed(code)) {
            throw new CurrencyInUseException(code);
        }

        currencyRepository.deleteByCode(code);
        log.info("Currency {} deleted", code.value());
    }
}
