package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.GiveAway;
import com.triippztech.guildo.repository.GiveAwayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GiveAway}.
 */
@Service
@Transactional
public class GiveAwayService {

    private final Logger log = LoggerFactory.getLogger(GiveAwayService.class);

    private final GiveAwayRepository giveAwayRepository;

    public GiveAwayService(GiveAwayRepository giveAwayRepository) {
        this.giveAwayRepository = giveAwayRepository;
    }

    /**
     * Save a giveAway.
     *
     * @param giveAway the entity to save.
     * @return the persisted entity.
     */
    public GiveAway save(GiveAway giveAway) {
        log.debug("Request to save GiveAway : {}", giveAway);
        return giveAwayRepository.save(giveAway);
    }

    /**
     * Get all the giveAways.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GiveAway> findAll() {
        log.debug("Request to get all GiveAways");
        return giveAwayRepository.findAll();
    }


    /**
     * Get one giveAway by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GiveAway> findOne(Long id) {
        log.debug("Request to get GiveAway : {}", id);
        return giveAwayRepository.findById(id);
    }

    /**
     * Delete the giveAway by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GiveAway : {}", id);
        giveAwayRepository.deleteById(id);
    }
}
