package com.falizsh.finance.infrastructure.cache.port;

import java.time.Duration;
import java.util.function.Supplier;

public interface CacheProvider {
    <T> T getOrFetch(String key, boolean ignoreCache, Duration ttl, Supplier<T> fetcher);
}