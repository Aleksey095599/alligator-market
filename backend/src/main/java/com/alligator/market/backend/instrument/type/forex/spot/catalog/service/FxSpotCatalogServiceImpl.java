package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.code.InstrumentCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Реализация сервиса {@link FxSpotCatalogService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FxSpotCatalogServiceImpl implements FxSpotCatalogService {

    private final FxSpotRepository fxSpotRepository;

    @Override
    @Transactional
    public FxSpot create(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Без пред‑проверок на уникальность (устраняем TOCTOU) – уникальность проверяет адаптер репозитория
        FxSpot created = fxSpotRepository.create(fxSpot);
        log.info("FX_SPOT instrument {} created", created.instrumentCode().value());
        return created;
    }

    @Override
    @Transactional
    public void update(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Ищем инструмент к обновлению
        FxSpot current = fxSpotRepository.findByCode(fxSpot.instrumentCode())
                .orElseThrow(() -> new FxSpotNotFoundException(fxSpot.instrumentCode()));

        // Если изменений нет – возвращаем текущее состояние без записи в БД
        if (current.equals(fxSpot)) {
            log.debug("FX_SPOT instrument {} update skipped: nothing to change", fxSpot.instrumentCode().value());
            return;
        }

        // Проверки целостности (валидация JPA/ограничения БД) и маппинг ошибок выполнит адаптер репозитория
        FxSpot updated = fxSpotRepository.update(fxSpot);
        log.info("FX_SPOT instrument {} updated", updated.instrumentCode().value());
    }

    @Override
    @Transactional
    public void delete(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Случай отсутствия инструмента и прочие сбои БД определит адаптер репозитория
        fxSpotRepository.deleteByCode(instrumentCode);
        log.info("FX_SPOT instrument {} deleted", instrumentCode.value());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxSpot> findAll() {

        List<FxSpot> result = fxSpotRepository.findAll();
        log.debug("Found {} FX_SPOT instruments", result.size());
        return result;
    }
}
