package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;

import java.util.Collection;

/**
 * DAO для синхронизации паспортов провайдеров в хранилище.
 *
 * <p>Предназначен для пакетных операций {@code UPSERT}/{@code DELETE} напрямую в хранилище
 * (в обход доменного репозитория {@link ProviderPassportRepository}).</p>
 */
public interface ProviderPassportSyncDao {

    /**
     * Пакетное удаление паспортов по их кодам.
     */
    void deleteByCodes(Collection<ProviderCode> codes);

    /**
     * Пакетная вставка или обновление (UPSERT) паспортов.
     */
    void upsertAll(Collection<ProviderPassport> passports);
}
