package com.triippztech.guildo.commands.general;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.JDAUtilitiesInfo;
import com.triippztech.guildo.commands.AbstractCommand;
import com.triippztech.guildo.service.ServiceManager;
import com.triippztech.guildo.utils.FormatUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class AboutCmd extends AbstractCommand {
    private final ServiceManager serviceManager;

    public AboutCmd(ServiceManager serviceManager) {
        super(serviceManager);
        this.serviceManager = serviceManager;

        this.name = "about";
        this.help = "Shows information about the bot";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Category("General");
    }

    @Override
    public void doCommand(CommandEvent event) {
        ShardManager sm = event.getJDA().getShardManager();
        assert sm != null;
        event.reply(new MessageBuilder()
            .setContent(" **All about Guildo** " )
            .setEmbed(new EmbedBuilder()
                .setColor(event.getGuild()==null ? Color.GRAY : event.getSelfMember().getColor())
                .setDescription("Hello, I am **Guildo**#8540, a bot designed to keep your guild's server safe and make moderating fast and easy!\n"
                    + "I was written in Java by **triippz**#0689 using [JDA](" + JDAInfo.GITHUB + ") and [JDA-Utilities](" + JDAUtilitiesInfo.GITHUB + ")\n"
                    + "Type `" + event.getClient().getPrefix() + event.getClient().getHelpWord() + "` for help and information.\n\n"
                    + FormatUtil.helpLinks(event))
                .addField("Stats", sm.getShardsTotal()+ " Shards\n" + sm.getGuildCache().size() + " Servers", true)
                .addField("", sm.getUserCache().size() + " Users\n" + Math.round(sm.getAverageGatewayPing()) + "ms Avg Ping", true)
                .addField("", sm.getTextChannelCache().size() + " Text Channels\n" + sm.getVoiceChannelCache().size() + " Voice Channels", true)
                .setFooter("Last restart", null)
                .setTimestamp(event.getClient().getStartTime())
                .build())
            .build());

    }
}
