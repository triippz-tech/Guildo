package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.AutoModeration;
import com.triippztech.guildo.repository.AutoModerationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AutoModeration}.
 */
@Service
@Transactional
public class AutoModerationService {

    private final Logger log = LoggerFactory.getLogger(AutoModerationService.class);

    private final AutoModerationRepository autoModerationRepository;

    public AutoModerationService(AutoModerationRepository autoModerationRepository) {
        this.autoModerationRepository = autoModerationRepository;
    }

    /**
     * Save a autoModeration.
     *
     * @param autoModeration the entity to save.
     * @return the persisted entity.
     */
    public AutoModeration save(AutoModeration autoModeration) {
        log.debug("Request to save AutoModeration : {}", autoModeration);
        return autoModerationRepository.save(autoModeration);
    }

    /**
     * Get all the autoModerations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AutoModeration> findAll() {
        log.debug("Request to get all AutoModerations");
        return autoModerationRepository.findAll();
    }


    /**
     * Get one autoModeration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AutoModeration> findOne(Long id) {
        log.debug("Request to get AutoModeration : {}", id);
        return autoModerationRepository.findById(id);
    }

    /**
     * Delete the autoModeration by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AutoModeration : {}", id);
        autoModerationRepository.deleteById(id);
    }
}
