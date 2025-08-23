package com.alligator.market.domain.provider.sync.model;

import com.alligator.market.domain.provider.model.profile.ProviderProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель результата сопоставления профилей провайдеров рыночных данных в виде трех соответствующих списков,
 * касательно которых требуются действия с хранилищем профилей для целей синхронизации.
 */
public record ProfileContextDiff(
        List<ProviderProfile> add,
        List<Long> replaced,
        List<Long> missing
) {
    public ProfileContextDiff() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
    public void putToAddList(ProviderProfile profile) {
        add.add(profile);
    }
    public void putToReplaceList(Long id) {
        replaced.add(id);
    }
    public void putToMissingList(Long id) {
        missing.add(id);
    }
}
