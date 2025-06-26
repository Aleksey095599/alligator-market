package com.alligator.market.backend.quotes.stream.scheduler;

import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.entity.CcyPairFeedSettingsEntity;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.repository.CcyPairFeedSettingsRepository;
import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.exeptions.QuoteUnavailableException;
import com.alligator.market.domain.quotes.stream.ports.QuoteFeedPort;
import com.alligator.market.domain.quotes.stream.ports.QuotePublishPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.TaskScheduler;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

//@Service
@RequiredArgsConstructor
public class PullScheduler implements ApplicationRunner {

    private final CcyPairFeedSettingsRepository ccyPairFeedSettingsRepository;
    private final Map<String, QuoteFeedPort> adapters;      // провайдер → бин‑адаптер
    private final QuotePublishPort publisher;
    private final TaskScheduler scheduler;                  // spring.task.scheduling.pool

    @Override
    public void run(ApplicationArguments args) {
        // грузим активные настройки PULL
        ccyPairFeedSettingsRepository.findAll().stream()
                .filter(CcyPairFeedSettingsEntity::isEnabled)
                .filter(s -> "PULL".equalsIgnoreCase(s.getProvider().getMode()))
                .collect(groupingBy(s -> s.getProvider().getName() + "#" + s.getFetchPeriodMs()))
                .forEach(this::scheduleGroup);
    }

    /* планируем одну task на (provider, period) */
    private void scheduleGroup(String key, List<CcyPairFeedSettingsEntity> list) {
        String provider = list.get(0).getProvider().getName();
        int periodMs = list.get(0).getFetchPeriodMs();

        QuoteFeedPort adapter = adapters.get(provider);
        List<String> pairs = list.stream().map(s -> s.getPair().getPair()).toList();

        scheduler.scheduleAtFixedRate(() -> pairs.forEach(pair -> {
            try {
                QuoteTick tick = adapter.fetchQuote(pair);
                publisher.publish(tick);
            } catch (QuoteUnavailableException e) {
                // логируем, но scheduler продолжает жить
            }
        }), Duration.ofMillis(periodMs));
    }
}
