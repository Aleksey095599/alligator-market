package com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.create.controller;

import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.create.dto.CreateFxSpotRequest;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.create.mapper.CreateFxSpotRequestMapper;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.create.CreateFxSpotService;
import com.alligator.market.domain.instrument.catalog.forex.fxspot.FxSpot;
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
 * REST API-адаптер use case создания инструмента FOREX_SPOT.
 */
@RestController
@RequestMapping("/api/v1/fx-spot")
@RequiredArgsConstructor
public class CreateFxSpotController {

    private final CreateFxSpotService createFxSpotService;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateFxSpotRequest request) {
        FxSpot created = createFxSpotService.create(
                CreateFxSpotRequestMapper.toCommand(request)
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{instrumentCode}")
                .buildAndExpand(created.instrumentCode().value())
                .toUri();

        return ResponseEntity.created(location).body(created.instrumentCode().value());
    }
}
