package com.alligator.market.backend.instrument.type.forex.currency.catalog.service;

import com.alligator.market.domain.instrument.type.forex.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.currency.exception.CurrencyUsedInFxSpotException;
import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.code.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.currency.repository.CurrencyRepository;
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

        // Без пред‑проверок на уникальность (устраняем TOCTOU) – уникальность проверяет адаптер репозитория
        Currency created = currencyRepository.create(currency);
        log.info("Currency {} created", created.code().value());
        return created;
    }

    @Override
    @Transactional
    public void update(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        // Находим валюту к обновлению
        Currency current = currencyRepository.findByCode(currency.code())
                .orElseThrow(() -> new CurrencyNotFoundException(currency.code()));

        // Если изменений нет – возвращаем текущее состояние без записи в БД
        if (current.equals(currency)) {
            log.debug("Currency {} update skipped: nothing to change", currency.code().value());
            return;
        }

        // Проверки целостности (валидация JPA/ограничения БД) и маппинг ошибок выполнит адаптер репозитория.
        Currency updated = currencyRepository.update(currency);
        log.info("Currency {} updated", updated.code().value());
    }

    @Override
    @Transactional
    public void delete(CurrencyCode code) {
        Objects.requireNonNull(code, "code must not be null");

        // Бизнес‑правило: валюта не должна использоваться в FX_SPOT
        if (fxSpotRepository.existsByCurrencyCode(code)) {
            throw new CurrencyUsedInFxSpotException(code);
        }

        // Случай отсутствия инструмента и прочие сбои БД определит адаптер репозитория
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
