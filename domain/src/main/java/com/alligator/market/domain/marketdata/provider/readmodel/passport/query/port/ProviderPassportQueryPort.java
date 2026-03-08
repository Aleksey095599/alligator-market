package com.alligator.market.domain.marketdata.provider.readmodel.passport.query.port;

import com.alligator.market.domain.marketdata.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.marketdata.provider.model.vo.ProviderCode;

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
