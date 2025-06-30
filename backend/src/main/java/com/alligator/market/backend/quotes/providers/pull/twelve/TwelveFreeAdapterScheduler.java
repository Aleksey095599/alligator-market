package com.alligator.market.backend.quotes.providers.pull.twelve;

import com.alligator.market.backend.quotes.ccypair_feed_settings.entity.CcyPairFeedSettingsEntity;
import com.alligator.market.backend.quotes.ccypair_feed_settings.repository.CcyPairFeedSettingsRepository;
import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.exeptions.QuoteUnavailableException;
import com.alligator.market.domain.quotes.stream.ports.QuotePublishPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

/**
 * Планировщик для адаптера {@link TwelveFreeAdapter}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TwelveFreeAdapterScheduler implements ApplicationRunner {

    @Value("${quotes.provider.twelve.pull.name}")
    private String providerName;

    private final CcyPairFeedSettingsRepository ccyPairFeedSettingsRepository;
    private final TwelveFreeAdapter adapter;
    private final QuotePublishPort publisher;
    private final TaskScheduler scheduler;

    /*
     * Находит все активные валютные пары, которые получают поток котировок от заданного провайдера (PULL),
     * группирует их по периоду запроса котировки и создает задачи планировщика для каждой группы.
     */
    @Override
    public void run(ApplicationArguments args) {
        ccyPairFeedSettingsRepository.findActivePullSettings(providerName)
                .stream()
                .collect(groupingBy(CcyPairFeedSettingsEntity::getFetchPeriodMs))
                .forEach(this::scheduleGroup);
    }

    /*
     * Создает задачу планировщика для группы валютных пар с одинаковым периодом запроса котировок.
     */
    private void scheduleGroup(Integer periodMs, List<CcyPairFeedSettingsEntity> settings) {

        List<String> pairs = settings.stream()
                .map(s -> s.getPair().getPair())
                .toList();

        scheduler.scheduleAtFixedRate(() -> pairs.forEach(pair -> {
            try {
                QuoteTick tick = adapter.fetchQuote(pair);
                publisher.publish(tick);
            } catch (QuoteUnavailableException e) {
                log.error("Cannot fetch quote for pair {}", pair, e);
            }
        }), Duration.ofMillis(periodMs));
    }

}
