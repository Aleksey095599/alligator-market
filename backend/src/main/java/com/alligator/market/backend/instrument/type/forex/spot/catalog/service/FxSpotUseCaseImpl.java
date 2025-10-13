package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotAlreadyExistsException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Реализация сервиса {@link FxSpotUseCase}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FxSpotUseCaseImpl implements FxSpotUseCase {

    private final FxSpotRepository fxSpotRepository;

    @Override
    @Transactional
    public FxSpot create(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Проверяем по коду инструмента (натуральный ключ), что такого инструмента еще нет
        if (fxSpotRepository.existsByInstrumentCode(fxSpot.instrumentCode())) {
            throw new FxSpotAlreadyExistsException(fxSpot.instrumentCode());
        }

        // Сохраняем инструмент (адаптер проверит, что обе валюты существуют)
        try {
            FxSpot created = fxSpotRepository.create(fxSpot);
            log.info("FxSpot instrument {} created", created.instrumentCode());
            return created;
        } catch (CurrencyNotFoundException ex) {
            log.warn("Unable to create FX_SPOT {}: currency is missing", fxSpot.instrumentCode(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public FxSpot update(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Ищем инструмент к обновлению
        FxSpot current = fxSpotRepository.findByCode(fxSpot.instrumentCode())
                .orElseThrow(() -> new FxSpotNotFoundException(fxSpot.instrumentCode()));

        // Если изменений нет — возвращаем текущее состояние без записи в БД
        if (current.equals(fxSpot)) {
            log.debug("FxSpot {} update skipped: nothing to change", fxSpot.instrumentCode());
            return current;
        }

        FxSpot updated = fxSpotRepository.update(fxSpot);
        log.info("FxSpot instrument {} updated", updated.instrumentCode());
        return updated;
    }

    @Override
    @Transactional
    public void delete(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Проверяем, что инструмент существует
        if (!fxSpotRepository.existsByInstrumentCode(instrumentCode)) {
            throw new FxSpotNotFoundException(instrumentCode);
        }

        fxSpotRepository.deleteByCode(instrumentCode);
        log.info("FxSpot instrument {} deleted", instrumentCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxSpot> findAll() {
        List<FxSpot> result = fxSpotRepository.findAll();
        log.debug("Found {} FX_SPOT instruments", result.size());
        return result;
    }
}
