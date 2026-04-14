package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

/**
 * Точка расширения проверки использования валюты внешней фичей/агрегатом.
 *
 * <p>Каждая внешняя фича/агрегат, ссылающаяся на валюту, предоставляет свою реализацию этой проверки и тем самым
 * вносит вклад в общий ответ: используется ли валюта где-либо в системе.</p>
 */
public interface CurrencyUsageContributor {

    /**
     * Проверяет, используется ли валюта в рамках конкретной внешней фичи/агрегата.
     */
    boolean isUsed(CurrencyCode currencyCode);
}
