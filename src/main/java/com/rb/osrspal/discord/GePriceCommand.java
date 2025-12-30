package com.rb.osrspal.discord;

import com.rb.osrspal.ge.GeService;
import com.rb.osrspal.util.ItemNameNormalizer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class GePriceCommand {
    private final GeService geService;

    public GePriceCommand(GeService geService) {
        this.geService = geService;
    }

    public void handle(SlashCommandInteractionEvent event) {
        String item = ItemNameNormalizer.normalize(event.getOption("item").getAsString());
        event.deferReply().queue();

        geService.lookup(item)
                .thenAccept(response -> event.getHook().sendMessage(response).queue())
                .exceptionally(e -> {
                    event.getHook().sendMessage("Could not fetch GE price.").queue();
                    return null;
                });
    }
}
