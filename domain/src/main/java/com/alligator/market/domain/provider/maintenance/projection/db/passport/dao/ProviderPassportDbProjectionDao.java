package com.alligator.market.domain.provider.maintenance.projection.db.passport.dao;

import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;

import java.util.Collection;
import java.util.Map;

/**
 * DAO прямых пакетных операций с паспортами провайдеров в БД.
 *
 * <p>Используется процессом проекции паспортов провайдеров из контекста приложения в БД.</p>
 */
public interface ProviderPassportDbProjectionDao {

    /**
     * Пакетное удаление (DELETE) паспортов по их кодам.
     */
    void deleteByCodes(Collection<ProviderCode> codes);

    /**
     * Пакетная вставка или обновление (UPSERT) паспортов.
     */
    void upsertAll(Map<ProviderCode, ProviderPassport> passports);
}
