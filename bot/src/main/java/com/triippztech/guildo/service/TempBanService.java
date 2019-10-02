package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.domain.ModerationLogItem;
import com.triippztech.guildo.domain.TempBan;
import com.triippztech.guildo.domain.enumeration.PunishmentType;
import com.triippztech.guildo.domain.enumeration.ReturnStatus;
import com.triippztech.guildo.repository.TempBanRepository;
import com.triippztech.guildo.utils.Pair;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link TempBan}.
 */
@Service
@Transactional
public class TempBanService {

    private final Logger log = LoggerFactory.getLogger(TempBanService.class);

    private final TempBanRepository tempBanRepository;

    public TempBanService(TempBanRepository tempBanRepository ) {
        this.tempBanRepository = tempBanRepository;
    }

    /**
     * Save a tempBan.
     *
     * @param tempBan the entity to save.
     * @return the persisted entity.
     */
    public TempBan save(TempBan tempBan) {
        log.debug("Request to save TempBan : {}", tempBan);
        return tempBanRepository.save(tempBan);
    }

    /**
     * Get all the tempBans.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TempBan> findAll() {
        log.debug("Request to get all TempBans");
        return tempBanRepository.findAll();
    }


    /**
     * Get one tempBan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TempBan> findOne(Long id) {
        log.debug("Request to get TempBan : {}", id);
        return tempBanRepository.findById(id);
    }

    /**
     * Delete the tempBan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TempBan : {}", id);
        tempBanRepository.deleteById(id);
    }

    public boolean userAlreadyBanned(DiscordUser discordUser, Guild guild) {
        log.debug("Request to check is User: {} is already banned on Server: {}", discordUser, guild.getName());
        if ( discordUser.getUserTempBans().isEmpty() ) return false;

        for (TempBan tempBan : discordUser.getUserTempBans())
            if ( tempBan.getGuildId() == guild.getIdLong() )
                return true;
        return false;
    }

    public Pair<ReturnStatus, TempBan> addTempBan(GuildServer guildServer, Member moderator,
                                                  DiscordUser discordUser, Integer time,
                                                  String reason ) {
        log.debug("Request to add TempBan to User: {}", discordUser.getUserName());

        if ( userAlreadyBanned( discordUser, moderator.getGuild() ) )
            return new Pair<>(ReturnStatus.WARNING, null);

        TempBan tempBan = new TempBan();
        tempBan.setEndTime( getTime(time) );
        tempBan.setReason(reason);
        tempBan.setGuildId(moderator.getGuild().getIdLong());
        tempBan.setTempBanGuildServer(guildServer);
        tempBan.setBannedUser(discordUser);
        tempBan.setUserId(discordUser.getUserId());
        save(tempBan);

        return new Pair<>(ReturnStatus.SUCCESS, tempBan);
    }

    private Instant getTime(Integer time)
    {
        if (time == null) return null;
        Instant punishTime = Instant.now();
        return punishTime.plus(time, ChronoUnit.SECONDS);
    }

    public Optional<TempBan> findByUserAndGuild(Long userId, Guild guild) {
        return tempBanRepository.findByUserIdAndGuildId(userId, guild.getIdLong());
    }

}
