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

    /** Список профилей, которые нужно создать со статусом ACTIVE */
    private final List<ProviderProfile> add = new ArrayList<>();

    /** Профили для замены статуса на REPLACED */
    private final Map<ProviderProfile, Long> replaced = new LinkedHashMap<>();

    /** Профили для замены статуса на MISSING */
    private final Map<ProviderProfile, Long> missing = new LinkedHashMap<>();
    
    // Методы добавления в соответствующие списки
    public void putToAddList(ProviderProfile profile) {
        add.add(profile);
    }
    public void putToReplaceMap(ProviderProfile profile, Long id) {
        replaced.put(profile, id);
    }
    public void putToMissingMap(ProviderProfile profile, Long id) {
        missing.put(profile, id);
    }
}
