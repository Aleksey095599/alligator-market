package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.delete.controller;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.delete.DeleteCurrencyService;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.vo.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API-адаптер use case удаления валюты.
 */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
public class DeleteCurrencyController {

    private final DeleteCurrencyService deleteCurrencyService;

    /**
     * Удалить валюту по коду.
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        deleteCurrencyService.delete(CurrencyCode.of(code));
        return ResponseEntity.noContent().build();
    }
}
