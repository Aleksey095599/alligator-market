package com.alligator.market.backend.currency.service;

import com.alligator.market.backend.currency.dto.CurrencyDto;
import com.alligator.market.backend.currency.dto.CurrencyUpdateDto;
import com.alligator.market.backend.currency.entity.Currency;
import com.alligator.market.backend.currency.repository.CurrencyRepository;
import com.alligator.market.backend.currency.service.exceptions.CurrencyNotFoundException;
import com.alligator.market.backend.currency.service.exceptions.DuplicateCurrencyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public String createCurrency(CurrencyDto dto) {

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

    //==================
    // Обновить валюту
    //==================
    @Override
    public void updateCurrency(String code, CurrencyUpdateDto dto) {

        Currency currency = repository.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));

        repository.findByName(dto.name()).ifPresent(c -> {
            if (!c.getId().equals(currency.getId())) {
                throw new DuplicateCurrencyException("name", dto.name());
            }
        });
        repository.findByCountry(dto.country()).ifPresent(c -> {
            if (!c.getId().equals(currency.getId())) {
                throw new DuplicateCurrencyException("country", dto.country());
            }
        });

        currency.setName(dto.name());
        currency.setCountry(dto.country());
        currency.setDecimal(dto.decimal());

        repository.save(currency);
        log.info("Currency {} updated (id={})", currency.getCode(), currency.getId());
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

    //==============================
    // Извлечь все валюты из таблицы
    //==============================
    @Override
    @Transactional(readOnly = true)
    public List<CurrencyDto> findAll() {

        List<CurrencyDto> result = repository.findAll(Sort.by("code"))
                .stream()
                .map(c -> new CurrencyDto(
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
