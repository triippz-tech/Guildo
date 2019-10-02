package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.AutoModAntiDup;
import com.triippztech.guildo.repository.AutoModAntiDupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AutoModAntiDup}.
 */
@Service
@Transactional
public class AutoModAntiDupService {

    private final Logger log = LoggerFactory.getLogger(AutoModAntiDupService.class);

    private final AutoModAntiDupRepository autoModAntiDupRepository;

    public AutoModAntiDupService(AutoModAntiDupRepository autoModAntiDupRepository) {
        this.autoModAntiDupRepository = autoModAntiDupRepository;
    }

    /**
     * Save a autoModAntiDup.
     *
     * @param autoModAntiDup the entity to save.
     * @return the persisted entity.
     */
    public AutoModAntiDup save(AutoModAntiDup autoModAntiDup) {
        log.debug("Request to save AutoModAntiDup : {}", autoModAntiDup);
        return autoModAntiDupRepository.save(autoModAntiDup);
    }

    /**
     * Get all the autoModAntiDups.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AutoModAntiDup> findAll() {
        log.debug("Request to get all AutoModAntiDups");
        return autoModAntiDupRepository.findAll();
    }


    /**
     * Get one autoModAntiDup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AutoModAntiDup> findOne(Long id) {
        log.debug("Request to get AutoModAntiDup : {}", id);
        return autoModAntiDupRepository.findById(id);
    }

    /**
     * Delete the autoModAntiDup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AutoModAntiDup : {}", id);
        autoModAntiDupRepository.deleteById(id);
    }
}
