package com.alligator.market.backend.currency.web;

import com.alligator.market.backend.currency.dto.CreateCurrencyRequest;
import com.alligator.market.backend.currency.entity.Currency;
import com.alligator.market.backend.currency.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/* REST-контроллер для операций с валютами. */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
@Slf4j
public class CurrencyController {

    private final CurrencyService service;   // внедряем бизнес-логику

    //==================
    // POST новую валюту
    //==================
    @PostMapping
    public ResponseEntity<Currency> create(@RequestBody @Valid CreateCurrencyRequest dto) {

        Currency saved = service.createCurrency(dto);
        log.debug("POST /currencies -> 201, id={}", saved.getId());
        return ResponseEntity
                .created(URI.create("/api/v1/currencies/" + saved.getId()))
                .build();
    }

}
