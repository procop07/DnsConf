package com.novibe.common.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class AppConfig {

    @Bean
    Gson gson() {
        return new Gson();
    }

    @Bean
    String sessionId() {
        return UUID.randomUUID().toString();
    }

    @Bean
    HttpClient httpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

}
