package me.artm2000.hibernatecache.common;

import me.artm2000.hibernatecache.common.config.RedisConfig;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.redisson.api.RedissonClient;
import org.redisson.hibernate.RedissonRegionFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class CustomizeRegionFactory extends RedissonRegionFactory {
    @Override
    @SuppressWarnings("unchecked")
    protected RedissonClient createRedissonClient(StandardServiceRegistry registry, Map properties) {
        loadActiveProfile(properties);

        String host = (String) properties.getOrDefault("spring.data.redis.host", "localhost");
        String port = (String) properties.getOrDefault("spring.data.redis.port", "6379");
        String databaseStr = (String) properties.getOrDefault("spring.data.redis.database", "0");
        String password = (String) properties.getOrDefault("spring.data.redis.password", "");

        return RedisConfig.getRedissonClient(host, port, Integer.parseInt(databaseStr), password, false);
    }

    @SuppressWarnings("rawtypes,unchecked")
    private void loadActiveProfile(Map properties) {
        String activeProfile = System.getProperty("spring.profiles.active");
        if (activeProfile == null || activeProfile.isBlank()) {
            activeProfile = System.getenv("SPRING_PROFILES_ACTIVE");
        }
        if (activeProfile == null) return;

        try {
            for (String property : Arrays.stream((new ClassPathResource(
                    String.format(
                        "application-%s.properties",
                        activeProfile
                    )))
                    .getContentAsString(StandardCharsets.UTF_8)
                    .split("\n"))
                .filter(line -> line.trim().contains("=") && !line.trim().startsWith("#"))
                .toList())
            {
                String[] keyValue = property.trim().split("=", 2);
                properties.put(keyValue[0], keyValue[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
