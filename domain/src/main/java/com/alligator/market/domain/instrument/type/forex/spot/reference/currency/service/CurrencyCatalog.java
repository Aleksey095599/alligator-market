package com.alligator.market.domain.instrument.type.forex.spot.reference.currency.service;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyDuplicateException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyUsedInFxSpotException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  Каталог валют: проверки, инварианты, доменные операции.
 */
@Service
public class CurrencyCatalog {

    private final CurrencyRepository repository;
    private final FxSpotRepository fxSpotRepository;

    public CurrencyCatalog(CurrencyRepository repository, FxSpotRepository fxSpotRepository) {
        this.repository = repository;
        this.fxSpotRepository = fxSpotRepository;
    }

    /** Создать валюту. */
    public String create(Currency currency) {
        // Проверяем, что нет валюты с таким же кодом
        repository.findByCode(currency.code()).ifPresent(c -> {
            throw new CurrencyDuplicateException("code", currency.code());
        });
        // Проверяем, что нет валюты с таким же названием
        repository.findByName(currency.name()).ifPresent(c -> {
            throw new CurrencyDuplicateException("name", currency.name());
        });
        return repository.save(currency);
    }

    /** Обновить валюту. */
    public void update(Currency currency) {
        // Проверяем, что валюта с таким кодом существует
        repository.findByCode(currency.code())
                .orElseThrow(() -> new CurrencyNotFoundException(currency.code()));
        // Проверяем, что нет валюты с таким же названием
        repository.findByName(currency.name()).ifPresent(c -> {
            if (!c.code().equals(currency.code())) {
                throw new CurrencyDuplicateException("name", currency.name());
            }
        });
        repository.save(currency);
    }

    /** Удалить валюту. */
    public void delete(String code) {
        // Проверяем, что валюта с таким кодом существует
        repository.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));
        // Проверяем, что валюта не используется инструментами FX_SPOT
        if (fxSpotRepository.existsByCurrency(code)) {
            throw new CurrencyUsedInFxSpotException(code);
        }
        repository.deleteByCode(code);
    }

    /** Вернуть список всех валют. */
    public List<Currency> getAll() {
        return repository.findAll();
    }
}

