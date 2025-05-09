package com.alligator.market.backend.currency.service;

import com.alligator.market.backend.currency.dto.CreateCurrencyRequest;
import com.alligator.market.backend.currency.entity.Currency;
import com.alligator.market.backend.currency.repository.CurrencyRepository;
import com.alligator.market.backend.currency.service.exeptions.DuplicateCurrencyException;
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

    @Override
    public Currency createCurrency(CreateCurrencyRequest dto) {

        // Проверяем уникальность кода, имени и страны
        repository.findByCode(dto.code()).ifPresent(c -> {
            log.warn("Currency code '{}' already exists", dto.code());
            throw new DuplicateCurrencyException("code", dto.code());
        });
        repository.findByName(dto.name()).ifPresent(c -> {
            log.warn("Currency name '{}' already exists", dto.name());
            throw new DuplicateCurrencyException("name", dto.name());
        });
        if (repository.existsByCountry(dto.country())) {
            log.warn("Currency for country '{}' already exists", dto.country());
            throw new DuplicateCurrencyException("country", dto.country());
        }

        // Маппинг dto → entity
        Currency entity = new Currency();
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setCountry(dto.country());
        entity.setDecimal(dto.decimal());

        // Сохраняем в БД
        Currency saved = repository.save(entity);
        log.info("Currency {} saved with id={}", saved.getCode(), saved.getId());
        return saved;
    }
}
