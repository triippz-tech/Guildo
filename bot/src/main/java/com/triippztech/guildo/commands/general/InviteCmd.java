package com.triippztech.guildo.commands.general;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.commands.AbstractCommand;
import com.triippztech.guildo.config.ApplicationProperties;
import com.triippztech.guildo.service.DiscordUserService;
import com.triippztech.guildo.service.ServiceManager;
import com.triippztech.guildo.utils.BotConstants;
import org.springframework.stereotype.Component;

@Component
public class InviteCmd extends AbstractCommand {
    private final ServiceManager serviceManager;

    public InviteCmd(ServiceManager serviceManager) {
        super(serviceManager);
        this.serviceManager = serviceManager;

        this.name = "invite";
        this.help = "invite the bot";
        this.guildOnly = false;
        this.category = new Category("General");
    }

    @Override
    public void doCommand(CommandEvent event) {
        event.reply("Hello. I am **"+event.getJDA().getSelfUser().getName()+"**, a simple moderation bot built by **triippz**#0689."
            + "\nYou can find out how to add me to your server with the link below:"
            + "\n\n\uD83D\uDD17 **<"+ BotConstants.Wiki.START+">**" // 🔗
            + "\n\nFor assistance, check out the wiki: <"+ BotConstants.Wiki.WIKI_BASE+">");
    }
}
