package com.triippztech.guildo.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.commands.owner.meta.AbstractOwnerCommand;
import com.triippztech.guildo.config.ApplicationProperties;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.service.user.DiscordUserService;
import com.triippztech.guildo.service.ServiceManager;
import com.triippztech.guildo.utils.ArgsUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;

@Component
public class BlacklistCmd extends AbstractOwnerCommand {
    private final ServiceManager serviceManager;
    private final DiscordUserService discordUserService;
    private final ApplicationProperties applicationProperties;
    private String DEFAULT_BAN_MESSAGE =
        "You have been blacklisted from using Guildo for the following reason:\n `%s`.\n\nIf you feel like this is a mistake," +
            "please join the support discord channel and make contact with a Mod. %s";
    private String DEFAULT_UNBAN_MESSAGE =
        "You have been unbanned from Guildo's services. Message from Admin: %s. For questions, contact a MOD via the support " +
            "Discord server: %s";

    public BlacklistCmd(ServiceManager serviceManager) {
        super(serviceManager);
        this.serviceManager = serviceManager;
        this.applicationProperties = serviceManager.getApplicationProperties();
        this.discordUserService = serviceManager.getDiscordUserService();

        this.name = "blacklist";
        this.aliases = new String[]{"bl"};
        this.help = "Blacklist's a user from using Guildo's features";
        this.arguments = "@user | reason";
    }

    @Override
    public void doCommand(CommandEvent event) {
        String[] args = event.getArgs().split("\\|");
        if (args.length < 2) {
            event.replyError("Must include a @user and reason for blacklisting");
            return;
        }

        if ( !checkGuild( args, event ) )
            checkOutside(args, event);

    }

    private boolean checkGuild(String[] args, CommandEvent event) {
        ArgsUtil.ResolvedArgs resolvedArgs = ArgsUtil.resolve(args[0].trim(), event.getGuild());
        if (resolvedArgs.members.size() == 0 ) return false;

        for (Member member: resolvedArgs.members )
        {
            if ( !discordUserService.isOwner(member.getUser() ) ) {
                DiscordUser discordUser = discordUserService.blackListUser(member.getUser());
                member.getUser().openPrivateChannel().complete().sendMessage(
                    String.format( discordUser.isBlacklisted() ? DEFAULT_BAN_MESSAGE : DEFAULT_UNBAN_MESSAGE,
                        args[1].trim(), applicationProperties.getDiscord().getServerInvite())
                ).queue();
                if (!discordUser.isBlacklisted())
                    event.replySuccess("User **" + member.getUser().getName() + "** has been blacklisted");
                else
                    event.replySuccess("User **" + member.getUser().getName() + "** has been unblacklisted");
            }
            event.replyError("You cannot ban the almighty creator");
        }
        return true;
    }

    private void checkOutside(String[] args, CommandEvent event) {
        User user = event.getJDA().getUserById(args[0].trim());
        if ( user != null ) {
            if (!discordUserService.isOwner(user)) {
                DiscordUser discordUser = discordUserService.blackListUser(user);
                user.openPrivateChannel().complete().sendMessage(
                    String.format(discordUser.isBlacklisted() ? DEFAULT_BAN_MESSAGE : DEFAULT_UNBAN_MESSAGE,
                        args[1].trim(), applicationProperties.getDiscord().getServerInvite())
                ).queue();
                if (!discordUser.isBlacklisted())
                    event.replySuccess("User **" + user.getName() + "** has been blacklisted");
                else
                    event.replySuccess("User **" + user.getName() + "** has been unblacklisted");
                return;
            }
            event.replyError("You cannot ban the almighty creator");
        } else {
            event.replyError("That user does not exist");
        }
    }
}
