package com.alligator.market.backend.fx.pairs.service;

import com.alligator.market.backend.fx.pairs.dto.PairDto;
import com.alligator.market.backend.fx.pairs.dto.PairCreateDto;
import com.alligator.market.backend.fx.pairs.dto.PairUpdateDto;
import com.alligator.market.backend.fx.pairs.entity.Pair;
import com.alligator.market.backend.fx.pairs.repository.PairRepository;
import com.alligator.market.backend.fx.pairs.service.exceptions.PairNotFoundException;
import com.alligator.market.backend.fx.pairs.service.exceptions.DuplicatePairException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/* Реализация интерфейса сервиса для валютных пар. */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PairServiceImpl implements PairService {

    private final PairRepository repository;

    //=====================
    // Создать новую пару
    //=====================
    @Override
    public String createPair(PairCreateDto dto) {

        String pair = dto.code1() + dto.code2();

        repository.findByPair(pair).ifPresent(p -> {
            throw new DuplicatePairException(pair);
        });

        Pair entity = new Pair();
        entity.setCode1(dto.code1());
        entity.setCode2(dto.code2());
        entity.setPair(pair);
        entity.setDecimal(dto.decimal());

        Pair saved = repository.save(entity);
        log.info("Pair {} saved with id={}", saved.getPair(), saved.getId());
        return saved.getPair();
    }

    //=================
    // Обновить пару
    //=================
    @Override
    public void updatePair(String pair, PairUpdateDto dto) {

        Pair entity = repository.findByPair(pair)
                .orElseThrow(() -> new PairNotFoundException(pair));

        entity.setDecimal(dto.decimal());

        repository.save(entity);
        log.info("Pair {} updated (id={})", entity.getPair(), entity.getId());
    }

    //=================
    // Удалить пару
    //=================
    @Override
    public void deletePair(String pair) {

        Pair entity = repository.findByPair(pair)
                .orElseThrow(() -> new PairNotFoundException(pair));

        repository.delete(entity);
        log.info("Pair {} deleted (id={})", entity.getPair(), entity.getId());
    }

    //====================
    // Получить все пары
    //====================
    @Override
    @Transactional(readOnly = true)
    public List<PairDto> findAll() {

        List<PairDto> result = repository.findAll(Sort.by("pair"))
                .stream()
                .map(p -> new PairDto(
                        p.getCode1(),
                        p.getCode2(),
                        p.getPair(),
                        p.getDecimal()
                ))
                .toList();
        log.debug("Found {} pairs", result.size());
        return result;
    }
}
