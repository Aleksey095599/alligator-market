package com.alligator.market.backend.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.AbstractMarketDataProvider;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * Spring-адаптер каркаса провайдера с проверкой уникальности имени бина, равной коду провайдера.
 */
public abstract class SpringMarketDataProvider<P extends MarketDataProvider>
        extends AbstractMarketDataProvider<P>
        implements BeanNameAware, InitializingBean {

    /* Имя бина, которое назначил Spring. */
    private String beanName;

    /** Конструктор. */
    protected SpringMarketDataProvider(
            String providerCode,
            ProviderDescriptor descriptor,
            ProviderPolicy policy,
            ProviderSettings settings,
            Set<? extends AbstractInstrumentHandler<P, ? extends Instrument>> handlers
    ) {
        super(providerCode, descriptor, policy, settings, handlers);
    }

    @Override
    public void setBeanName(@NonNull @NotBlank String name) {
        // Сохраняем имя бина до хука afterPropertiesSet
        final var validatedBeanName = Objects.requireNonNull(name, "beanName must not be null");

        if (validatedBeanName.isBlank()) {
            throw new IllegalArgumentException("Bean name must not be blank");
        }

        this.beanName = validatedBeanName;
    }

    @Override
    public void afterPropertiesSet() {
        // Правило уникальности: имя бина должно совпадать с providerCode
        if (!beanName.equals(providerCode())) {
            throw new IllegalStateException(
                    "Bean name must equal providerCode (name='" + beanName
                            + "', providerCode='" + providerCode() + "')"
            );
        }
    }
}
