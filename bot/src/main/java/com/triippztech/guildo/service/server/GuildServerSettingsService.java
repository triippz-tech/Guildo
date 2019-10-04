package com.triippztech.guildo.service.server;

import com.triippztech.guildo.domain.GuildServerSettings;
import com.triippztech.guildo.repository.GuildServerSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GuildServerSettings}.
 */
@Service
@Transactional
public class GuildServerSettingsService {

    private final Logger log = LoggerFactory.getLogger(GuildServerSettingsService.class);

    private final GuildServerSettingsRepository guildServerSettingsRepository;

    public GuildServerSettingsService(GuildServerSettingsRepository guildServerSettingsRepository) {
        this.guildServerSettingsRepository = guildServerSettingsRepository;
    }

    /**
     * Save a guildServerSettings.
     *
     * @param guildServerSettings the entity to save.
     * @return the persisted entity.
     */
    public GuildServerSettings save(GuildServerSettings guildServerSettings) {
        log.debug("Request to save GuildServerSettings : {}", guildServerSettings);
        return guildServerSettingsRepository.save(guildServerSettings);
    }

    /**
     * Get all the guildServerSettings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GuildServerSettings> findAll() {
        log.debug("Request to get all GuildServerSettings");
        return guildServerSettingsRepository.findAll();
    }


    /**
     * Get one guildServerSettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GuildServerSettings> findOne(Long id) {
        log.debug("Request to get GuildServerSettings : {}", id);
        return guildServerSettingsRepository.findById(id);
    }

    /**
     * Delete the guildServerSettings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GuildServerSettings : {}", id);
        guildServerSettingsRepository.deleteById(id);
    }
}
