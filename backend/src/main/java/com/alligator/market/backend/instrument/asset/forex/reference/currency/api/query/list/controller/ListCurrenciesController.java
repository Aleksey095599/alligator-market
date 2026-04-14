package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.controller;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.dto.common.CurrencyDto;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.dto.mapper.CurrencyDtoMapper;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.query.list.ListCurrenciesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST API-адаптер query use case списка валют.
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
    public ResponseEntity<ApiResponse<List<CurrencyDto>>> getAll() {
        // Конвертируем доменные сущности в DTO ответа.
        List<CurrencyDto> currencyDtoList = listCurrenciesService.findAll()
                .stream()
                .map(CurrencyDtoMapper::toCurrencyDto)
                .toList();

        return ResponseEntityFactory.ok(currencyDtoList);
    }
}
