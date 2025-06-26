package com.alligator.market.backend.quotes.stream.providers.pull.scheduler;

import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.entity.CcyPairFeedSettingsEntity;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.repository.CcyPairFeedSettingsRepository;
import com.alligator.market.backend.quotes.stream.providers.pull.adapters.TwelvePullQuoteFeedAdapter;
import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.exeptions.QuoteUnavailableException;
import com.alligator.market.domain.quotes.stream.ports.QuotePublishPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Планировщик для загрузки котировок от {@code twelve_free_mid_pull}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TwelvePullScheduler implements ApplicationRunner {

    private static final String PROVIDER = "twelve_free_mid_pull";

    private final CcyPairFeedSettingsRepository settingsRepository;
    private final TwelvePullQuoteFeedAdapter adapter;
    private final QuotePublishPort publisher;
    private final TaskScheduler scheduler;

    @Override
    public void run(ApplicationArguments args) {
        settingsRepository.findAll().stream()
                .filter(CcyPairFeedSettingsEntity::isEnabled)
                .filter(s -> PROVIDER.equalsIgnoreCase(s.getProvider().getName()))
                .collect(Collectors.groupingBy(CcyPairFeedSettingsEntity::getFetchPeriodMs))
                .forEach(this::scheduleGroup);
    }

    /* Планируем один task на период. */
    private void scheduleGroup(Integer periodMs, List<CcyPairFeedSettingsEntity> settings) {
        List<String> pairs = settings.stream()
                .map(s -> s.getPair().getPair())
                .toList();

        scheduler.scheduleAtFixedRate(() -> pairs.forEach(pair -> {
            try {
                QuoteTick tick = adapter.fetchQuote(pair);
                publisher.publish(tick);
            } catch (QuoteUnavailableException e) {
                log.error("Cannot fetch quote", e);
            }
        }), Duration.ofMillis(periodMs));
    }

}
