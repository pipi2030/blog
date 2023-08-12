package com.neutech.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {
    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    @Resource
    private RedisConnectionFactory connectionFactory;
    @Resource
    private RedisConnectionFactory factory;
    @Configuration
    @EnableCaching
    public class RedisConfig {
        @Bean
        public CacheManager cacheManager() {
            //默认数据的失效时间为两个小时
            ThreadLocalRandom random = ThreadLocalRandom.current();
            RedisCacheConfiguration redisCacheConfiguration =
                    RedisCacheConfiguration.defaultCacheConfig()
                            .entryTtl(Duration.ofMinutes(120));
            return RedisCacheManager
                    .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                    .cacheDefaults(redisCacheConfiguration).build();
        }

        @Bean
        public RedisTemplate<Object, Object> redisTemplate() {
            RedisTemplate<Object, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);

            //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
            Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            serializer.setObjectMapper(mapper);

            template.setValueSerializer(serializer);
            //使用StringRedisSerializer来序列化和反序列化redis的key值
            template.setKeySerializer(new StringRedisSerializer());
            template.afterPropertiesSet();
            return template;
        }
        @Bean
        public StringRedisTemplate stringRedisTemplate() {
            StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
            stringRedisTemplate.setConnectionFactory(factory);
            return stringRedisTemplate;
        }
    }
}
