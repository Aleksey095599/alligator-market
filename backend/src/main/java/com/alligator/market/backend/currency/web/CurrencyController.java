package com.alligator.market.backend.currency.web;

import com.alligator.market.backend.currency.dto.CreateCurrencyRequest;
import com.alligator.market.backend.currency.entity.Currency;
import com.alligator.market.backend.currency.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/* REST-контроллер для операций с валютами. */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
@Slf4j
public class CurrencyController {

    private final CurrencyService service; // внедряем бизнес-логику

    //==================
    // POST новую валюту
    //==================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid CreateCurrencyRequest dto) {

        Currency created = service.createCurrency(dto);
        log.debug("POST /currencies -> 201, id={}", created.getId());
    }

}