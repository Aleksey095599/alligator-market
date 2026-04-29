package com.alligator.market.backend.provider.application.passport.catalog.port.out;

import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Map;

/**
 * Query-порт чтения проекции паспортов провайдеров (materialized view).
 *
 * <p>Назначение: предоставить данные для UI/каталога без доступа к {@code ProviderRegistry}.</p>
 */
public interface ProviderPassportQueryPort {

    /**
     * Вернуть паспорта всех активных провайдеров.
     */
    Map<ProviderCode, ProviderPassport> findAll();
}
