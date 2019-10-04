package com.triippztech.guildo.service.moderation;

import com.triippztech.guildo.domain.Mute;
import com.triippztech.guildo.repository.MuteRepository;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Mute}.
 */
@Service
@Transactional
public class MuteService {

    private final Logger log = LoggerFactory.getLogger(MuteService.class);

    private final MuteRepository muteRepository;

    public MuteService(MuteRepository muteRepository) {
        this.muteRepository = muteRepository;
    }

    /**
     * Save a mute.
     *
     * @param mute the entity to save.
     * @return the persisted entity.
     */
    public Mute save(Mute mute) {
        log.debug("Request to save Mute : {}", mute);
        return muteRepository.save(mute);
    }

    /**
     * Get all the mutes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Mute> findAll() {
        log.debug("Request to get all Mutes");
        return muteRepository.findAll();
    }


    /**
     * Get one mute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Mute> findOne(Long id) {
        log.debug("Request to get Mute : {}", id);
        return muteRepository.findById(id);
    }

    /**
     * Delete the mute by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Mute : {}", id);
        muteRepository.deleteById(id);
    }

    public int timeUntilUnmute(Guild guild, long userId)
    {
        log.debug("Request to view timeleft on mute for User={} in Guild={}", userId, guild.getName());
        Optional<Mute> tempMutesOptional = muteRepository.findByUserIdAndGuildId(userId, guild.getIdLong());
        if ( tempMutesOptional.isPresent() )
        {
            Instant end = tempMutesOptional.get().getEndTime();
            if(end.getEpochSecond() == Instant.MAX.getEpochSecond())
                return Integer.MAX_VALUE;
            else
                return (int)(Instant.now().until(end, ChronoUnit.MINUTES));
        } else {
            log.warn("User={} in Guild={} is not muted", userId, guild.getName());
            return 0;
        }
    }
}
