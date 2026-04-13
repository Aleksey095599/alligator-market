package com.alligator.market.backend.sourcing.plan.api.common.mapper;

import com.alligator.market.backend.sourcing.plan.api.common.dto.MarketDataSourceRequest;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.springframework.stereotype.Component;

/**
 * Маппер входной модели источника рыночных данных в доменную модель.
 */
@Component
public class MarketDataSourceRequestMapper {

    /**
     * Конвертирует входную модель источника в доменную модель.
     */
    public MarketDataSource toDomain(MarketDataSourceRequest request) {
        return new MarketDataSource(
                new ProviderCode(request.providerCode()),
                request.active(),
                request.priority()
        );
    }
}
