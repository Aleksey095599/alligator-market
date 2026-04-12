package com.alligator.market.backend.instrument.asset.forex.reference.currency.application;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.exception.CurrencyNotFoundException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.delete.DeleteCurrencyService;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Реализация сервиса {@link CurrencyCatalogService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyCatalogServiceImpl implements CurrencyCatalogService {

    private final CurrencyRepository currencyRepository;
    private final DeleteCurrencyService deleteCurrencyService;

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
        // Делегируем delete use case в выделенный command service.
        deleteCurrencyService.delete(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> findAll() {

        List<Currency> result = currencyRepository.findAll();
        log.debug("Found {} currencies", result.size());
        return result;
    }
}
