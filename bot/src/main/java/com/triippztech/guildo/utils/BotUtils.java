package com.triippztech.guildo.utils;


import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.service.user.DiscordUserService;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BotUtils {

    private final DiscordUserService discordUserService;

    public BotUtils(DiscordUserService discordUserService) {
        this.discordUserService = discordUserService;
    }

    public String getUserProfile(User targetUser)
    {
        List<String> mutualGuilds = new ArrayList<>();
        String sharedGuildsStr;

        Optional<DiscordUser> user = this.discordUserService.findByUserId(targetUser.getIdLong());
        if (user.isEmpty())
            return String.format("%s is not a Guildo user or part of a server which uses Guildo", targetUser.getName());
        else return FormatUtil.formatUserProfile(user.get());
    }
}
