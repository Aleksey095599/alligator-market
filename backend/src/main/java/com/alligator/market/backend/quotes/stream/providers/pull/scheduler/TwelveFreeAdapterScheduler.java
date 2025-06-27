package com.alligator.market.backend.quotes.stream.providers.pull.scheduler;

import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.entity.CcyPairFeedSettingsEntity;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.repository.CcyPairFeedSettingsRepository;
import com.alligator.market.backend.quotes.stream.providers.pull.adapters.TwelveFreeAdapter;
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

    private final CcyPairFeedSettingsRepository settingsRepository;
    private final TwelveFreeAdapter adapter;
    private final QuotePublishPort publisher;
    private final TaskScheduler scheduler;

    @Override
    public void run(ApplicationArguments args) {
        settingsRepository.findActivePullSettings(providerName)
                .stream()
                .collect(groupingBy(CcyPairFeedSettingsEntity::getFetchPeriodMs))
                .forEach(this::scheduleGroup);
    }

    /* Планируем один task на период; пропускаем, если период не положительный. */
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
