package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

/**
 * Проверка использования валюты конкретной фичей (данная фича ).
 *
 * <p>Каждый внешний участник, использующий валюту, предоставляет свою реализацию этой проверки и тем самым
 * вносит вклад в общий ответ: используется ли валюта где-либо в application.</p>
 */
public interface CurrencyUsageContributor {

    /**
     * Флаг, сигнализирующий об использовании валюты конкретным contributor.
     */
    boolean isUsed(CurrencyCode currencyCode);
}
