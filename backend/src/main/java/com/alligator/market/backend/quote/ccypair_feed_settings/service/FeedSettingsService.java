package com.alligator.market.backend.quote.ccypair_feed_settings.service;

import com.alligator.market.backend.quote.ccypair_feed_settings.dto.FeedSettingsCreateDto;
import com.alligator.market.backend.quote.ccypair_feed_settings.dto.FeedSettingsDto;
import com.alligator.market.backend.quote.ccypair_feed_settings.dto.FeedSettingsUpdateDto;

import java.util.List;

/**
 * Интерфейс сервиса для операций с соответсвующее таблицей.
 */
public interface FeedSettingsService {

    String createSettings(FeedSettingsCreateDto dto);

    void updateSettings(String pair, String provider, FeedSettingsUpdateDto dto);

    void deleteSettings(String pair, String provider);

    List<FeedSettingsDto> findAll();

}
