package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.controller;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.dto.CurrencyListItemResponse;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.mapper.CurrencyListItemResponseMapper;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.query.list.CurrencyListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST API-адаптер use case получение списка валют.
 */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
public class CurrencyListController {

    private final CurrencyListService currencyListService;

    /**
     * Вернуть все валюты.
     */
    @GetMapping
    public ResponseEntity<List<CurrencyListItemResponse>> getAll() {

        List<CurrencyListItemResponse> currencies = currencyListService.findAll()
                .stream()
                .map(CurrencyListItemResponseMapper::toResponse)
                .toList();

        return ResponseEntity.ok(currencies);
    }
}
