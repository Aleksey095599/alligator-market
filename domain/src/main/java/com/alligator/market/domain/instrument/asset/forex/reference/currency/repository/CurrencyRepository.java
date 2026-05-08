package com.alligator.market.domain.instrument.asset.forex.reference.currency.repository;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository {

    Currency create(Currency currency);

    Currency update(Currency currency);

    void deleteByCode(CurrencyCode code);

    Optional<Currency> findByCode(CurrencyCode code);

    List<Currency> findAll();
}
