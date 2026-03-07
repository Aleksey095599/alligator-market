package com.alligator.market.backend.instrument.asset.forex.contract.spot.catalog.service;

import com.alligator.market.domain.instrument.asset.forex.contract.spot.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.InstrumentFxSpot;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.model.vo.InstrumentCode;
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
    public InstrumentFxSpot create(InstrumentFxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Без пред‑проверок на уникальность (устраняем TOCTOU) – уникальность проверяет адаптер репозитория
        InstrumentFxSpot created = fxSpotRepository.create(fxSpot);
        log.info("FX_SPOT instrument {} created", created.instrumentCode().value());
        return created;
    }

    @Override
    @Transactional
    public void update(InstrumentFxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Ищем инструмент к обновлению
        InstrumentFxSpot current = fxSpotRepository.findByCode(fxSpot.instrumentCode())
                .orElseThrow(() -> new FxSpotNotFoundException(fxSpot.instrumentCode()));

        // Если изменений нет – возвращаем текущее состояние без записи в БД
        if (current.equals(fxSpot)) {
            log.debug("FX_SPOT instrument {} update skipped: nothing to change", fxSpot.instrumentCode().value());
            return;
        }

        // Проверки целостности (валидация JPA/ограничения БД) и маппинг ошибок выполнит адаптер репозитория
        InstrumentFxSpot updated = fxSpotRepository.update(fxSpot);
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
    public List<InstrumentFxSpot> findAll() {

        List<InstrumentFxSpot> result = fxSpotRepository.findAll();
        log.debug("Found {} FX_SPOT instruments", result.size());
        return result;
    }
}
