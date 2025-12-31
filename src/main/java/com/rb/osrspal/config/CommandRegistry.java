package com.rb.osrspal.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandRegistry {

    public CommandRegistry(JDA jda) {
        jda.updateCommands()
                .addCommands(
                        Commands.slash("ge", "Lookup GE price")
                                .addOption(
                                        OptionType.STRING,
                                        "item",
                                        "Item name",
                                        true
                                )
                )
                .queue();
    }
}
