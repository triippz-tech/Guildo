package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.AutoModIgnore;
import com.triippztech.guildo.repository.AutoModIgnoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AutoModIgnore}.
 */
@Service
@Transactional
public class AutoModIgnoreService {

    private final Logger log = LoggerFactory.getLogger(AutoModIgnoreService.class);

    private final AutoModIgnoreRepository autoModIgnoreRepository;

    public AutoModIgnoreService(AutoModIgnoreRepository autoModIgnoreRepository) {
        this.autoModIgnoreRepository = autoModIgnoreRepository;
    }

    /**
     * Save a autoModIgnore.
     *
     * @param autoModIgnore the entity to save.
     * @return the persisted entity.
     */
    public AutoModIgnore save(AutoModIgnore autoModIgnore) {
        log.debug("Request to save AutoModIgnore : {}", autoModIgnore);
        return autoModIgnoreRepository.save(autoModIgnore);
    }

    /**
     * Get all the autoModIgnores.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AutoModIgnore> findAll() {
        log.debug("Request to get all AutoModIgnores");
        return autoModIgnoreRepository.findAll();
    }


    /**
     * Get one autoModIgnore by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AutoModIgnore> findOne(Long id) {
        log.debug("Request to get AutoModIgnore : {}", id);
        return autoModIgnoreRepository.findById(id);
    }

    /**
     * Delete the autoModIgnore by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AutoModIgnore : {}", id);
        autoModIgnoreRepository.deleteById(id);
    }
}
