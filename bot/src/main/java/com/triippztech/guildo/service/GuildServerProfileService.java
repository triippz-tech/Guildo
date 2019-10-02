package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.GuildServerProfile;
import com.triippztech.guildo.repository.GuildServerProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GuildServerProfile}.
 */
@Service
@Transactional
public class GuildServerProfileService {

    private final Logger log = LoggerFactory.getLogger(GuildServerProfileService.class);

    private final GuildServerProfileRepository guildServerProfileRepository;

    public GuildServerProfileService(GuildServerProfileRepository guildServerProfileRepository) {
        this.guildServerProfileRepository = guildServerProfileRepository;
    }

    /**
     * Save a guildServerProfile.
     *
     * @param guildServerProfile the entity to save.
     * @return the persisted entity.
     */
    public GuildServerProfile save(GuildServerProfile guildServerProfile) {
        log.debug("Request to save GuildServerProfile : {}", guildServerProfile);
        return guildServerProfileRepository.save(guildServerProfile);
    }

    /**
     * Get all the guildServerProfiles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GuildServerProfile> findAll() {
        log.debug("Request to get all GuildServerProfiles");
        return guildServerProfileRepository.findAll();
    }


    /**
     * Get one guildServerProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GuildServerProfile> findOne(Long id) {
        log.debug("Request to get GuildServerProfile : {}", id);
        return guildServerProfileRepository.findById(id);
    }

    /**
     * Delete the guildServerProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GuildServerProfile : {}", id);
        guildServerProfileRepository.deleteById(id);
    }
}
