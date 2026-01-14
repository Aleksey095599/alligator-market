package com.alligator.market.domain.provider.reconciliation.db.dao;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;

import java.util.Collection;
import java.util.Map;

/**
 * DAO для прямых пакетных операций с паспортами провайдеров в БД с целью выполнения процесса синхронизации.
 */
public interface ProviderPassportSyncDao {

    /**
     * Пакетное удаление (DELETE) паспортов по их кодам.
     */
    void deleteByCodes(Collection<ProviderCode> codes);

    /**
     * Пакетная вставка или обновление (UPSERT) паспортов.
     */
    void upsertAll(Map<ProviderCode, ProviderPassport> passports);
}
