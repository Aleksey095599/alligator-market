package com.alligator.market.backend.instrument.asset.forex.reference.currency.config;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.command.create.CreateCurrencyServiceWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.command.delete.DeleteCurrencyServiceWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.command.update.UpdateCurrencyServiceWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.query.list.ListCurrenciesServiceWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.usage.contributor.CurrencyUsageContributorWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи currency.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        CreateCurrencyServiceWiringConfig.class,
        UpdateCurrencyServiceWiringConfig.class,
        DeleteCurrencyServiceWiringConfig.class,
        ListCurrenciesServiceWiringConfig.class,
        CurrencyUsageContributorWiringConfig.class
})
public class CurrencyFeatureWiringConfig {
}
