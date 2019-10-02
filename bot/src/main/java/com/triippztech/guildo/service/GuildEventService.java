package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.GuildEvent;
import com.triippztech.guildo.repository.GuildEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GuildEvent}.
 */
@Service
@Transactional
public class GuildEventService {

    private final Logger log = LoggerFactory.getLogger(GuildEventService.class);

    private final GuildEventRepository guildEventRepository;

    public GuildEventService(GuildEventRepository guildEventRepository) {
        this.guildEventRepository = guildEventRepository;
    }

    /**
     * Save a guildEvent.
     *
     * @param guildEvent the entity to save.
     * @return the persisted entity.
     */
    public GuildEvent save(GuildEvent guildEvent) {
        log.debug("Request to save GuildEvent : {}", guildEvent);
        return guildEventRepository.save(guildEvent);
    }

    /**
     * Get all the guildEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GuildEvent> findAll() {
        log.debug("Request to get all GuildEvents");
        return guildEventRepository.findAll();
    }


    /**
     * Get one guildEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GuildEvent> findOne(Long id) {
        log.debug("Request to get GuildEvent : {}", id);
        return guildEventRepository.findById(id);
    }

    /**
     * Delete the guildEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GuildEvent : {}", id);
        guildEventRepository.deleteById(id);
    }
}
