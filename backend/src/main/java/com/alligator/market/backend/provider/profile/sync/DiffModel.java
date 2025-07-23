package com.alligator.market.backend.provider.profile.sync;

import com.alligator.market.domain.provider.ProviderProfile;

import java.util.List;

/**
 * Утилитарная модель для удобства представления результата сверки профилей провайдеров,
 * изъятых из контекста Spring и выгруженных из базы данных.
 * <pre>
 *   toInsert  – записи, которых нет в БД → вставить
 *   toUpdate  – записи, которые есть, но содержимое изменилось → обновить
 *   toOrphan  – "осиротевшие" записи (бин адаптера исчез) → перевести в NOT_IMPLEMENTED
 * </pre>
 */
public record DiffModel(

        List<ProviderProfile> toInsert,
        List<ProviderProfile> toUpdate,
        List<ProviderProfile> toOrphan
) {

}
