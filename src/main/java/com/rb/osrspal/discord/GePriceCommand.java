package com.rb.osrspal.discord;

import com.rb.osrspal.ge.GeItemMappingService;
import com.rb.osrspal.ge.GeService;
import com.rb.osrspal.util.ItemNameNormalizer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GePriceCommand {
    private final GeItemMappingService geItemMappingService;
    private final GeService geService;

    public GePriceCommand(GeItemMappingService geItemMappingService, GeService geService) {
        this.geItemMappingService = geItemMappingService;
        this.geService = geService;
    }

    private static final Logger log = LoggerFactory.getLogger(GePriceCommand.class);

    public void handle(SlashCommandInteractionEvent event) {
        String normalizedItemName = ItemNameNormalizer.normalize(
                Objects.requireNonNull(event.getOption("item")).getAsString());

        event.deferReply().queue();

        geItemMappingService.getItemIdFromNormalizedName(normalizedItemName)
                .thenCompose(geService::lookupPriceById)
                .thenAccept(response ->
                        event.getHook().sendMessage(response).queue())
                .exceptionally(ex -> {
                    log.error("GE price lookup failed", ex);
                    event.getHook().sendMessage(
                                    "❌ Could not fetch GE price for **" + normalizedItemName + "**")
                            .queue();
                    return null;
                });
    }
}
