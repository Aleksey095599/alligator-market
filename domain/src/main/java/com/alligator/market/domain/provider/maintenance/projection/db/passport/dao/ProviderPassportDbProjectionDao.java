package com.alligator.market.domain.provider.maintenance.projection.db.passport.dao;

/**
 * Legacy alias: write-порт read-model хранилища паспортов.
 *
 * <p>Нужен для плавной миграции. Будет удалён после переноса инфраструктуры.</p>
 */
public interface ProviderPassportDbProjectionDao extends ProviderPassportWriteStore {
}
