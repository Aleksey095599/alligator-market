package com.alligator.market.domain.instrument.type.forex.currency;

import java.util.List;
import java.util.Optional;

/**
 * Порт хранилища валют.
 */
public interface CurrencyRepository {

    /** Сохранить или обновить валюту. */
    String save(Currency currency);

    /** Удалить валюту по коду. */
    void deleteByCode(String code);

    /** Найти валюту по коду. */
    Optional<Currency> findByCode(String code);

    /** Найти валюту по имени. */
    Optional<Currency> findByName(String name);

    /** Найти валюту по стране. */
    Optional<Currency> findByCountry(String country);

    /** Вернуть все валюты. */
    List<Currency> findAll();
}
