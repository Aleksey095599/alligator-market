package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

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

        // Без пред‑проверок на уникальность (устраняем TOCTOU) — уникальные ключи распознаёт адаптер
        FxSpot created = fxSpotRepository.create(fxSpot);
        log.info("FX_SPOT instrument {} created", created.instrumentCode());
        return created;
    }

    @Override
    @Transactional
    public void update(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Ищем инструмент к обновлению
        FxSpot current = fxSpotRepository.findByCode(fxSpot.instrumentCode())
                .orElseThrow(() -> new FxSpotNotFoundException(fxSpot.instrumentCode()));

        // Если изменений нет — возвращаем текущее состояние без записи в БД
        if (current.equals(fxSpot)) {
            log.debug("FX_SPOT instrument {} update skipped: nothing to change", fxSpot.instrumentCode());
            return;
        }

        // Проверки целостности (валидация JPA/ограничения БД) и маппинг ошибок выполнит адаптер.
        // Бизнес-идентичность (instrumentCode) при update не меняется,
        // в случае сбоев адаптер бросит FxSpotUpdateException.
        FxSpot updated = fxSpotRepository.update(fxSpot);
        log.info("FX_SPOT instrument {} updated", updated.instrumentCode());
    }

    @Override
    @Transactional
    public void delete(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Отсутствие инструмента определит адаптер (FxSpotNotFoundException).
        // Прочие БД-сбои — FxSpotDeleteException.
        fxSpotRepository.deleteByCode(instrumentCode);
        log.info("FX_SPOT instrument {} deleted", instrumentCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxSpot> findAll() {

        List<FxSpot> result = fxSpotRepository.findAll();
        log.debug("Found {} FX_SPOT instruments", result.size());
        return result;
    }
}
