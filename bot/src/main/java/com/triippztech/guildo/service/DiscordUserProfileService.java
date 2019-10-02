package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.DiscordUserProfile;
import com.triippztech.guildo.repository.DiscordUserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link DiscordUserProfile}.
 */
@Service
@Transactional
public class DiscordUserProfileService {

    private final Logger log = LoggerFactory.getLogger(DiscordUserProfileService.class);

    private final DiscordUserProfileRepository discordUserProfileRepository;

    public DiscordUserProfileService(DiscordUserProfileRepository discordUserProfileRepository) {
        this.discordUserProfileRepository = discordUserProfileRepository;
    }

    /**
     * Save a discordUserProfile.
     *
     * @param discordUserProfile the entity to save.
     * @return the persisted entity.
     */
    public DiscordUserProfile save(DiscordUserProfile discordUserProfile) {
        log.debug("Request to save DiscordUserProfile : {}", discordUserProfile);
        return discordUserProfileRepository.save(discordUserProfile);
    }

    /**
     * Get all the discordUserProfiles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DiscordUserProfile> findAll() {
        log.debug("Request to get all DiscordUserProfiles");
        return discordUserProfileRepository.findAll();
    }


    /**
     * Get one discordUserProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DiscordUserProfile> findOne(Long id) {
        log.debug("Request to get DiscordUserProfile : {}", id);
        return discordUserProfileRepository.findById(id);
    }

    /**
     * Delete the discordUserProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DiscordUserProfile : {}", id);
        discordUserProfileRepository.deleteById(id);
    }
}
