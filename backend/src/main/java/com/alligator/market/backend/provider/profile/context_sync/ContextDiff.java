package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.backend.provider.profile.service.ProviderProfileServiceImpl;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * В классе заданы списки профилей, касательно которых требуются действия с базой данных для обеспечения
 * синхронизации с профилями провайдеров извлеченными из контекста Spring
 * методом {@link ProviderContextScanner#getProviderProfiles()}.
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
