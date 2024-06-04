package com.geekuniverse.cac.config;

import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @description Redisson配置
 * @author 谢诗宏
 * @date 2022/12/10
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public Integer port;

    @Value("${spring.redis.password}")
    public String password;

    @Value("${spring.redis.database}")
    public Integer database;

    /**
     * 默认redisson实例
     */
    @Bean
    public RedissonClient redissonClient() throws IOException {
        Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-config.yml"));
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(StrUtil.isNotBlank(password) ? password : null)
                .setDatabase(database);
        config.setCodec(new StringCodec());
        return Redisson.create(config);
    }

}