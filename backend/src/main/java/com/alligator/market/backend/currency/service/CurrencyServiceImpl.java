package com.alligator.market.backend.currency.service;

import com.alligator.market.backend.currency.dto.CreateCurrencyRequest;
import com.alligator.market.backend.currency.entity.Currency;
import com.alligator.market.backend.currency.repository.CurrencyRepository;
import com.alligator.market.backend.currency.service.exceptions.CurrencyNotFoundException;
import com.alligator.market.backend.currency.service.exceptions.DuplicateCurrencyException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/* Реализация интерфейса сервиса для операций с валютами. */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;

    //=====================
    // Создать новую валюту
    //=====================
    @Override
    public String createCurrency(CreateCurrencyRequest dto) {

        repository.findByCode(dto.code()).ifPresent(c -> {
            throw new DuplicateCurrencyException("code", dto.code());
        });
        repository.findByName(dto.name()).ifPresent(c -> {
            throw new DuplicateCurrencyException("name", dto.name());
        });
        repository.findByCountry(dto.country()).ifPresent(c -> {
            throw new DuplicateCurrencyException("country", dto.country());
        });

        Currency entity = new Currency();
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setCountry(dto.country());
        entity.setDecimal(dto.decimal());

        Currency saved = repository.save(entity);
        log.info("Currency {} saved with id={}", saved.getCode(), saved.getId());
        return saved.getCode();
    }

    //===================================
    // Удалить валюту по уникальному коду
    //===================================
    @Override
    public void deleteCurrency(String code) {

        Currency currency = repository.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));

        repository.delete(currency);
        log.info("Currency {} deleted (id={})", currency.getCode(), currency.getId());
    }
}
