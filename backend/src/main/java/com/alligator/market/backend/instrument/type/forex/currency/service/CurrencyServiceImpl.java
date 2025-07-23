package com.alligator.market.backend.instrument.type.forex.currency.service;

import com.alligator.market.backend.instrument.type.forex.currency.exception.CurrencyNotFoundException;
import com.alligator.market.backend.instrument.type.forex.currency.exception.CurrencyUsedInPairsException;
import com.alligator.market.backend.instrument.type.forex.currency.exception.DuplicateCurrencyException;
import com.alligator.market.domain.instrument.type.forex.currency.Currency;
import com.alligator.market.backend.instrument.type.forex.currency.service.CurrencyService;
import com.alligator.market.domain.instrument.type.forex.currency.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация интерфейса сервиса {@link CurrencyService}.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;
    private final CurrencyPairRepository pairRepository;

    //=====================
    // Создать новую валюту
    //=====================
    @Override
    public String createCurrency(Currency currency) {

        repository.findByCode(currency.code()).ifPresent(c -> {
            throw new DuplicateCurrencyException("code", currency.code());
        });
        repository.findByName(currency.name()).ifPresent(c -> {
            throw new DuplicateCurrencyException("name", currency.name());
        });
        repository.findByCountry(currency.country()).ifPresent(c -> {
            throw new DuplicateCurrencyException("country", currency.country());
        });

        String code = repository.save(currency);
        log.info("Currency {} saved", code);
        return code;
    }

    //================
    // Обновить валюту
    //================
    @Override
    public void updateCurrency(Currency currency) {

        // Проверка наличия валюты к обновлению
        repository.findByCode(currency.code())
                .orElseThrow(() -> new CurrencyNotFoundException(currency.code()));

        // Проверки, что обновление не приведет к дублированию
        repository.findByName(currency.name()).ifPresent(c -> {
            if (!c.code().equals(currency.code())) {
                throw new DuplicateCurrencyException("name", currency.name());
            }
        });
        repository.findByCountry(currency.country()).ifPresent(c -> {
            if (!c.code().equals(currency.code())) {
                throw new DuplicateCurrencyException("country", currency.country());
            }
        });

        repository.save(currency);
        log.info("Currency {} updated", currency.code());
    }

    //===================================
    // Удалить валюту по уникальному коду
    //===================================
    @Override
    public void deleteCurrency(String code) {

        repository.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));

        // Проверка, что валюта не используется в парах
        if (pairRepository.existsByCurrency(code)) {
            throw new CurrencyUsedInPairsException(code);
        }

        repository.deleteByCode(code);
        log.info("Currency {} deleted", code);
    }

    //==============================
    // Извлечь все валюты из таблицы
    //==============================
    @Override
    @Transactional(readOnly = true)
    public List<Currency> findAll() {

        // Извлекаем все валюты, преобразуя список сущностей к доменной модели валюты
        List<Currency> result = repository.findAll();

        log.debug("Found {} currencies", result.size());
        return result;
    }
}
