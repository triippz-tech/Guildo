package com.triippztech.guildo.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.domain.DiscordUser;
import net.dv8tion.jda.api.entities.User;

public class FormatUtil {
    public final static String LINESTART = "\u25AB"; // ▫
    private final static String MULTIPLE_FOUND = "**Multiple %s found matching \"%s\":**";
    private final static String CMD_EMOJI = "\uD83D\uDCDC"; // 📜
    private final static String POLL_EMOJI = "\uD83D\uDCCA"; //📊
    private final static String PARTY_EMOJI = "\uD83C\uDF89"; //🎉

    public static String formatUserProfile(DiscordUser discordUser) {
        StringBuilder builder = new StringBuilder();
        builder.append(LINESTART).append("Guildo Level: **").append(discordUser.getUserLevel()).append("**\n");
        if (discordUser.isBlacklisted())
            builder.append(LINESTART + "Guildo Status: **BANNED**\n" );
        if (discordUser.getUserProfile().getFavoriteGame() != null)
            builder.append(LINESTART + "Favorite Game: ").append(discordUser.getUserProfile().getFavoriteGame()).append("\n");
        if (discordUser.getUserProfile().getTwitterUrl() != null)
            builder.append(LINESTART + "[Twitter](").append(discordUser.getUserProfile().getTwitterUrl()).append(")").append("\n");
        if (discordUser.getUserProfile().getFacebookUrl() != null)
            builder.append(LINESTART + "[Facebook](").append(discordUser.getUserProfile().getFacebookUrl()).append(")").append("\n");
        if (discordUser.getUserProfile().getTwitchUrl() != null)
            builder.append(LINESTART + "[Twitch](").append(discordUser.getUserProfile().getTwitchUrl()).append(")").append("\n");
        if (discordUser.getUserProfile().getYoutubeUrl() != null)
            builder.append(LINESTART + "[YouTube](").append(discordUser.getUserProfile().getYoutubeUrl()).append(")").append("\n");
        if (discordUser.getUserProfile().getBeamUrl() != null)
            builder.append(LINESTART + "[Beam](").append(discordUser.getUserProfile().getBeamUrl()).append(")").append("\n");
        if (discordUser.getUserProfile().getHitboxUrl() != null)
            builder.append(LINESTART + "[HitBox](").append(discordUser.getUserProfile().getHitboxUrl()).append(")").append("\n");
        builder.append(LINESTART).append("Commands Issued to Guildo: **").append(discordUser.getCommandsIssued()).append("**\n");

        return builder.toString();
    }

    public static String helpLinks(CommandEvent event)
    {
        return "\uD83D\uDD17 ["+event.getSelfUser().getName()+" Wiki]("+ BotConstants.Wiki.WIKI_BASE+")\n" // 🔗
            + "\uD83D\uDCBB [Support Server]("+event.getClient().getServerInvite()+")\n"
            +  CMD_EMOJI + " [Full Command Reference]("+ BotConstants.Wiki.COMMANDS+")";
    }

    public static String filterEveryone(String input)
    {
        return input.replace("@everyone","@\u0435veryone") // cyrillic e
            .replace("@here","@h\u0435re") // cyrillic e
            .replace("discord.gg/", "dis\u0441ord.gg/"); // cyrillic c
    }

    public static String formatFullUserId(long userId)
    {
        return "<@"+userId+"> (ID:"+userId+")";
    }

    public static String formatUser(User user)
    {
        return filterEveryone("**"+user.getName()+"**#"+user.getDiscriminator());
    }

    public static String formatFullUser(User user)
    {
        return filterEveryone("**"+user.getName()+"**#"+user.getDiscriminator()+" (ID:"+user.getId()+")");
    }

    public static String secondsToTime(long timeseconds)
    {
        StringBuilder builder = new StringBuilder();
        int years = (int)(timeseconds / (60*60*24*365));
        if(years>0)
        {
            builder.append("**").append(years).append("** years, ");
            timeseconds = timeseconds % (60*60*24*365);
        }
        int weeks = (int)(timeseconds / (60*60*24*365));
        if(weeks>0)
        {
            builder.append("**").append(weeks).append("** weeks, ");
            timeseconds = timeseconds % (60*60*24*7);
        }
        int days = (int)(timeseconds / (60*60*24));
        if(days>0)
        {
            builder.append("**").append(days).append("** days, ");
            timeseconds = timeseconds % (60*60*24);
        }
        int hours = (int)(timeseconds / (60*60));
        if(hours>0)
        {
            builder.append("**").append(hours).append("** hours, ");
            timeseconds = timeseconds % (60*60);
        }
        int minutes = (int)(timeseconds / (60));
        if(minutes>0)
        {
            builder.append("**").append(minutes).append("** minutes, ");
            timeseconds = timeseconds % (60);
        }
        if(timeseconds>0)
            builder.append("**").append(timeseconds).append("** seconds");
        String str = builder.toString();
        if(str.endsWith(", "))
            str = str.substring(0,str.length()-2);
        if(str.isEmpty())
            str="**No time**";
        return str;
    }

    public static String secondsToTimeCompact(long timeseconds)
    {
        StringBuilder builder = new StringBuilder();
        int years = (int)(timeseconds / (60*60*24*365));
        if(years>0)
        {
            builder.append("**").append(years).append("**y ");
            timeseconds = timeseconds % (60*60*24*365);
        }
        int weeks = (int)(timeseconds / (60*60*24*365));
        if(weeks>0)
        {
            builder.append("**").append(weeks).append("**w ");
            timeseconds = timeseconds % (60*60*24*7);
        }
        int days = (int)(timeseconds / (60*60*24));
        if(days>0)
        {
            builder.append("**").append(days).append("**d ");
            timeseconds = timeseconds % (60*60*24);
        }
        int hours = (int)(timeseconds / (60*60));
        if(hours>0)
        {
            builder.append("**").append(hours).append("**h ");
            timeseconds = timeseconds % (60*60);
        }
        int minutes = (int)(timeseconds / (60));
        if(minutes>0)
        {
            builder.append("**").append(minutes).append("**m ");
            timeseconds = timeseconds % (60);
        }
        if(timeseconds>0)
            builder.append("**").append(timeseconds).append("**s");
        String str = builder.toString();
        if(str.endsWith(", "))
            str = str.substring(0,str.length()-2);
        if(str.isEmpty())
            str="**No time**";
        return str;
    }
}
