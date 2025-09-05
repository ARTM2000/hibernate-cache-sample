package me.artm2000.hibernatecache.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host:#{null}}")
    private String redisHost;
    @Value("${spring.data.redis.port:#{null}}")
    private String redisPort;
    @Value("${spring.data.redis.database:0}")
    private int redisDatabase;
    @Value("${spring.data.redis.password:#{null}}")
    private String redisPassword;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        return getRedissonClient(redisHost, redisPort, redisDatabase, redisPassword, false);
    }

    public static RedissonClient getRedissonClient(String host, String port, int database, String password, boolean useSsl) {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
            .setAddress((useSsl ? "rediss://" : "redis://") + host + ":" + port)
            .setDatabase(database)
            .setKeepAlive(true);

        if (!password.isBlank()) {
            serverConfig.setPassword(password);
        }
        return Redisson.create(config);
    }
}
