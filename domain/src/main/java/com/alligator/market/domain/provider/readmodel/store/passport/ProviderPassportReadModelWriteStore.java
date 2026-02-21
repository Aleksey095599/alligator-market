package com.alligator.market.domain.provider.readmodel.store.passport;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Collection;
import java.util.Map;

/**
 * Хранилище read model паспортов провайдеров (операции записи).
 *
 * <p>Это доменная абстракция: конкретная реализация может быть через JDBC/JPA/файл/кэш и т.п.</p>
 */
public interface ProviderPassportReadModelWriteStore {

    /**
     * Удалить паспорта по кодам провайдеров.
     */
    void deleteByCodes(Collection<ProviderCode> codes);

    /**
     * Сохранить паспорта (insert или update).
     */
    void upsertAll(Map<ProviderCode, ProviderPassport> passports);
}
