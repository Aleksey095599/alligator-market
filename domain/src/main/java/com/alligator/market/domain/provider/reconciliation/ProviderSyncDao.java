package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;

import java.util.Collection;

/**
 * Контракт DAO синхронизации паспортов провайдеров в БД.
 *
 * <p>Используется для пакетных операций UPSERT/DELETE напрямую в хранилище,
 * в обход доменного репозитория {@link ProviderPassportRepository}.</p>
 */
public interface ProviderSyncDao {

    /**
     * Пакетное удаление паспортов по их кодам.
     */
    void deleteByCodes(Collection<ProviderCode> codes);

    /**
     * Пакетная вставка или обновление (UPSERT) паспортов.
     */
    void upsertAll(Collection<ProviderPassport> passports);
}
