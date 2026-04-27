package it.gromov.chatfilter.filter;

import it.gromov.chatfilter.config.section.AntiAdsSection;
import it.gromov.chatfilter.filter.matcher.CompiledMatcher;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AdFilterService {
    private final AntiAdsSection config;
    private final List<CompiledMatcher> matchers;

    public AdFilterService(AntiAdsSection config) {
        this.config = config;
        this.matchers = config.patterns.stream().map(CompiledMatcher::new).collect(Collectors.toList());
    }

    public FilterResult check(String message) {
        if (!config.enabled) {
            return FilterResult.CLEAN;
        }

        String lower = message.toLowerCase(Locale.ROOT);
        for (String safe : config.whitelist) {
            if (lower.contains(safe.toLowerCase(Locale.ROOT))) {
                return FilterResult.CLEAN;
            }
        }

        for (String blockedWord : config.blockedWords) {
            if (lower.contains(blockedWord.toLowerCase(Locale.ROOT))) {
                return new FilterResult(true, "blocked-word:" + blockedWord);
            }
        }

        for (CompiledMatcher matcher : matchers) {
            if (matcher.matches(message)) {
                return new FilterResult(true, "pattern:" + matcher.getSource());
            }
        }

        return FilterResult.CLEAN;
    }
}
