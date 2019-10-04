package com.triippztech.guildo.service;

import com.triippztech.guildo.config.ApplicationProperties;
import com.triippztech.guildo.service.guild.*;
import com.triippztech.guildo.service.moderation.*;
import com.triippztech.guildo.service.server.GuildServerProfileService;
import com.triippztech.guildo.service.server.GuildServerService;
import com.triippztech.guildo.service.user.DiscordUserProfileService;
import com.triippztech.guildo.service.user.DiscordUserService;
import org.springframework.stereotype.Service;

@Service
public class ServiceManager {
    private final ApplicationProperties applicationProperties;

    // Auto mod services
    private final AutoModAntiDupQueryService autoModAntiDupQueryService;
    private final AutoModAutoRaidService autoModAutoRaidService;
    private final AutoModerationService autoModerationService;
    private final AutoModIgnoreService autoModIgnoreService;
    private final AutoModMentionsService autoModMentionsService;

    //
    private final DiscordUserProfileService discordUserProfileService;
    private final DiscordUserService discordUserService;
    private final GiveAwayService giveAwayService;
    private final GuildApplicationService guildApplicationService;
    private final GuildApplicationFormService guildApplicationFormService;
    private final GuildEventService guildEventService;
    private final GuildPollService guildPollService;
    private final GuildPollItemService guildPollItemService;
    private final GuildServerService guildServerService;
    private final GuildServerProfileService guildServerProfileService;
    private final ModerationLogItemService modLogService;
    private final ServerLogItemService serverLogService;
    private final MuteService muteService;
    private final PunishmentService punishmentService;
    private final ScheduledAnnouncementService scheduledAnnouncementService;
    private final TempBanService tempBanService;
    private final WelcomeMessageService welcomeMessageService;
    private final GuildServerStrikeService strikeService;

    public ServiceManager(ApplicationProperties applicationProperties,
                          AutoModAntiDupQueryService autoModAntiDupQueryService,
                          AutoModAutoRaidService autoModAutoRaidService,
                          AutoModerationService autoModerationService,
                          AutoModIgnoreService autoModIgnoreService,
                          AutoModMentionsService autoModMentionsService,
                          DiscordUserProfileService discordUserProfileService,
                          DiscordUserService discordUserService,
                          GiveAwayService giveAwayService,
                          GuildApplicationService guildApplicationService,
                          GuildApplicationFormService guildApplicationFormService,
                          GuildEventService guildEventService,
                          GuildPollService guildPollService,
                          GuildPollItemService guildPollItemService,
                          GuildServerService guildServerService,
                          GuildServerProfileService guildServerProfileService,
                          ModerationLogItemService modLogService,
                          ServerLogItemService serverLogService,
                          MuteService muteService,
                          PunishmentService punishmentService,
                          ScheduledAnnouncementService scheduledAnnouncementService,
                          TempBanService tempBanService,
                          WelcomeMessageService welcomeMessageService,
                          GuildServerStrikeService strikeService) {
        this.applicationProperties = applicationProperties;
        this.autoModAntiDupQueryService = autoModAntiDupQueryService;
        this.autoModAutoRaidService = autoModAutoRaidService;
        this.autoModerationService = autoModerationService;
        this.autoModIgnoreService = autoModIgnoreService;
        this.autoModMentionsService = autoModMentionsService;
        this.discordUserProfileService = discordUserProfileService;
        this.discordUserService = discordUserService;
        this.giveAwayService = giveAwayService;
        this.guildApplicationService = guildApplicationService;
        this.guildApplicationFormService = guildApplicationFormService;
        this.guildEventService = guildEventService;
        this.guildPollService = guildPollService;
        this.guildPollItemService = guildPollItemService;
        this.guildServerService = guildServerService;
        this.guildServerProfileService = guildServerProfileService;
        this.modLogService = modLogService;
        this.serverLogService = serverLogService;
        this.muteService = muteService;
        this.punishmentService = punishmentService;
        this.scheduledAnnouncementService = scheduledAnnouncementService;
        this.tempBanService = tempBanService;
        this.welcomeMessageService = welcomeMessageService;
        this.strikeService = strikeService;
    }

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    public AutoModAntiDupQueryService getAutoModAntiDupQueryService() {
        return autoModAntiDupQueryService;
    }

    public AutoModAutoRaidService getAutoModAutoRaidService() {
        return autoModAutoRaidService;
    }

    public AutoModerationService getAutoModerationService() {
        return autoModerationService;
    }

    public AutoModIgnoreService getAutoModIgnoreService() {
        return autoModIgnoreService;
    }

    public AutoModMentionsService getAutoModMentionsService() {
        return autoModMentionsService;
    }

    public DiscordUserProfileService getDiscordUserProfileService() {
        return discordUserProfileService;
    }

    public DiscordUserService getDiscordUserService() {
        return discordUserService;
    }

    public GiveAwayService getGiveAwayService() {
        return giveAwayService;
    }

    public GuildApplicationService getGuildApplicationService() {
        return guildApplicationService;
    }

    public GuildApplicationFormService getGuildApplicationFormService() {
        return guildApplicationFormService;
    }

    public GuildEventService getGuildEventService() {
        return guildEventService;
    }

    public GuildPollService getGuildPollService() {
        return guildPollService;
    }

    public GuildPollItemService getGuildPollItemService() {
        return guildPollItemService;
    }

    public GuildServerService getGuildServerService() {
        return guildServerService;
    }

    public GuildServerProfileService getGuildServerProfileService() {
        return guildServerProfileService;
    }

    public ModerationLogItemService getModLogService() {
        return modLogService;
    }

    public ServerLogItemService getServerLogService() {
        return serverLogService;
    }

    public MuteService getMuteService() {
        return muteService;
    }

    public PunishmentService getPunishmentService() {
        return punishmentService;
    }

    public ScheduledAnnouncementService getScheduledAnnouncementService() {
        return scheduledAnnouncementService;
    }

    public TempBanService getTempBanService() {
        return tempBanService;
    }

    public WelcomeMessageService getWelcomeMessageService() {
        return welcomeMessageService;
    }

    public GuildServerStrikeService getStrikeService() {
        return strikeService;
    }
}
