package com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.update.controller;

import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.update.dto.UpdateFxSpotRequest;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.update.mapper.UpdateFxSpotRequestMapper;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update.UpdateFxSpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API-адаптер use case обновления инструмента FOREX_SPOT.
 */
@RestController
@RequestMapping("/api/v1/fx-spot")
@RequiredArgsConstructor
public class UpdateFxSpotController {

    private final UpdateFxSpotService updateFxSpotService;

    @PatchMapping("/{instrumentCode}")
    public ResponseEntity<Void> update(@PathVariable String instrumentCode,
                                       @Valid @RequestBody UpdateFxSpotRequest request) {
        updateFxSpotService.update(UpdateFxSpotRequestMapper.toCommand(instrumentCode, request));
        return ResponseEntity.noContent().build();
    }
}
