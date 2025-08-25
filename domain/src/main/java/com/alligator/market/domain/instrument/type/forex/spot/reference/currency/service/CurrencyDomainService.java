package com.alligator.market.domain.instrument.type.forex.spot.reference.currency.service;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyDuplicateException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyUsedInFxSpotException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.storage.CurrencyStorage;
import com.alligator.market.domain.instrument.type.forex.spot.storage.FxSpotStorage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для операций с валютами.
 */
@Service
public class CurrencyDomainService {

    private final CurrencyStorage storage;
    private final FxSpotStorage fxSpotStorage;

    public CurrencyDomainService(CurrencyStorage storage, FxSpotStorage fxSpotStorage) {
        this.storage = storage;
        this.fxSpotStorage = fxSpotStorage;
    }

    /** Добавить валюту. */
    public String add(Currency currency) {
        // Проверяем уникальность кода
        storage.findByCode(currency.code()).ifPresent(c -> {
            throw new CurrencyDuplicateException("code", currency.code());
        });
        // Проверяем уникальность имени
        storage.findByName(currency.name()).ifPresent(c -> {
            throw new CurrencyDuplicateException("name", currency.name());
        });
        return storage.save(currency);
    }

    /** Получить валюту по коду. */
    public Currency get(String code) {
        return storage.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));
    }

    /** Обновить валюту. */
    public void update(Currency currency) {
        // Убеждаемся, что валюта существует
        storage.findByCode(currency.code())
                .orElseThrow(() -> new CurrencyNotFoundException(currency.code()));
        // Проверяем, что имя не занято другой валютой
        storage.findByName(currency.name()).ifPresent(c -> {
            if (!c.code().equals(currency.code())) {
                throw new CurrencyDuplicateException("name", currency.name());
            }
        });
        storage.save(currency);
    }

    /** Удалить валюту по коду. */
    public void remove(String code) {
        // Проверяем, что валюта существует
        get(code);
        // Нельзя удалять, если используется в FX_SPOT
        if (fxSpotStorage.existsByCurrency(code)) {
            throw new CurrencyUsedInFxSpotException(code);
        }
        storage.deleteByCode(code);
    }

    /** Вернуть все валюты. */
    public List<Currency> getAll() {
        return storage.findAll();
    }
}

