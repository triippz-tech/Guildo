package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.Punishment;
import com.triippztech.guildo.repository.PunishmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Punishment}.
 */
@Service
@Transactional
public class PunishmentService {

    private final Logger log = LoggerFactory.getLogger(PunishmentService.class);

    private final PunishmentRepository punishmentRepository;

    public PunishmentService(PunishmentRepository punishmentRepository) {
        this.punishmentRepository = punishmentRepository;
    }

    /**
     * Save a punishment.
     *
     * @param punishment the entity to save.
     * @return the persisted entity.
     */
    public Punishment save(Punishment punishment) {
        log.debug("Request to save Punishment : {}", punishment);
        return punishmentRepository.save(punishment);
    }

    /**
     * Get all the punishments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Punishment> findAll() {
        log.debug("Request to get all Punishments");
        return punishmentRepository.findAll();
    }


    /**
     * Get one punishment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Punishment> findOne(Long id) {
        log.debug("Request to get Punishment : {}", id);
        return punishmentRepository.findById(id);
    }

    /**
     * Delete the punishment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Punishment : {}", id);
        punishmentRepository.deleteById(id);
    }
}
