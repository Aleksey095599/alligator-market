package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.controller;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.dto.CreateCurrencyRequest;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.mapper.CreateCurrencyRequestMapper;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.create.CreateCurrencyService;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * REST API-адаптер use case создания валюты.
 */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
public class CreateCurrencyController {

    private final CreateCurrencyService createCurrencyService;

    /**
     * Создать валюту.
     */
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateCurrencyRequest request) {
        Currency created = createCurrencyService.create(
                CreateCurrencyRequestMapper.toDomain(request)
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(created.code().value())
                .toUri();
        return ResponseEntity.created(location).body(created.code().value());
    }
}
