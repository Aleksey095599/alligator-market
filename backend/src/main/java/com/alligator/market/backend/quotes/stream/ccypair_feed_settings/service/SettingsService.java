package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.service;

import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto.SettingsCreateDto;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto.SettingsDto;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto.SettingsUpdateDto;

import java.util.List;

/**
 * Интерфейс сервиса для операций с соответсвующее таблицей.
 */
public interface SettingsService {

    String createConfig(SettingsCreateDto dto);

    void updateConfig(String pair, String provider, SettingsUpdateDto dto);

    void deleteConfig(String pair, String provider);

    List<SettingsDto> findAll();

}
