package com.triippztech.guildo.service.scheduled;

import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.TempBan;
import com.triippztech.guildo.domain.enumeration.PunishmentType;
import com.triippztech.guildo.service.*;
import com.triippztech.guildo.service.moderation.ModerationLogItemService;
import com.triippztech.guildo.service.moderation.TempBanService;
import com.triippztech.guildo.service.server.GuildServerService;
import com.triippztech.guildo.service.user.DiscordUserService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class ScheduledJobs {
    private final Logger log = LoggerFactory.getLogger(ScheduledJobs.class);

    private final DiscordUserService discordUserService;
    private final TempBanService tempBanService;
    private final GuildServerService guildServerService;
    private final ModerationLogItemService modLogService;

    public ScheduledJobs(GuildoBotService guildoBotService) {
        this.discordUserService = guildoBotService.getServiceManager().getDiscordUserService();
        this.tempBanService = guildoBotService.getServiceManager().getTempBanService();
        this.guildServerService = guildoBotService.getServiceManager().getGuildServerService();
        this.modLogService = guildoBotService.getServiceManager().getModLogService();
    }

    public void initialJobs(ShardManager shardManager) {
        this.checkForNewUsers(shardManager);
        this.checkForExpiredBans(shardManager);
    }

    public void checkForNewUsers(ShardManager shardManager) {
        log.debug("Updating Guildo User Repository");
        int newUsers = 0;

        for( Guild guild : shardManager.getGuilds() )
        {
            for ( Member member : guild.getMembers() )
            {
                if (! member.getUser().isBot()) {
                    if ( !this.discordUserService.userExists2(member.getUser().getIdLong() ) ) {
                        discordUserService.createDiscordUser(member.getUser());
                        newUsers++;
                    }
                }
            }
        }
        log.info("Added {} new users", newUsers);
    }

    public void checkForExpiredBans(ShardManager shardManager) {
        log.info("Checking for expired Temporary Bans");

        List<TempBan> tempBanList = tempBanService.findAll();

        for ( TempBan tempBan : tempBanList ) {
            if ( tempBan.getEndTime().isBefore(Instant.now())) {
                tempBanService.delete(tempBan.getId());
                DiscordUser discordUser = tempBan.getBannedUser();
                Guild guild = shardManager.getGuildById(tempBan.getTempBanGuildServer().getGuildId());
                assert guild != null;
                guild.unban(String.valueOf(discordUser.getUserId())).reason("Ban has expired").queue(success -> {
                    Objects.requireNonNull(shardManager.getUserById(discordUser.getUserId())).openPrivateChannel().queue(connSucc -> {
                        connSucc.sendMessage("Your ban from **" + guild.getName() + "** has expired").queue();
                    });
                    this.modLogService.createBotGeneratedItem(
                        guildServerService.getGuildServer( guild )
                        , tempBan.getBannedUser()
                        , PunishmentType.UN_BAN
                        , "Temporary Ban has expired"
                    );
                }, failure -> {
                    this.modLogService.createBotGeneratedItem(
                        guildServerService.getGuildServer( guild )
                        , tempBan.getBannedUser()
                        , PunishmentType.UN_BAN
                        , "Failed to remove temporary ban: " + failure.getMessage()
                    );
                });
            }
        }
    }
}
