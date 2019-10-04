package com.triippztech.guildo.commands.moderation;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.commands.moderation.meta.AbstractModerationCommand;
import com.triippztech.guildo.domain.enumeration.ModLogFilter;
import com.triippztech.guildo.exception.command.EmptyListException;
import com.triippztech.guildo.service.server.GuildServerService;
import com.triippztech.guildo.service.moderation.ModerationLogItemService;
import com.triippztech.guildo.service.ServiceManager;
import net.dv8tion.jda.api.Permission;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static net.dv8tion.jda.api.Permission.MANAGE_SERVER;

@Component
public class ModLogCmd extends AbstractModerationCommand {
    private final ServiceManager serviceManager;
    private final ModerationLogItemService modLogService;
    private final GuildServerService guildServerService;

    public ModLogCmd(ServiceManager serviceManager) {
        super(serviceManager, new Permission[]{MANAGE_SERVER});
        this.serviceManager = serviceManager;
        this.modLogService = serviceManager.getModLogService();
        this.guildServerService = serviceManager.getGuildServerService();

        this.name = "modlog";
        this.aliases = new String[]{"ml"};
        this.help = "Filters the mod log for viewing based off 1 argument";
        this.arguments = "ALL | BAN | KICK | MUTE | UNBAN";
        this.guildOnly = true;
    }

    @Override
    public void doCommand(CommandEvent event) {
        String argument = event.getArgs().trim().toUpperCase();

        ModLogFilter filter = ModLogFilter.valueOf(argument);
        if ( filter.equals( ModLogFilter.ALL ) ) {
            try {
                event.getChannel().sendTyping().queue();
                byte[] logBytes = modLogService.getAllLogItems(event.getGuild(), guildServerService);
                event.getChannel().sendFile(logBytes,
                    event.getGuild().getName().replaceAll(" ", "_") + "_log.txt").queue();
            } catch (EmptyListException e) {
                event.replyWarning(e.getMessage());
                return;
            } catch (IOException e) {
                event.replyError("Error generating log, please contact DEVS for help, if this issue persists");
                return;
            }
        }

    }
}
