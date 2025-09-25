package com.alligator.market.domain.provider.exception;

/**
 * Дублирующий дескриптор провайдера.
 */
public class ProviderDescriptorDuplicateException extends RuntimeException {
    public ProviderDescriptorDuplicateException(String providerCode, String displayName) {
        super("Duplicate provider descriptor detected for provider code '%s' or display name '%s'".formatted(
                providerCode,
                displayName
        ));
    }
}
