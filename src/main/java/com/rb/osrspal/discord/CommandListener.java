package com.rb.osrspal.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class CommandListener extends ListenerAdapter {

    private final GePriceCommand geCommand;

    public CommandListener(GePriceCommand geCommand) {
        this.geCommand = geCommand;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ge")) {
            geCommand.handle(event);
        }
    }
}
