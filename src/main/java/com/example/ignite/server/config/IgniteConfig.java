package com.example.ignite.server.config;

import com.example.ignite.server.entity.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.ignite.server.constants.AppConstants.*;

@Configuration
public class IgniteConfig {

    @Bean
    public Ignite igniteInstance() {
        Ignite ignite = Ignition.start();

        // Configure the User cache
        CacheConfiguration<Long, User> userCacheConfig = new CacheConfiguration<>(USER_CACHE);
        userCacheConfig.setIndexedTypes(Long.class, User.class);
        userCacheConfig.setCacheMode(CacheMode.PARTITIONED);
        ignite.getOrCreateCache(userCacheConfig);

        return ignite;
    }
}
