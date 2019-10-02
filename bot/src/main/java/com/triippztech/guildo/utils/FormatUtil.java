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
}
