package com.alligator.market.backend.instrument.forex.currency.service;

import com.alligator.market.backend.instrument.forex.currency.entity.CurrencyEntity;
import com.alligator.market.backend.instrument.forex.currency.exception.CurrencyNotFoundException;
import com.alligator.market.backend.instrument.forex.currency.exception.CurrencyUsedInPairsException;
import com.alligator.market.backend.instrument.forex.currency.exception.DuplicateCurrencyException;
import com.alligator.market.backend.instrument.forex.currency.repository.CurrencyRepository;
import com.alligator.market.backend.instrument.forex.currency_pair.repository.PairRepository;
import com.alligator.market.domain.instrument.forex.currency.Currency;
import com.alligator.market.domain.instrument.forex.currency.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация интерфейса сервиса {@link CurrencyService} для операций с валютами.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;
    private final PairRepository pairRepository;

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

        CurrencyEntity entity = new CurrencyEntity();
        entity.setCode(currency.code());
        entity.setName(currency.name());
        entity.setCountry(currency.country());
        entity.setDecimal(currency.decimal());

        CurrencyEntity saved = repository.save(entity);
        log.info("Currency {} saved with id={}", saved.getCode(), saved.getId());
        return saved.getCode();
    }

    //================
    // Обновить валюту
    //================
    @Override
    public void updateCurrency(Currency currency) {

        // Проверка наличия валюты к обновлению
        CurrencyEntity currencyEntity = repository.findByCode(currency.code())
                .orElseThrow(() -> new CurrencyNotFoundException(currency.code()));

        // Проверки, что обновление не приведет к дублированию
        repository.findByName(currency.name()).ifPresent(c -> {
            if (!c.getId().equals(currencyEntity.getId())) {
                throw new DuplicateCurrencyException("name", currency.name());
            }
        });
        repository.findByCountry(currency.country()).ifPresent(c -> {
            if (!c.getId().equals(currencyEntity.getId())) {
                throw new DuplicateCurrencyException("country", currency.country());
            }
        });

        currencyEntity.setName(currency.name());
        currencyEntity.setCountry(currency.country());
        currencyEntity.setDecimal(currency.decimal());

        repository.save(currencyEntity);
        log.info("Currency {} updated (id={})", currencyEntity.getCode(), currencyEntity.getId());
    }

    //===================================
    // Удалить валюту по уникальному коду
    //===================================
    @Override
    public void deleteCurrency(String code) {

        CurrencyEntity currencyEntity = repository.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));

        // Проверка, что валюта не используется в парах
        if (pairRepository.existsByCode1_CodeOrCode2_Code(code, code)) {
            throw new CurrencyUsedInPairsException(code);
        }

        repository.delete(currencyEntity);
        log.info("Currency {} deleted (id={})", currencyEntity.getCode(), currencyEntity.getId());
    }

    //==============================
    // Извлечь все валюты из таблицы
    //==============================
    @Override
    @Transactional(readOnly = true)
    public List<Currency> findAll() {

        // Извлекаем все валюты, преобразуя список сущностей к доменной модели валюты
        List<Currency> result = repository.findAll(Sort.by("code"))
                .stream()
                .map(c -> new Currency(
                        c.getCode(),
                        c.getName(),
                        c.getCountry(),
                        c.getDecimal()
                ))
                .toList();

        log.debug("Found {} currencies", result.size());
        return result;
    }

}
