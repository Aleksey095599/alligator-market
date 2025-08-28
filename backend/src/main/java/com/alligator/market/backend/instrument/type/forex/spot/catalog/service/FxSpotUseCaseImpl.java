package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotDuplicateException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса {@link FxSpotUseCase}.
 * Содержит проверки и операции с инструментами FX_SPOT.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FxSpotUseCaseImpl implements FxSpotUseCase {

    private final FxSpotRepository fxSpotRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    public String create(FxSpot fxSpot) {
        // В модели уже указаны коды валют и дата валютирования
        String baseCode = fxSpot.base().code();
        String quoteCode = fxSpot.quote().code();
        Currency base = currencyRepository.findByCode(baseCode)
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(baseCode));
        Currency quote = currencyRepository.findByCode(quoteCode)
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(quoteCode));
        FxSpot instrument = new FxSpot(base, quote, fxSpot.valueDateCode(), fxSpot.quoteDecimal());
        // Проверяем, что инструмента с таким кодом нет
        fxSpotRepository.find(instrument.getCode()).ifPresent(i -> {
            throw new FxSpotDuplicateException(instrument.getCode());
        });
        fxSpotRepository.save(instrument);
        log.info("FxSpot {} created", instrument.getCode());
        return instrument.getCode();
    }

    @Override
    public void updateQuoteDecimal(FxSpot fxSpot) {
        // Из модели получаем код и новую точность
        String code = fxSpot.getCode();
        // Проверяем, что инструмент существует
        FxSpot current = fxSpotRepository.find(code)
                .orElseThrow(() -> new FxSpotNotFoundException(code));
        FxSpot updated = new FxSpot(
                current.base(),
                current.quote(),
                current.valueDateCode(),
                fxSpot.quoteDecimal()
        );
        fxSpotRepository.save(updated);
        log.info("FxSpot {} updated", code);
    }

    @Override
    public void delete(String code) {
        // Проверяем, что инструмент существует
        fxSpotRepository.find(code)
                .orElseThrow(() -> new FxSpotNotFoundException(code));
        fxSpotRepository.delete(code);
        log.info("FxSpot {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxSpot> findAll() {
        List<FxSpot> result = fxSpotRepository.findAll();
        log.debug("Found {} FX_SPOT instruments", result.size());
        return result;
    }
}
