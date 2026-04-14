package com.alligator.market.backend.instrument.asset.forex.reference.currency.api;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.dto.common.CurrencyDto;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.dto.mapper.CurrencyDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер для валют.
 */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyCatalogService service;

    /**
     * Вернуть все валюты.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CurrencyDto>>> getAll() {

        List<CurrencyDto> currencyDtoList = service.findAll()
                .stream()
                .map(CurrencyDtoMapper::toCurrencyDto)
                .toList();
        return ResponseEntityFactory.ok(currencyDtoList);
    }
}
