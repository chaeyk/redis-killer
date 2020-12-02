package net.chaeyk.rediskiller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import java.io.*;

@SpringBootApplication
@Slf4j
public class RedisKillerApplication {

    public static void main(String[] args) throws IOException {
        File config = createRedissonConfig();
        System.setProperty("spring.jpa.properties.hibernate.cache.redisson.config", config.getAbsolutePath());

        SpringApplication.run(RedisKillerApplication.class, args);
    }

    @Data
    public static class RedissonConfig {
        private SingleServerConfig singleServerConfig = new SingleServerConfig();
    }

    @Data
    public static class SingleServerConfig {
        private String address;
        private int connectionMinimumIdleSize;
    }

    private static File createRedissonConfig() throws IOException {
        RedissonConfig config = new RedissonConfig();
        config.getSingleServerConfig().setAddress("redis://" + getRedisAddress());
        config.getSingleServerConfig().setConnectionMinimumIdleSize(20);

        ObjectMapper objectMapper = new ObjectMapper();

        File tmpFile = File.createTempFile("redisson", ".json");
        try (FileWriter fw = new FileWriter(tmpFile)) {
            fw.write(objectMapper.writeValueAsString(config));
        }

        tmpFile.deleteOnExit();
        return tmpFile.getAbsoluteFile();
    }

    private static String getRedisAddress() {
        String address = System.getProperty("killer.redis.address");
        if (address == null) {
            address = System.getenv("KILLER_REDIS_ADDRESS");
        }

        if (StringUtils.isEmpty(address)) {
            throw new RuntimeException("You should set KILLER_REDIS_ADDRESS environment variable.");
        }

        if (!address.contains(":"))
            address = address + ":6379";

        log.info("Redis address: {}", address);
        return address;
    }

}
