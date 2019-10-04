package com.triippztech.guildo.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.commands.moderation.meta.AbstractModerationCommand;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.domain.TempBan;
import com.triippztech.guildo.domain.enumeration.PunishmentType;
import com.triippztech.guildo.domain.enumeration.ReturnStatus;
import com.triippztech.guildo.listeners.CommandExceptionListener;
import com.triippztech.guildo.service.*;
import com.triippztech.guildo.service.moderation.ModerationLogItemService;
import com.triippztech.guildo.service.moderation.TempBanService;
import com.triippztech.guildo.service.server.GuildServerService;
import com.triippztech.guildo.service.user.DiscordUserService;
import com.triippztech.guildo.utils.ArgsUtil;
import com.triippztech.guildo.utils.FormatUtil;
import com.triippztech.guildo.utils.OtherUtil;
import com.triippztech.guildo.utils.Pair;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.dv8tion.jda.api.Permission.BAN_MEMBERS;
import static net.dv8tion.jda.api.Permission.MANAGE_SERVER;

@Component
public class BanCmd extends AbstractModerationCommand {
    private final ServiceManager serviceManager;

    private final static String LINESTART = "\u25AB"; // ▫


    public BanCmd(ServiceManager serviceManager)
    {
        super(serviceManager, new Permission[]{BAN_MEMBERS});
        this.serviceManager = serviceManager;
        this.name = "ban";
        this.arguments = "temp|perm|list";
        this.help = "bans users";
        this.guildOnly = true;
        this.children = new AbstractModerationCommand[] {
            new TempBanCmd( this.serviceManager, new Permission[]{BAN_MEMBERS} ),
            new PermBanCmd( this.serviceManager, new Permission[]{BAN_MEMBERS} ),
            new BanListCmd( this.serviceManager, new Permission[]{MANAGE_SERVER} )
        };
    }

    @Override
    public void doCommand(CommandEvent event) {
        StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Guild Ban Commands:\n");
        for (Command cmd : this.children)
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
        event.reply(builder.toString());
    }

    public class TempBanCmd extends AbstractModerationCommand
    {
        private final DiscordUserService discordUserService;
        private final TempBanService tempBanService;
        private final GuildServerService guildServerService;
        private final ModerationLogItemService modLogService;

        TempBanCmd(ServiceManager serviceManager, Permission[] permissions) {
            super(serviceManager, permissions);
            this.discordUserService = serviceManager.getDiscordUserService();
            this.guildServerService = serviceManager.getGuildServerService();
            this.tempBanService = serviceManager.getTempBanService();
            this.modLogService = serviceManager.getModLogService();
            this.name = "temp";
            this.arguments = "<@users> <time> <reason>";
            this.help = "Temporarily bans a user(s) from the server, for a period of time";
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

            int minutes = getMinutes(args.time);
            if ( minutes < 1 ) {
                event.replyError("Must provide a length of time (in minutes) to ban user for (ex. 1M, 1HR, 1D)");
                return;
            }
            if ( args.reason.isEmpty() ) {
                event.replyError("Must provide a reason for banning a user");
                return;
            }

            List<Long> ids = new ArrayList<>(args.ids);
            StringBuilder builder = new StringBuilder();
            args.members.forEach(m ->
            {
                if(!event.getMember().canInteract(m))
                    builder.append("\n").append(event.getClient().getError()).append(" You do not have permission to ban ").append(FormatUtil.formatUser(m.getUser()));
                else if(!event.getSelfMember().canInteract(m))
                    builder.append("\n").append(event.getClient().getError()).append(" I am unable to ban ").append(FormatUtil.formatUser(m.getUser()));
                else if(m.getPermissions().contains(Permission.MANAGE_SERVER))
                    builder.append("\n").append(event.getClient().getError()).append(" I won't ban ").append(FormatUtil.formatUser(m.getUser())).append(" because they have the Moderator Role");
                else
                    ids.add(m.getUser().getIdLong());
            });
            args.unresolved.forEach(un -> builder.append("\n").append(event.getClient().getWarning()).append(" Could not resolve `").append(un).append("` to a user ID"));
            args.users.forEach(u -> ids.add(u.getIdLong()));

            if(ids.isEmpty())
            {
                event.reply(builder.toString());
                return;
            }

            List<User> banUserList = OtherUtil.resolveUsers(ids, event.getJDA().getShardManager() );
            if (banUserList.isEmpty()) {
                event.replyError("Unable to find user(s)");
                return;
            }
            GuildServer guildServer = this.guildServerService.getGuildServer( event.getGuild() );
            for ( int i = 0; i < banUserList.size(); i++ ) {
                long uid = banUserList.get(i).getIdLong();
                String id = Long.toString(uid);
                boolean last = i+1 == ids.size();
                User user = banUserList.get(i);

                DiscordUser discordUser = discordUserService.getDiscordUser( banUserList.get(i) );
                Pair<ReturnStatus, TempBan> tempBanPair =
                    tempBanService.addTempBan(
                          guildServer
                        , event.getMember()
                        , discordUser
                        , args.time
                        , args.reason);
                if ( tempBanPair.getKey().equals(ReturnStatus.WARNING)) {
                    builder.append("\n").append(event.getClient().getWarning()).append("User is already banned");
                    event.reply(builder.toString());
                }
                else {
                    messageUser(event, user, args.reason);
                    event.getGuild().ban(banUserList.get(i), 7, args.reason).queue(success -> {
                        builder.append("\n").append(event.getClient().getSuccess())
                            .append(" Successfully banned <@").append(id).append(">");

                        modLogService.createItem(
                            guildServer,
                            event.getMember(),
                            discordUser,
                            PunishmentType.TEMP_BAN,
                            args.reason,
                            event.getChannel().getIdLong(),
                            event.getChannel().getName()
                        );
                        if(last)
                            event.reply(builder.toString());
                    }, failure -> {
                        builder.append("\n").append(event.getClient().getError()).append(" Failed to ban <@").append(id).append(">");
                        tempBanService.delete(tempBanPair.getValue().getId());
                        modLogService.createItem(
                            guildServer,
                            event.getMember(),
                            discordUser,
                            PunishmentType.TEMP_BAN,
                            "FAILED: " + failure.getMessage(),
                            event.getChannel().getIdLong(),
                            event.getChannel().getName()
                        );

                        if(last)
                            event.reply(builder.toString());
                    });
                }
            }
        }
    }

    public class PermBanCmd extends AbstractModerationCommand
    {
        private final ModerationLogItemService moderationLogServer;
        private final DiscordUserService discordUserService;
        private final GuildServerService guildServerService;

        PermBanCmd(ServiceManager serviceManager, Permission[] permissions) {
            super(serviceManager, permissions);

            this.moderationLogServer = serviceManager.getModLogService();
            this.discordUserService = serviceManager.getDiscordUserService();
            this.guildServerService = serviceManager.getGuildServerService();
            this.name = "perm";
            this.arguments = "<@users> [reason]";
            this.help = "Permanently bans a user(s) from the server";
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

            int minutes = getMinutes(args.time);
            if ( minutes < 1 ) {
                event.replyError("Must provide a length of time (in minutes) to ban user for (ex. 1M, 1HR, 1D)");
                return;
            }
            if ( args.reason.isEmpty() ) {
                event.replyError("Must provide a reason for banning a user");
                return;
            }

            List<Long> ids = new ArrayList<>(args.ids);
            StringBuilder builder = new StringBuilder();
            args.members.forEach(m ->
            {
                if(!event.getMember().canInteract(m))
                    builder.append("\n").append(event.getClient().getError()).append(" You do not have permission to ban ").append(FormatUtil.formatUser(m.getUser()));
                else if(!event.getSelfMember().canInteract(m))
                    builder.append("\n").append(event.getClient().getError()).append(" I am unable to ban ").append(FormatUtil.formatUser(m.getUser()));
                else if(m.getPermissions().contains(Permission.MANAGE_SERVER))
                    builder.append("\n").append(event.getClient().getError()).append(" I won't ban ").append(FormatUtil.formatUser(m.getUser())).append(" because they have the Moderator Role");
                else
                    ids.add(m.getUser().getIdLong());
            });
            args.unresolved.forEach(un -> builder.append("\n").append(event.getClient().getWarning()).append(" Could not resolve `").append(un).append("` to a user ID"));
            args.users.forEach(u -> ids.add(u.getIdLong()));

            if(ids.isEmpty())
            {
                event.reply(builder.toString());
                return;
            }

            List<User> banUserList = OtherUtil.resolveUsersFromId(ids, event.getGuild());

            for ( int i = 0; i < banUserList.size(); i++ ) {
                long uid = banUserList.get(i).getIdLong();
                String id = Long.toString(uid);
                boolean last = i+1 == ids.size();
                User user = banUserList.get(i);

                DiscordUser discordUser = discordUserService.getDiscordUser( banUserList.get(i) );
                GuildServer guildServer = guildServerService.getGuildServer( event.getGuild() );
                this.moderationLogServer.createItem(
                      guildServer
                    , event.getMember()
                    , discordUser
                    , PunishmentType.TEMP_BAN
                    , args.reason
                    , event.getChannel().getIdLong()
                    , event.getChannel().getName());

                messageUser(event, user, args.reason);
                event.getGuild().ban( banUserList.get( i ), 7).reason(args.reason).queue( success -> {
                    builder.append("\n").append(event.getClient().getSuccess())
                        .append(" Successfully banned <@").append(id).append(">");
                    if(last)
                        event.reply(builder.toString());
                }, failure -> {
                    builder.append("\n").append(event.getClient().getError()).append(" Failed to ban <@").append(id).append(">");
                    if(last)
                        event.reply(builder.toString());
                });
            }
        }
    }

    public class BanListCmd extends AbstractModerationCommand
    {

        BanListCmd(ServiceManager serviceManager, Permission[] permissions) {
            super(serviceManager, permissions);

            this.name = "list";
            this.arguments = "list";
            this.help = "Lists all the currently banned users in the server";
        }

        @Override
        public void doCommand(CommandEvent event) {
            List<Guild.Ban> banList = event.getGuild().retrieveBanList().complete();
            if ( banList.isEmpty() ) {
                event.reply("There are no banned users");
                return;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(event.getGuild().getName()).append(" Banned Users").append("\n");
            for ( Guild.Ban ban : banList ) {
                builder.append(LINESTART).append(ban.getUser().getName())
                    .append("\t: ").append(ban.getUser().getIdLong())
                    .append(" - ").append(ban.getReason()).append("\n");
            }
            event.reply(builder.toString());
        }
    }

    private int getMinutes(Integer time) {
        if(time < 0)
            throw new CommandExceptionListener.CommandErrorException("Timed bans cannot be negative time!");
        else if(time == 0)
            return 0;
        else if(time > 60)
            return (int)Math.round(time/60.0);
        else
            return 1;
    }

    private void messageUser(CommandEvent event, User user, String reason) {
        user.openPrivateChannel().complete()
            .sendMessage("Hi there! You have been banned from **" + event.getGuild().getName() + "** " +
            "for the following reason(s): `" + reason + "`").queue();
    }
}
