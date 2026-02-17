package com.alligator.market.domain.provider.maintenance.projection.db.passport.service;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.dao.ProviderPassportDbProjectionDao;
import com.alligator.market.domain.provider.maintenance.scanner.context.ProviderContextScanner;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;

import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Сервис проекции паспортов провайдеров из контекста приложения в БД.
 *
 * <p><b>Логика</b></p>
 * <ul>
 *     <li>Источник истины — контекст приложения.</li>
 *     <li>{@link ProviderContextScanner} извлекает из контекста приложения паспорта провайдеров, индексированные
 *     по коду провайдера.</li>
 *     <li>{@link ProviderPassportRepository} извлекает коды провайдеров, для которых в БД есть паспорта.</li>
 *     <li>{@link ProviderPassportDbProjectionDao} пакетно удаляет из БД паспорта, соответствующие устаревшим
 *     кодам провайдеров.</li>
 *     <li>{@link ProviderPassportDbProjectionDao} осуществляет пакетный UPSERT паспортов из контекста.</li>
 * </ul>
 *
 * <p><b>Преимущества применения DAO:</b></p>
 * <ul>
 *     <li>Минимум обращений к БД → меньше сетевых задержек и нагрузка на пул соединений.</li>
 *     <li>Прямой UPSERT/DELETE без ORM-накладных расходов → быстрее и проще предсказать нагрузку.</li>
 *     <li>Стабильная атомарность операции → все изменения выполняются единым набором пакетных команд.</li>
 *     <li>Единый контракт DAO → легче поддерживать и масштабировать процесс.</li>
 * </ul>
 */
@SuppressWarnings("ClassCanBeRecord")
public class ProviderPassportDbProjection {

    /* Сканер контекста. */
    private final ProviderContextScanner contextScanner;

    /* Репозиторий паспортов. */
    private final ProviderPassportRepository repository;

    /* DAO для прямых пакетных операций с паспортами. */
    private final ProviderPassportDbProjectionDao projectionDao;

    /* Конструктор. */
    public ProviderPassportDbProjection(ProviderContextScanner contextScanner,
                                        ProviderPassportRepository repository,
                                        ProviderPassportDbProjectionDao projectionDao) {
        Objects.requireNonNull(contextScanner, "contextScanner must not be null");
        Objects.requireNonNull(repository, "repository must not be null");
        Objects.requireNonNull(projectionDao, "projectionDao must not be null");

        this.contextScanner = contextScanner;
        this.repository = repository;
        this.projectionDao = projectionDao;
    }

    /**
     * Обновить проекцию (одна атомарная транзакция).
     */
    public void refresh() {
        // 1) Извлекаем мапу с паспортами из контекста (индексация по кодам провайдеров)
        Map<ProviderCode, ProviderPassport> ctxPassports = contextScanner.passportsByCode();
        // 2) Извлекаем коды провайдеров для хранящихся в БД паспортов
        Set<ProviderCode> dbProviderCodes = new LinkedHashSet<>(repository.findAllCodes());

        // Если в контексте пусто — чистим таблицу и выходим
        if (ctxPassports.isEmpty()) {
            if (!dbProviderCodes.isEmpty()) {
                projectionDao.deleteByCodes(dbProviderCodes);
            }
            return;
        }

        // 3) Вычисляем устаревшие коды (есть в БД, нет в контексте) и удаляем связанные паспорта
        Set<ProviderCode> obsolete = new LinkedHashSet<>(dbProviderCodes);
        obsolete.removeAll(ctxPassports.keySet());
        if (!obsolete.isEmpty()) {
            projectionDao.deleteByCodes(obsolete);
        }

        // 4) UPSERT всех паспортов из контекста (новые/изменившиеся обновятся, актуальные — без изменений)
        projectionDao.upsertAll(ctxPassports);
        // Готово
    }
}
