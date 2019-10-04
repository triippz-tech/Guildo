package com.triippztech.guildo.service.guild;

import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServerStrike;
import com.triippztech.guildo.repository.GuildServerStrikeRepository;
import com.triippztech.guildo.repository.DiscordUserRepository;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GuildServerStrike}.
 */
@Service
@Transactional
public class GuildServerStrikeService {

    private final Logger log = LoggerFactory.getLogger(GuildServerStrikeService.class);

    private final GuildServerStrikeRepository guildServerStrikeRepository;

    private final DiscordUserRepository discordUserRepository;

    public GuildServerStrikeService(GuildServerStrikeRepository guildServerStrikeRepository, DiscordUserRepository discordUserRepository) {
        this.guildServerStrikeRepository = guildServerStrikeRepository;
        this.discordUserRepository = discordUserRepository;
    }

    /**
     * Save a guildServerStrike.
     *
     * @param guildServerStrike the entity to save.
     * @return the persisted entity.
     */
    public GuildServerStrike save(GuildServerStrike guildServerStrike) {
        log.debug("Request to save GuildServerStrike : {}", guildServerStrike);
        long discordUserId = guildServerStrike.getDiscordUser().getId();
        discordUserRepository.findById(discordUserId).ifPresent(guildServerStrike::discordUser);
        return guildServerStrikeRepository.save(guildServerStrike);
    }

    /**
     * Get all the guildServerStrikes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildServerStrike> findAll(Pageable pageable) {
        log.debug("Request to get all GuildServerStrikes");
        return guildServerStrikeRepository.findAll(pageable);
    }


    /**
     * Get one guildServerStrike by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GuildServerStrike> findOne(Long id) {
        log.debug("Request to get GuildServerStrike : {}", id);
        return guildServerStrikeRepository.findById(id);
    }

    /**
     * Delete the guildServerStrike by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GuildServerStrike : {}", id);
        guildServerStrikeRepository.deleteById(id);
    }

    public GuildServerStrike createUserStrikeForGuild(DiscordUser discordUser, Long guildId) {
        log.debug("Request to create new GuildServerStrike entity for User:{} in Guild:{}", discordUser, guildId);
        GuildServerStrike userStrike = new GuildServerStrike();
        userStrike.setCount(0);
        userStrike.setDiscordUser(discordUser);
        userStrike.setUserId(discordUser.getUserId());
        userStrike.setGuildId(guildId);
        save(userStrike);
        return userStrike;
    }

    public GuildServerStrike createUserStrikeForGuild(DiscordUser discordUser, Guild guild) {
        return createUserStrikeForGuild(discordUser, guild.getIdLong());
    }

    public GuildServerStrike getUserStrike(DiscordUser discordUser, Long guildId) {
        Optional<GuildServerStrike> userStrike =
            guildServerStrikeRepository.findByUserIdAndGuildId(discordUser.getUserId(), guildId);
        if ( userStrike.isEmpty() ) return createUserStrikeForGuild(discordUser, guildId);
        return userStrike.get();
    }

    public GuildServerStrike getUserStrike(DiscordUser discordUser, Guild guild) {
        Optional<GuildServerStrike> userStrike =
            guildServerStrikeRepository.findByUserIdAndGuildId(discordUser.getUserId(), guild.getIdLong());
        if ( userStrike.isEmpty() ) return createUserStrikeForGuild(discordUser, guild);
        return userStrike.get();
    }

    public GuildServerStrike addStrike(DiscordUser discordUser, Guild guild, Integer count) {
        log.debug("Request to add Strike to discord user: {} in guild {}", discordUser, guild.getName());
        GuildServerStrike userStrike = getUserStrike(discordUser, guild);

        Integer strikes = userStrike.getCount();
        userStrike.setCount( strikes + count );
        save(userStrike);
        return userStrike;
    }

    public GuildServerStrike addStrike(DiscordUser discordUser, Guild guild) {
        return addStrike(discordUser, guild, 1);
    }

    public GuildServerStrike removeStrike(DiscordUser discordUser, Guild guild, Integer count) {
        GuildServerStrike userStrike = getUserStrike(discordUser, guild);

        int newCount = userStrike.getCount() - count;
        if ( newCount < 0 ) {
            userStrike.setCount(0);
            save(userStrike);
            return userStrike;
        }

        userStrike.setCount(newCount);
        save(userStrike);
        return userStrike;
    }

    public GuildServerStrike removeStrike(DiscordUser discordUser, Guild guild) {
        return removeStrike(discordUser, guild, 1);
    }

    public Integer getUserStrikeCount(DiscordUser discordUser, Guild guild) {
        GuildServerStrike userStrike = getUserStrike(discordUser, guild);
        return userStrike.getCount();
    }

    public List<GuildServerStrike> getAllStrikesForGuild(Guild guild) {
        return guildServerStrikeRepository.findAllByGuildId(guild.getIdLong());
    }
}
