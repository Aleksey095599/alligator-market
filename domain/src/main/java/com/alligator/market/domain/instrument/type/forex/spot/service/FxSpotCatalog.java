package com.alligator.market.domain.instrument.type.forex.spot.service;

import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotDuplicateException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotSameCurrenciesException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.reference.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  Каталог инструментов FX_SPOT: проверки, инварианты, доменные операции.
 */
@Service
public class FxSpotCatalog {

    private final FxSpotRepository repository;
    private final CurrencyRepository currencyRepository;

    public FxSpotCatalog(FxSpotRepository repository, CurrencyRepository currencyRepository) {
        this.repository = repository;
        this.currencyRepository = currencyRepository;
    }

    /** Создать инструмент. */
    public String create(FxSpot instrument) {
        // Проверяем, что базовая и котируемая валюты разные
        if (instrument.baseCurrency().equals(instrument.quoteCurrency())) {
            throw new FxSpotSameCurrenciesException();
        }
        // Проверяем, что базовая валюта существует
        currencyRepository.findByCode(instrument.baseCurrency())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(instrument.baseCurrency()));
        // Проверяем, что котируемая валюта существует
        currencyRepository.findByCode(instrument.quoteCurrency())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(instrument.quoteCurrency()));
        // Проверяем, что инструмента с таким кодом нет
        repository.find(instrument.getCode()).ifPresent(i -> {
            throw new FxSpotDuplicateException(instrument.getCode());
        });
        repository.save(instrument);
        return instrument.getCode();
    }

    /** Получить инструмент по коду. */
    public FxSpot get(String code) {
        return repository.find(code)
                .orElseThrow(() -> new FxSpotNotFoundException(code));
    }

    /** Обновить точность котировки. */
    public void updateQuoteDecimal(String code, int quoteDecimal) {
        // Проверяем, что инструмент существует
        FxSpot current = get(code);
        FxSpot updated = new FxSpot(
                current.baseCurrency(),
                current.quoteCurrency(),
                current.valueDateCode(),
                quoteDecimal
        );
        repository.save(updated);
    }

    /** Удалить инструмент по коду. */
    public void delete(String code) {
        // Проверяем, что инструмент существует
        get(code);
        repository.delete(code);
    }

    /** Вернуть список всех инструментов. */
    public List<FxSpot> getAll() {
        return repository.findAll();
    }
}
