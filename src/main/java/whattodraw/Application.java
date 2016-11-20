package whattodraw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import whattodraw.suggestions.Suggestion;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    @Autowired
    Environment env;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        jedisConFactory.setHostName(env.getProperty("jedis.factory.host"));
        jedisConFactory.setPort(Integer.valueOf(env.getProperty("jedis.factory.port")));
        return jedisConFactory;
    }

    @Bean
    StringRedisSerializer stringRedisSerializer(){
        return new StringRedisSerializer();
    }

    @Bean(name = "StringTemplate")
    public RedisTemplate<String, String> redisStringTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<String, String>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setDefaultSerializer(stringRedisSerializer());
        return template;
    }

    @Bean(name = "SuggestionTemplate")
    public RedisTemplate<String, Suggestion> redisSuggestionTemplate() {
        RedisTemplate<String, Suggestion> template = new RedisTemplate<String, Suggestion>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setStringSerializer(stringRedisSerializer());
        template.setKeySerializer(stringRedisSerializer());
        return template;
    }
}
