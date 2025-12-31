package com.rb.osrspal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final int MAP_BUFFER_SIZE_BYTES = 16 * 1024 * 1024; // 16 MB

    @Bean(name = "wikiPricesWebClient")
    public WebClient wikiPricesWebClient(@Value("${osrs.base-url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "osrspal-discord-bot - Bibzy")
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAP_BUFFER_SIZE_BYTES)
                )
                .build();
    }
}
