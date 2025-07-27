package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Модель содержит списки профилей, касательно которых требуются действия в базе данных для обеспечения
 * синхронизации профилей провайдеров получаемых методом {@link ProviderContextScanner#getProviderProfiles()} и
 * профилей провайдеров, содержащихся в базе данных.
 */
@Getter
@NoArgsConstructor
public class ContextDiff {

    /** Профили, которые нужно создать со статусом ACTIVE */
    private final List<ProviderProfile> addWithActiveStatus = new ArrayList<>();

    /** Профили для замены на новые */
    private final Map<ProviderProfile, Long> changeStatusToReplaced = new LinkedHashMap<>();

    /** Профили отсутствующие в контексте */
    private final Map<ProviderProfile, Long> changeStatusToMissing = new LinkedHashMap<>();

    /**
     * Конструктор инициализирует модель заранее подготовленными контейнерами.
     */
    public ContextDiff(List<ProviderProfile> addWithActiveStatus,
                       Map<ProviderProfile, Long> changeStatusToReplaced,
                       Map<ProviderProfile, Long> changeStatusToMissing) {
        this.addWithActiveStatus.addAll(addWithActiveStatus);
        this.changeStatusToReplaced.putAll(changeStatusToReplaced);
        this.changeStatusToMissing.putAll(changeStatusToMissing);
    }

    /** Добавить новый профиль со статусом ACTIVE. */
    public void putAddList(ProviderProfile profile) {
        addWithActiveStatus.add(profile);
    }

    /** Пометить профиль в БД как REPLACED. */
    public void putToReplaceMap(ProviderProfile profile, Long id) {
        changeStatusToReplaced.put(profile, id);
    }

    /** Пометить профиль в БД как MISSING. */
    public void putToMissingMap(ProviderProfile profile, Long id) {
        changeStatusToMissing.put(profile, id);
    }
}
