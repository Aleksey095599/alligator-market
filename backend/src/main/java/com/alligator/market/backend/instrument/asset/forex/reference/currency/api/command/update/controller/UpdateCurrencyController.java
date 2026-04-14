package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.update.controller;

import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.update.dto.UpdateCurrencyRequest;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.update.mapper.UpdateCurrencyRequestMapper;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.update.UpdateCurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API-адаптер use case обновления валюты.
 */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
public class UpdateCurrencyController {

    private final UpdateCurrencyService updateCurrencyService;

    /**
     * Обновить валюту по коду.
     */
    @PutMapping("/{code}")
    public ResponseEntity<Void> update(@PathVariable String code, @Valid @RequestBody UpdateCurrencyRequest request) {
        updateCurrencyService.update(UpdateCurrencyRequestMapper.toDomain(code, request));
        return ResponseEntityFactory.noContent();
    }
}
