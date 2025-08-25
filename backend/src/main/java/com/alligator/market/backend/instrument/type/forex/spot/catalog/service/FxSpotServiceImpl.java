package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.service.FxSpotDomainService;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация контракта сервиса {@link FxSpotService}.
 * Применяем доменный сервис, реализующий бизнес-логику работы с репозиторием инструментов FX_SPOT.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FxSpotServiceImpl implements FxSpotService {

    private final FxSpotDomainService domain;

    @Override
    public String create(FxSpot instrument) {
        String code = domain.create(instrument);
        log.info("FxSpot {} created", code);
        return code;
    }

    @Override
    public void updateQuoteDecimal(String code, int quoteDecimal) {
        domain.updateQuoteDecimal(code, quoteDecimal);
        log.info("FxSpot {} updated", code);
    }

    @Override
    public void delete(String code) {
        domain.delete(code);
        log.info("FxSpot {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxSpot> findAll() {
        List<FxSpot> result = domain.getAll();
        log.debug("Found {} FX_SPOT instruments", result.size());
        return result;
    }
}
