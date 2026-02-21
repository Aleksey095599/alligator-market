package com.alligator.market.domain.provider.readmodel.passport.store;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Контракт хранилища паспортов провайдеров.
 *
 * <p>Примечание: Разделение на {@link Read} и {@link Write} обусловлено различной реализацией данных методов
 * и позволяет не смешивать соответствующие зависимости.</p>
 */
public interface ProviderPassportStore {

    /**
     * Операции чтения.
     */
    interface Read {
        List<ProviderCode> findAllCodes();
    }

    /**
     * Операции изменения состояния хранилища.
     */
    interface Write {
        void deleteByCodes(Collection<ProviderCode> codes);
        void upsertAll(Map<ProviderCode, ProviderPassport> passports);
    }
}
