package com.triippztech.guildo.commands.moderation;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.triippztech.guildo.commands.moderation.meta.AbstractModerationCommand;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.enumeration.Action;
import com.triippztech.guildo.service.ServiceManager;
import com.triippztech.guildo.service.BotSystems;
import com.triippztech.guildo.utils.FormatUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserCheckCmd extends AbstractModerationCommand {

    private final ServiceManager serviceManager;
    private final BotSystems botSystems;

    public UserCheckCmd(ServiceManager serviceManager, BotSystems botSystems) {
        super(serviceManager, new Permission[]{Permission.MANAGE_SERVER});
        this.serviceManager = serviceManager;
        this.botSystems = botSystems;

        this.name = "check";
        this.arguments = "<@user>";
        this.help = "This command is used to see a user's strikes and mute/ban status for the current server.";
        this.guildOnly = true;
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        if(event.getArgs().isEmpty() || event.getArgs().equalsIgnoreCase("help"))
        {
            event.replySuccess("This command is used to see a user's strikes and mute/ban status for the current server. Please include a user or user ID to check.");
            return;
        }
        event.getChannel().sendTyping().queue();
        List<Member> members = FinderUtil.findMembers(event.getArgs(), event.getGuild());
        if(!members.isEmpty())
        {
            check(event, members.get(0).getUser());
            return;
        }
        List<User> users = FinderUtil.findUsers(event.getArgs(), event.getJDA());
        if(!users.isEmpty())
        {
            check(event, users.get(0));
            return;
        }
        try
        {
            long uid = Long.parseLong(event.getArgs());
            User u = this.botSystems.getShardManager().getUserById(uid);
            if(u!=null)
                check(event, u);
            else
                event.getJDA().retrieveUserById(uid).queue(
                    user -> check(event, user),
                    v -> event.replyError("`"+uid+"` is not a valid user ID"));
        }
        catch(Exception ex)
        {
            event.replyError("Could not find a user `"+event.getArgs()+"`");
        }
    }

    private void check(CommandEvent event, User user)
    {
        if(event.getGuild().isMember(user))
            check(event, user, null);
        else
            event.getGuild().retrieveBan(user).queue(ban -> check(event, user, ban), t -> check(event, user, null));
    }

    private void check(CommandEvent event, User user, Guild.Ban ban)
    {
        DiscordUser discordUser = this.serviceManager.getDiscordUserService().getDiscordUser( user );
        int strikes = this.serviceManager.getStrikeService().getUserStrikeCount(discordUser, event.getGuild());
        int minutesMuted = this.serviceManager.getMuteService().timeUntilUnmute(event.getGuild(), user.getIdLong());
        int minutesBanned = this.serviceManager.getTempBanService().timeUntilUnban(event.getGuild(), user.getIdLong());

        String str = "Moderation Information for "+ FormatUtil.formatFullUser(user)+":\n"
            + Action.STRIKE.getEmoji() + " Strikes: **"+strikes+"**\n"
//            + Action.MUTE.getEmoji() + " Muted: **" + (event.getGuild().isMember(user)
//            ? (event.getGuild().getMember(user).getRoles().contains(mRole) ? "Yes" : "No")
//            : "Not In Server") + "**\n"
            + Action.TEMPMUTE.getEmoji() + " Mute Time Remaining: " + (minutesMuted <= 0 ? "N/A" : FormatUtil.secondsToTime(minutesMuted * 60)) + "\n"
            + Action.BAN.getEmoji() + " Banned: **" + (ban==null ? "No**" : "Yes** (`" + ban.getReason() + "`)") + "\n"
            + Action.TEMPBAN.getEmoji() + " Ban Time Remaining: " + (minutesBanned <= 0 ? "N/A" : FormatUtil.secondsToTime(minutesBanned * 60));
        event.reply(str);
    }
}
