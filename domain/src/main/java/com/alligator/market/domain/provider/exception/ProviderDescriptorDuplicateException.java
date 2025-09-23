package com.alligator.market.domain.provider.exception;

/**
 * Дублирующий дескриптор провайдера.
 */
public class ProviderDescriptorDuplicateException extends RuntimeException {
    public ProviderDescriptorDuplicateException(String providerCode) {
        super("Duplicate provider descriptor detected for provider code: %s".formatted(providerCode));
    }
}
