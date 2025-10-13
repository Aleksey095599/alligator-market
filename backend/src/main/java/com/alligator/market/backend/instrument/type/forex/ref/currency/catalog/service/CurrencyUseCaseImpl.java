package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.service;

import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyAlreadyExistsException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyNameDuplicateException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyUsedInFxSpotException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Реализация сервиса {@link CurrencyUseCase}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyUseCaseImpl implements CurrencyUseCase {

    private final CurrencyRepository currencyRepository;
    private final FxSpotRepository fxSpotRepository;

    @Override
    @Transactional
    public Currency create(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        // Проверяем по коду валюты (натуральный ключ), что такой валюты еще нет
        if (currencyRepository.existsByCode(currency.code())) {
            throw new CurrencyAlreadyExistsException(currency.code()); //
        }

        // Проверяем, что нет валюты с таким же названием
        if (currencyRepository.existsByName(currency.name())) {
            throw new CurrencyNameDuplicateException(currency.name());
        }

        Currency created = currencyRepository.create(currency);
        log.info("Currency {} created", created.code().value());
        return created;
    }

    @Override
    @Transactional
    public Currency update(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        // Находим валюту к обновлению
        Currency current = currencyRepository.findByCode(currency.code())
                .orElseThrow(() -> new CurrencyNotFoundException(currency.code()));

        // Если изменений нет — возвращаем текущее состояние без записи в БД
        if (current.equals(currency)) {
            log.debug("Currency {} update skipped: nothing to change", currency.code().value());
            return current;
        }

        // Если имя меняется — проверяем, что новое имя никем не занято
        if (!current.name().equals(currency.name())
                && currencyRepository.existsByName(currency.name())) {
            throw new CurrencyNameDuplicateException(currency.name());
        }

        Currency updated = currencyRepository.update(currency);
        log.info("Currency {} updated", currency.code().value());
        return updated;
    }

    @Override
    @Transactional
    public void delete(CurrencyCode code) {
        Objects.requireNonNull(code, "code must not be null");

        // Проверяем по коду валюты (натуральный ключ), что валюта с таким кодом существует
        if (!currencyRepository.existsByCode(code)) {
            throw new CurrencyNotFoundException(code);
        }

        // Проверяем, что валюта не используется инструментами FX_SPOT
        if (fxSpotRepository.existsByCurrencyCode(code)) {
            throw new CurrencyUsedInFxSpotException(code);
        }

        currencyRepository.deleteByCode(code);
        log.info("Currency {} deleted", code.value());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> findAll() {
        List<Currency> result = currencyRepository.findAll();
        log.debug("Found {} currencies", result.size());
        return result;
    }
}
