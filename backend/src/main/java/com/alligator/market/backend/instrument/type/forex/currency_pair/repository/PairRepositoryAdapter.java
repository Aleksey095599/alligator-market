package com.alligator.market.backend.instrument.type.forex.currency_pair.repository;

import com.alligator.market.backend.instrument.type.forex.currency.entity.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.currency.repository.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.type.forex.currency_pair.entity.PairEntity;
import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPair;
import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер доменного репозитория валютных пар к Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class PairRepositoryAdapter implements CurrencyPairRepository {

    private final PairJpaRepository jpaRepository;
    private final CurrencyJpaRepository currencyJpaRepository;

    @Override
    public String save(CurrencyPair pair) {
        CurrencyEntity c1 = currencyJpaRepository.findByCode(pair.code1()).orElseThrow();
        CurrencyEntity c2 = currencyJpaRepository.findByCode(pair.code2()).orElseThrow();
        PairEntity entity = new PairEntity();
        entity.setCode1(c1);
        entity.setCode2(c2);
        entity.setPair(pair.pair());
        entity.setDecimal(pair.decimal());
        return jpaRepository.save(entity).getPair();
    }

    @Override
    public void deleteByPair(String pair) {
        jpaRepository.findByPair(pair).ifPresent(jpaRepository::delete);
    }

    @Override
    public Optional<CurrencyPair> findByPair(String pair) {
        return jpaRepository.findByPair(pair).map(this::toDomain);
    }

    @Override
    public boolean existsByCurrency(String code) {
        return jpaRepository.existsByCode1_CodeOrCode2_Code(code, code);
    }

    @Override
    public List<CurrencyPair> findAll() {
        return jpaRepository.findAll(Sort.by("pair")).stream()
                .map(this::toDomain)
                .toList();
    }

    private CurrencyPair toDomain(PairEntity entity) {
        return new CurrencyPair(
                entity.getCode1().getCode(),
                entity.getCode2().getCode(),
                entity.getPair(),
                entity.getDecimal()
        );
    }
}
