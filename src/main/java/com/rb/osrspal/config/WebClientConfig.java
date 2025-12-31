package com.rb.osrspal.config;

import com.rb.osrspal.discord.GePriceCommand;
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

    @Bean
    public WebClient osrsWebClient(@Value("${osrs.base-url}") String baseUrl) {
        log.debug("{}", baseUrl);
        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "osrspal-discord-bot/1.0")
                .build();
    }
}
