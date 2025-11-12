package com.gianniskoulopoulos.order_management.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
// TODO: Maybe rename to RedisCachingConfig, and have a generic CachingConfig 
public class CachingConfig {

    // Cache names
    public static final String ORDER_BY_ID_CACHE = "orderById";
    public static final String ORDER_HISTORY_CACHE = "orderHistory";
    public static final String ALL_ORDERS_CACHE = "allOrders";

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .disableCachingNullValues(); // Don't cache null values

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        cacheConfigurations.put(ORDER_BY_ID_CACHE, 
            defaultConfig.entryTtl(Duration.ofMinutes(15)));
        
        cacheConfigurations.put(ORDER_HISTORY_CACHE, 
            defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        cacheConfigurations.put(ALL_ORDERS_CACHE, 
            defaultConfig.entryTtl(Duration.ofMinutes(5)));

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .transactionAware() // Enable transaction-aware cache operations
            .build();
    }
}
