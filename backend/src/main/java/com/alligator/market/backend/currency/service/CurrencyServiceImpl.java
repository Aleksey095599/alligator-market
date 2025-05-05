package com.alligator.market.backend.currency.service;

import com.alligator.market.backend.currency.dto.CreateCurrencyRequest;
import com.alligator.market.backend.currency.entity.Currency;
import com.alligator.market.backend.currency.repository.CurrencyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;

    @Override
    public Currency create(CreateCurrencyRequest dto) {

        // Проверяем уникальность кода
        repository.findByCode(dto.code()).ifPresent(c ->
        {
            throw new DuplicateCurrencyException(dto.code());
        });

        // Маппинг dto → entity (KISS - вручную)
        Currency entity = new Currency();
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setCountry(dto.country());
        entity.setDecimal(dto.decimal());

        return repository.save(entity);
    }
}
