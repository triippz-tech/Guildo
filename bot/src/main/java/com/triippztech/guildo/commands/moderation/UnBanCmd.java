package com.triippztech.guildo.commands.moderation;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.commands.moderation.meta.AbstractModerationCommand;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.domain.TempBan;
import com.triippztech.guildo.domain.enumeration.PunishmentType;
import com.triippztech.guildo.service.*;
import com.triippztech.guildo.service.moderation.ModerationLogItemService;
import com.triippztech.guildo.service.moderation.TempBanService;
import com.triippztech.guildo.service.server.GuildServerService;
import com.triippztech.guildo.service.user.DiscordUserService;
import com.triippztech.guildo.utils.ArgsUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.dv8tion.jda.api.Permission.BAN_MEMBERS;

@Component
public class UnBanCmd extends AbstractModerationCommand {
    private final ServiceManager serviceManager;
    private final TempBanService tempBanService;
    private final DiscordUserService discordUserService;
    private final ModerationLogItemService moderationLogService;
    private final GuildServerService guildServerService;

    public UnBanCmd(ServiceManager serviceManager) {
        super(serviceManager, new Permission[]{BAN_MEMBERS});
        this.serviceManager = serviceManager;
        this.tempBanService = serviceManager.getTempBanService();
        this.discordUserService = serviceManager.getDiscordUserService();
        this.moderationLogService = serviceManager.getModLogService();
        this.guildServerService = serviceManager.getGuildServerService();

        this.name = "unban";
        this.arguments = "@user";
        this.help = "Un-bans users";
        this.guildOnly = true;
    }
    @Override
    @SuppressWarnings("Duplicates")
    public void doCommand(CommandEvent event) {
        ArgsUtil.ResolvedArgs args = ArgsUtil.resolve(event.getArgs(), true, event.getGuild());
        if(args.isEmpty())
        {
            event.replyError("Please include at least one user to ban (@mention or ID)!");
            return;
        }

        List<Long> ids = new ArrayList<>(args.ids);
        StringBuilder builder = new StringBuilder();

        args.unresolved.forEach(un -> builder.append("\n").append(event.getClient().getWarning()).append(" Could not resolve `").append(un).append("` to a user ID"));
        args.users.forEach(u -> ids.add(u.getIdLong()));

        if(ids.isEmpty())
        {
            event.reply(builder.toString());
            return;
        }

        GuildServer guildServer = this.guildServerService.getGuildServer( event.getGuild() );
        ids.forEach( userId -> {
            event.getGuild().unban(String.valueOf(userId)).queue(success -> {
                DiscordUser discordUser = this.discordUserService.getDiscordUser( userId );
                // TODO delete tempban entry if applicable
                moderationLogService.createItem(
                      guildServer
                    , event.getMember()
                    , discordUser
                    , PunishmentType.UN_BAN
                    , "Un-ban user"
                    , event.getChannel().getIdLong()
                    , event.getChannel().getName());
                event.reply("<@" + userId + "> successfully unbanned");
                checkIfTemp(discordUser.getUserId(), event.getGuild());
            }, failure -> {
                DiscordUser discordUser = this.discordUserService.getDiscordUser( userId );
                moderationLogService.createItem(
                      guildServer
                    , event.getMember()
                    , discordUser
                    , PunishmentType.UN_BAN
                    , "Unable to remove ban from user: " + failure.getMessage()
                    , event.getChannel().getIdLong()
                    , event.getChannel().getName());
                event.reply("<@" + userId + "> UNSUCCESSFULLY unbanned");
                checkIfTemp(discordUser.getUserId(), event.getGuild());
            });
        });
    }

    public void checkIfTemp(Long userId, Guild guild) {
        Optional<TempBan> tempBan = this.tempBanService.findByUserAndGuild(userId, guild);
        if (tempBan.isEmpty()) return;
        this.tempBanService.delete(tempBan.get().getId());
    }
    // TODO Add a menu list to remove bans using a reaction
}
