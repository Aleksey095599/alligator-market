package com.alligator.market.domain.instrument.type.forex.spot.reference.currency.service;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyDuplicateException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyUsedInFxSpotException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotInstrumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис реализует бизнес логику работы с репозиторием валют.
 */
@Service
public class CurrencyDomainService {

    private final CurrencyRepository repository;
    private final FxSpotInstrumentRepository fxSpotRepository;

    public CurrencyDomainService(CurrencyRepository repository, FxSpotInstrumentRepository fxSpotRepository) {
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

    /** Получить валюту по коду. */
    public Currency get(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));
    }

    /** Обновить валюту. */
    public void update(Currency currency) {
        // Проверяем, что валюта существует
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
    public void remove(String code) {
        // Проверяем, что валюта существует
        get(code);
        // Проверяем, что валюта не используется инструментом FX_SPOT
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

