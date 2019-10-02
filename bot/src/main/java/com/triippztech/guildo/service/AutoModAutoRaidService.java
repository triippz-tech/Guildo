package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.AutoModAutoRaid;
import com.triippztech.guildo.repository.AutoModAutoRaidRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AutoModAutoRaid}.
 */
@Service
@Transactional
public class AutoModAutoRaidService {

    private final Logger log = LoggerFactory.getLogger(AutoModAutoRaidService.class);

    private final AutoModAutoRaidRepository autoModAutoRaidRepository;

    public AutoModAutoRaidService(AutoModAutoRaidRepository autoModAutoRaidRepository) {
        this.autoModAutoRaidRepository = autoModAutoRaidRepository;
    }

    /**
     * Save a autoModAutoRaid.
     *
     * @param autoModAutoRaid the entity to save.
     * @return the persisted entity.
     */
    public AutoModAutoRaid save(AutoModAutoRaid autoModAutoRaid) {
        log.debug("Request to save AutoModAutoRaid : {}", autoModAutoRaid);
        return autoModAutoRaidRepository.save(autoModAutoRaid);
    }

    /**
     * Get all the autoModAutoRaids.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AutoModAutoRaid> findAll() {
        log.debug("Request to get all AutoModAutoRaids");
        return autoModAutoRaidRepository.findAll();
    }


    /**
     * Get one autoModAutoRaid by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AutoModAutoRaid> findOne(Long id) {
        log.debug("Request to get AutoModAutoRaid : {}", id);
        return autoModAutoRaidRepository.findById(id);
    }

    /**
     * Delete the autoModAutoRaid by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AutoModAutoRaid : {}", id);
        autoModAutoRaidRepository.deleteById(id);
    }
}
