package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;

import java.util.Collection;

/**
 * Контракт DAO синхронизации паспортов провайдеров в базе данных.
 */
public interface ProviderSyncDao {

    /**
     * Удалить записи паспортов по набору кодов провайдеров.
     */
    void deleteByCodes(Collection<ProviderCode> codes);

    /**
     * Выполнить пакетный UPSERT паспортов.
     */
    void upsertAll(Collection<ProviderPassport> passports);
}
