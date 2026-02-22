package com.alligator.market.domain.provider.readmodel.passport.store;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Хранилище паспортов провайдеров.
 *
 * <p>Примечание: Разделение на {@link Read} и {@link Write} обусловлено различной реализацией данных методов
 * и позволяет не смешивать необходимые зависимости при реализации.</p>
 */
public interface ProviderPassportStore {

    /**
     * Операции чтения (read-порты).
     */
    interface Read {
        List<ProviderCode> findAllCodes();
    }

    /**
     * Операции изменения состояния хранилища (write-порты).
     */
    interface Write {
        void deleteByCodes(Collection<ProviderCode> codes);
        void upsertAll(Map<ProviderCode, ProviderPassport> passports);
    }
}
