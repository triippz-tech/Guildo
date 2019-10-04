package com.triippztech.guildo.listeners;

import com.triippztech.guildo.service.BotSystems;
import com.triippztech.guildo.service.GuildoBotService;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class EventListener extends ListenerAdapter {

    private final GuildoBotService guildoBotService;

    public EventListener(GuildoBotService guildoBotService) {
        this.guildoBotService = guildoBotService;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        this.guildoBotService.getBotSystems().getScheduledJobs().checkForNewUsers(
            guildoBotService.getBotSystems().getShardManager());
        this.guildoBotService.getBotSystems().getThreadpool().scheduleWithFixedDelay( () ->
            this.guildoBotService.getBotSystems().getScheduledJobs().checkForExpiredBans(
                guildoBotService.getBotSystems().getShardManager())
            , 0
            , 1
            , TimeUnit.MINUTES);

    }
}
