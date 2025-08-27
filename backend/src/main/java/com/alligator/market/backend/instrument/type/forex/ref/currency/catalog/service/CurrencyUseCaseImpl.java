package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.service;

import com.alligator.market.domain.common.exception.NotFoundException;
import com.alligator.market.domain.common.exception.ResourceInUseException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyDuplicateException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса {@link CurrencyUseCase}:
 * содержит проверки и операции с валютами.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CurrencyUseCaseImpl implements CurrencyUseCase {

    private final CurrencyRepository currencyRepository;
    private final FxSpotRepository fxSpotRepository;

    @Override
    public String createCurrency(Currency currency) {
        // Проверяем, что нет валюты с таким же кодом
        currencyRepository.findByCode(currency.code()).ifPresent(c -> {
            throw new CurrencyDuplicateException("code", currency.code());
        });
        // Проверяем, что нет валюты с таким же названием
        currencyRepository.findByName(currency.name()).ifPresent(c -> {
            throw new CurrencyDuplicateException("name", currency.name());
        });
        String code = currencyRepository.save(currency);
        log.info("Currency {} created", code);
        return code;
    }

    @Override
    public void updateCurrency(Currency currency) {
        // Проверяем, что валюта с таким кодом существует
        currencyRepository.findByCode(currency.code())
                .orElseThrow(() -> new NotFoundException("Currency '%s' not found".formatted(currency.code())));
        // Проверяем, что нет валюты с таким же названием
        currencyRepository.findByName(currency.name()).ifPresent(c -> {
            if (!c.code().equals(currency.code())) {
                throw new CurrencyDuplicateException("name", currency.name());
            }
        });
        currencyRepository.save(currency);
        log.info("Currency {} updated", currency.code());
    }

    @Override
    public void deleteCurrency(String code) {
        // Проверяем, что валюта с таким кодом существует
        currencyRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency '%s' not found".formatted(code)));
        // Проверяем, что валюта не используется инструментами FX_SPOT
        if (fxSpotRepository.existsByCurrency(code)) {
            throw new ResourceInUseException("Currency '%s'".formatted(code), "FX_SPOT instrument");
        }
        currencyRepository.deleteByCode(code);
        log.info("Currency {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> getAll() {
        List<Currency> result = currencyRepository.findAll();
        log.debug("Found {} currencies", result.size());
        return result;
    }
}
