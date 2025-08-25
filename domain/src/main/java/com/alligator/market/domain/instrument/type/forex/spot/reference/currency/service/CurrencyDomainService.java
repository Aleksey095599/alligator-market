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

    /** Добавить валюту в хранилище. */
    public String add(Currency currency) {
        // Проверяем, что в хранилище нет валюты с таким же кодом
        storage.findByCode(currency.code()).ifPresent(c -> {
            throw new CurrencyDuplicateException("code", currency.code());
        });
        // Проверяем, что в хранилище нет валюты с таким же названием
        storage.findByName(currency.name()).ifPresent(c -> {
            throw new CurrencyDuplicateException("name", currency.name());
        });
        return storage.save(currency);
    }

    /** Получить валюту из хранилища по коду. */
    public Currency get(String code) {
        return storage.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));
    }

    /** Обновить параметры валюты в хранилище. */
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

    /** Удалить валюту из хранилища по коду. */
    public void remove(String code) {
        // Проверяем, что валюта существует
        get(code);
        // Нельзя удалять, если валюта используется инструментом FX_SPOT
        if (fxSpotStorage.existsByCurrency(code)) {
            throw new CurrencyUsedInFxSpotException(code);
        }
        storage.deleteByCode(code);
    }

    /** Вернуть список всех валют из хранилища. */
    public List<Currency> getAll() {
        return storage.findAll();
    }
}

