package com.falizsh.finance.infrastructure.cache.adapter;

import com.falizsh.finance.infrastructure.cache.port.CacheProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisCacheProvider implements CacheProvider {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> T getOrFetch(String key, boolean ignoreCache, Duration ttl, Supplier<T> fetcher) {

        if (!ignoreCache) {
            try {
                @SuppressWarnings("unchecked")
                T cachedValue = (T) redisTemplate.opsForValue().get(key);

                if (cachedValue != null) {
                    log.debug("L1 Cache Hit para a chave: {}", key);
                    return cachedValue; // Retorna imediatamente
                }
            } catch (Exception e) {
                log.warn("Falha ao ler do Redis (chave: {}). Seguindo para L2/L3.", key, e);
            }
        } else {
            log.info("Bypass do Cache L1 solicitado para a chave: {}", key);
        }

        T result = fetcher.get();

        if (shouldCache(result)) {
            try {
                redisTemplate.opsForValue().set(key, result, ttl);
                log.debug("Dados salvos no Cache L1 para a chave: {}", key);
            } catch (Exception e) {
                log.warn("Falha ao salvar dados no Redis (chave: {})", key, e);
            }
        }

        return result;
    }

    private boolean shouldCache(Object result) {
        if (result == null) return false;
        if (result instanceof Collection<?> collection) {
            return !collection.isEmpty();
        }
        return true;
    }
}
