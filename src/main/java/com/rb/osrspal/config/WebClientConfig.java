package com.rb.osrspal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final Logger log =
            LoggerFactory.getLogger(WebClientConfig.class);

    @Bean(name = "osrsWebClient")
    public WebClient osrsWebClient(@Value("${osrs.base-url}") String baseUrl) {
        log.debug("{}", baseUrl);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "osrspal-discord-bot/1.0")
                .build();
    }

    @Bean(name = "geMappingWebClient")
    public WebClient geMappingWebClient(@Value("${osrs.ge-mapping-base-url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024) // 16 MB, enough for names.json
                )
                .build();
    }
}
