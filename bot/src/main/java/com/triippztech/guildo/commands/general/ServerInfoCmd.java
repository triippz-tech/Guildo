package com.triippztech.guildo.commands.general;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.commands.AbstractCommand;
import com.triippztech.guildo.config.ApplicationProperties;
import com.triippztech.guildo.service.DiscordUserService;
import com.triippztech.guildo.service.ServiceManager;
import com.triippztech.guildo.utils.FormatUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.triippztech.guildo.utils.FormatUtil.LINESTART;

@Component
public class ServerInfoCmd extends AbstractCommand {
    private final ServiceManager serviceManager;

    private final static String GUILD_EMOJI = "\uD83D\uDDA5"; // 🖥
    private final static String NO_REGION = "\u2754"; // ❔

    public ServerInfoCmd(ServiceManager serviceManager) {
        super(serviceManager);
        this.serviceManager = serviceManager;

        this.name = "serverinfo";
        this.aliases = new String[]{"server","guildinfo"};
        this.help = "shows server info";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly = true;
        this.category = new Category("General");
    }

    @Override
    public void doCommand(CommandEvent event) {
        Guild guild = event.getGuild();
        long onlineCount = guild.getMembers().stream().filter((u) -> (u.getOnlineStatus()!= OnlineStatus.OFFLINE)).count();
        long botCount = guild.getMembers().stream().filter(m -> m.getUser().isBot()).count();
        EmbedBuilder builder = new EmbedBuilder();
        String title = FormatUtil.filterEveryone(GUILD_EMOJI + " Information about **"+guild.getName()+"**:");
        String verif;
        switch(guild.getVerificationLevel()) {
            case VERY_HIGH:
                verif = "┻━┻ミヽ(ಠ益ಠ)ノ彡┻━┻";
                break;
            case HIGH:
                verif = "(╯°□°）╯︵ ┻━┻";
                break;
            default:
                verif = guild.getVerificationLevel().name();
                break;
        }
        guild.getRegion();
        String str = LINESTART + "ID: **" + guild.getId() + "**\n"
            + LINESTART + "Owner: " + FormatUtil.formatUser(Objects.requireNonNull(guild.getOwner()).getUser()) + "\n"
            + LINESTART + "Location: " + guild.getRegion().getEmoji() + " **" + guild.getRegion().getName() + "**\n"
            + LINESTART + "Creation: **" + guild.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "**\n"
            + LINESTART + "Users: **" + guild.getMemberCache().size() + "** (" + onlineCount + " online, " + botCount + " bots)\n"
            + LINESTART + "Channels: **" + guild.getTextChannelCache().size()
                + "** Text, **" + guild.getVoiceChannelCache().size()
                + "** Voice, **" + guild.getCategoryCache().size()
                + "** Categories\n"
            + LINESTART + "Verification: **" + verif + "**";
        if(!guild.getFeatures().isEmpty())
            str += "\n"+LINESTART+"Features: **"+String.join("**, **", guild.getFeatures())+"**";
        if(guild.getSplashId()!=null)
        {
            builder.setImage(guild.getSplashUrl()+"?size=1024");
            str += "\n"+LINESTART+"Splash: ";
        }
        if(guild.getIconUrl()!=null)
            builder.setThumbnail(guild.getIconUrl());
        builder.setColor(guild.getOwner().getColor());
        builder.setDescription(str);
        event.reply(new MessageBuilder().append(title).setEmbed(builder.build()).build());
    }
}
