package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.model.info.ProviderStaticInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель результата сопоставления профилей провайдеров в виде трех списков,
 * касательно которых требуются действия с репозиторием для целей синхронизации с контекстом.
 */
public record ProfileDiff(
        List<ProviderStaticInfo> add,
        List<Long> replaced,
        List<Long> missing
) {
    public ProfileDiff() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public void putToAddList(ProviderStaticInfo providerStaticInfo) {
        add.add(providerStaticInfo);
    }
    public void putToReplaceList(Long id) {
        replaced.add(id);
    }
    public void putToMissingList(Long id) {
        missing.add(id);
    }
}
