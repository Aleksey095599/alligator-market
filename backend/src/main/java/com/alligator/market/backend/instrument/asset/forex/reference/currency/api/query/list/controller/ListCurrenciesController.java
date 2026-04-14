package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.controller;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.dto.CurrencyListResponse;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.mapper.CurrencyListResponseMapper;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.query.list.ListCurrenciesService;
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
public class ListCurrenciesController {

    private final ListCurrenciesService listCurrenciesService;

    /**
     * Вернуть все валюты.
     */
    @GetMapping
    public ResponseEntity<List<CurrencyListResponse>> getAll() {

        List<CurrencyListResponse> currencies = listCurrenciesService.findAll()
                .stream()
                .map(CurrencyListResponseMapper::toResponse)
                .toList();

        return ResponseEntity.ok(currencies);
    }
}
