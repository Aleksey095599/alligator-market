package com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.delete.controller;

import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.delete.DeleteFxSpotService;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API-адаптер use case удаления инструмента FOREX_SPOT.
 */
@RestController
@RequestMapping("/api/v1/fx-spot")
@RequiredArgsConstructor
public class DeleteFxSpotController {

    private final DeleteFxSpotService deleteFxSpotService;

    @DeleteMapping("/{instrumentCode}")
    public ResponseEntity<Void> delete(@PathVariable String instrumentCode) {
        deleteFxSpotService.delete(InstrumentCode.of(instrumentCode));
        return ResponseEntityFactory.noContent();
    }
}
