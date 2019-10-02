package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.GuildApplication;
import com.triippztech.guildo.repository.GuildApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GuildApplication}.
 */
@Service
@Transactional
public class GuildApplicationService {

    private final Logger log = LoggerFactory.getLogger(GuildApplicationService.class);

    private final GuildApplicationRepository guildApplicationRepository;

    public GuildApplicationService(GuildApplicationRepository guildApplicationRepository) {
        this.guildApplicationRepository = guildApplicationRepository;
    }

    /**
     * Save a guildApplication.
     *
     * @param guildApplication the entity to save.
     * @return the persisted entity.
     */
    public GuildApplication save(GuildApplication guildApplication) {
        log.debug("Request to save GuildApplication : {}", guildApplication);
        return guildApplicationRepository.save(guildApplication);
    }

    /**
     * Get all the guildApplications.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GuildApplication> findAll() {
        log.debug("Request to get all GuildApplications");
        return guildApplicationRepository.findAll();
    }


    /**
     * Get one guildApplication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GuildApplication> findOne(Long id) {
        log.debug("Request to get GuildApplication : {}", id);
        return guildApplicationRepository.findById(id);
    }

    /**
     * Delete the guildApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GuildApplication : {}", id);
        guildApplicationRepository.deleteById(id);
    }
}
