package com.alligator.market.backend.quotes.stream.settings.service;

import com.alligator.market.backend.quotes.stream.settings.dto.SettingsCreateDto;
import com.alligator.market.backend.quotes.stream.settings.dto.SettingsDto;
import com.alligator.market.backend.quotes.stream.settings.dto.SettingsUpdateDto;

import java.util.List;

/**
 * Интерфейс сервиса для операций с соответсвующее таблицей.
 */
public interface SettingsService {

    String createConfig(SettingsCreateDto dto);

    void updateConfig(String pair, String provider, String mode, SettingsUpdateDto dto);

    void deleteConfig(String pair, String provider, String mode);

    List<SettingsDto> findAll();

}
