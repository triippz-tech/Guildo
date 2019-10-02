package com.triippztech.guildo.service;

import com.triippztech.guildo.config.ApplicationProperties;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.DiscordUserProfile;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.domain.TempBan;
import com.triippztech.guildo.domain.enumeration.PunishmentType;
import com.triippztech.guildo.domain.enumeration.ReturnStatus;
import com.triippztech.guildo.repository.DiscordUserRepository;
import com.triippztech.guildo.utils.Pair;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * Service Implementation for managing {@link DiscordUser}.
 */
@Service
@Transactional
public class DiscordUserService {

    private final Logger log = LoggerFactory.getLogger(DiscordUserService.class);

    private final DiscordUserRepository discordUserRepository;
    private final DiscordUserProfileService discordUserProfileService;
    private final ApplicationProperties applicationProperties;
    private final TempBanService tempBanService;
    private final GuildoBotService guildoService;

    public DiscordUserService(DiscordUserRepository discordUserRepository,
                              DiscordUserProfileService discordUserProfileService,
                              ApplicationProperties applicationProperties,
                              TempBanService tempBanService,
                              @Lazy GuildoBotService guildoService) {
        this.discordUserRepository = discordUserRepository;
        this.discordUserProfileService = discordUserProfileService;
        this.applicationProperties = applicationProperties;
        this.tempBanService = tempBanService;
        this.guildoService = guildoService;
    }

    /**
     * Save a discordUser.
     *
     * @param discordUser the entity to save.
     * @return the persisted entity.
     */
    public DiscordUser save(DiscordUser discordUser) {
        log.debug("Request to save DiscordUser : {}", discordUser);
        return discordUserRepository.save(discordUser);
    }

    /**
     * Get all the discordUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DiscordUser> findAll(Pageable pageable) {
        log.debug("Request to get all DiscordUsers");
        return discordUserRepository.findAll(pageable);
    }


    /**
     * Get one discordUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DiscordUser> findOne(Long id) {
        log.debug("Request to get DiscordUser : {}", id);
        return discordUserRepository.findById(id);
    }

    /**
     * Delete the discordUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DiscordUser : {}", id);
        discordUserRepository.deleteById(id);
    }

    public Pair<Boolean, DiscordUser> userExists(Long userId) {
        Optional<DiscordUser> discordUser = findByUserId(userId);
        return discordUser.map(user -> new Pair<>(true, user)).orElseGet(() -> new Pair<>(false, null));
    }

    public Boolean userExists2(Long userId) {
        Optional<DiscordUser> discordUser = findByUserId(userId);
        return discordUser.isPresent();
    }

    public DiscordUser getDiscordUser(User user) {
        log.debug("Request to get DiscordUser: {}", user);
        Optional<DiscordUser> discordUser = findByUserId(user.getIdLong());
        return discordUser.orElseGet(() -> createDiscordUser(user));
    }

    public DiscordUser getDiscordUser(Long userId) {
        log.debug("Request to get DiscordUser: {}", userId);
        Optional<DiscordUser> discordUser = findByUserId(userId);
        return discordUser.orElseGet(() -> createDiscordUser( userId ));
    }

    public DiscordUser createDiscordUser(User user) {
        log.debug("Request to create new DiscordUser for: {}", user.getName());
        Pair<Boolean, DiscordUser> userPair = userExists(user.getIdLong());
        if ( userPair.getKey() ) return userPair.getValue();

        DiscordUserProfile userProfile = new DiscordUserProfile();
        discordUserProfileService.save(userProfile);
        DiscordUser discordUser = new DiscordUser(user.getIdLong(), user.getName(), user.getAvatarUrl(), userProfile);
        save(discordUser);
        return discordUser;
    }

    public DiscordUser createDiscordUser(Long userId) {
        User user = guildoService.getShardManager().getUserById(userId);
        return createDiscordUser(Objects.requireNonNull(user));
    }

    public Boolean isBlacklisted(User user) {
        log.debug("Request to check if Discord User: {} is blacklisted", user.getName());
        Optional<DiscordUser> discordUser = discordUserRepository
            .findByUserIdAndAndBlacklisted(user.getIdLong(), true);
        return discordUser.isPresent();
    }

    public Optional<DiscordUser> findByUserId(Long userId) {
        log.debug("Request to find DiscordUser by ID: {}", userId);
        return discordUserRepository.findByUserId(userId);
    }

    public DiscordUser blackListUser(User user) {
        log.debug("Request to Blacklist DiscordUser: {}", user);
        DiscordUser discordUser = getDiscordUser(user);
        discordUser.setBlacklisted(!discordUser.isBlacklisted());
        save(discordUser);
        return discordUser;
    }

    public boolean isOwner(User user) {
        return user.getIdLong() == Long.parseLong(applicationProperties.getDiscord().getAuthorId());
    }

    public void addCommand(User user) {
        DiscordUser discordUser = getDiscordUser( user );
        Integer issued = discordUser.getCommandsIssued() + 1;
        discordUser.setCommandsIssued(issued);
        save(discordUser);
    }

//    public Pair<ReturnStatus, String> addTempBan(GuildServer guildServer, Member moderator, User user, PunishmentType punishmentType,
//                                                 Integer time, String reason, Long channelId, String channelName) {
//        DiscordUser discordUser = getDiscordUser( user );
//        Pair<ReturnStatus, TempBan> tempBanPair = tempBanService.addTempBan(
//              guildServer
//            , moderator
//            , discordUser
//            , punishmentType
//            , time
//            , reason
//            , channelId
//            , channelName);
//        if (tempBanPair.getKey().equals(ReturnStatus.WARNING))
//            return new Pair<>(ReturnStatus.WARNING, discordUser.getUserName() + " has already been banned");
//
//        discordUser.addUserTempBans(tempBanPair.getValue());
//        save(discordUser);
//
//        return new Pair<>(ReturnStatus.SUCCESS, discordUser.getUserName()
//            + " has been successfully banned from "
//            + moderator.getGuild().getName());
//    }
}
