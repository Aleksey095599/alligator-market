package com.alligator.market.backend.provider.adapter.common;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.AbstractMarketDataProvider;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;
import java.util.Set;

/**
 * Базовый Spring-класс для провайдеров рыночных данных.
 *
 * <p>Обеспечивает интеграцию с жизненным циклом Spring и строгое соответствие
 * имени бина коду провайдера (bean name == provider code).</p>
 */
public abstract class SpringMarketDataProvider<P extends MarketDataProvider>
        extends AbstractMarketDataProvider<P>
        implements BeanNameAware, InitializingBean {

    /* Имя бина, которое назначил Spring. */
    private String beanName;

    /**
     * Конструктор.
     */
    protected SpringMarketDataProvider(
            ProviderCode providerCode,
            ProviderPassport passport,
            ProviderPolicy policy,
            Set<? extends AbstractInstrumentHandler<P, ? extends Instrument>> handlers
    ) {
        super(providerCode, passport, policy, handlers);
    }

    /**
     * Получает имя бина, назначенное Spring, и сохраняет его для последующей проверки.
     *
     * @param name имя бина, назначенное Spring контейнером
     * @throws NullPointerException если {@code name == null}
     * @throws IllegalArgumentException если {@code name} пустое или состоит только из пробелов
     */
    @Override
    public void setBeanName(@org.springframework.lang.NonNull @NotBlank String name) {
        // Сохраняем имя бина до хука afterPropertiesSet
        final String validatedBeanName = Objects.requireNonNull(name, "beanName must not be null");

        if (validatedBeanName.isBlank()) {
            throw new IllegalArgumentException("Bean name must not be blank");
        }

        this.beanName = validatedBeanName;
    }

    /**
     * Проверяет соглашение: {@code beanName == providerCode()}.
     *
     * @throws IllegalStateException если имя бина не совпадает с кодом провайдера
     */
    @Override
    public void afterPropertiesSet() {
        if (!beanName.equals(providerCode().value())) {
            throw new IllegalStateException(
                    "Bean name must equal providerCode (name='" + beanName + "', providerCode='" +
                            providerCode().value() + "')"
            );
        }
    }
}
