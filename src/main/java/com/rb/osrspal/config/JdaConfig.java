package com.rb.osrspal.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JdaConfig {

    @Bean
    public JDA jda(
            @Value("${discord.token}") String token,
            List<ListenerAdapter> listeners
    ) throws InterruptedException {

        JDABuilder builder = JDABuilder.createDefault(token);

        listeners.forEach(builder::addEventListeners);

        JDA jda = builder.build();

        jda.awaitReady();

        return jda;
    }
}
