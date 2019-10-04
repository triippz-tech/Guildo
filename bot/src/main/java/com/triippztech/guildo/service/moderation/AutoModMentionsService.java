package com.triippztech.guildo.service.moderation;

import com.triippztech.guildo.domain.AutoModMentions;
import com.triippztech.guildo.repository.AutoModMentionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AutoModMentions}.
 */
@Service
@Transactional
public class AutoModMentionsService {

    private final Logger log = LoggerFactory.getLogger(AutoModMentionsService.class);

    private final AutoModMentionsRepository autoModMentionsRepository;

    public AutoModMentionsService(AutoModMentionsRepository autoModMentionsRepository) {
        this.autoModMentionsRepository = autoModMentionsRepository;
    }

    /**
     * Save a autoModMentions.
     *
     * @param autoModMentions the entity to save.
     * @return the persisted entity.
     */
    public AutoModMentions save(AutoModMentions autoModMentions) {
        log.debug("Request to save AutoModMentions : {}", autoModMentions);
        return autoModMentionsRepository.save(autoModMentions);
    }

    /**
     * Get all the autoModMentions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AutoModMentions> findAll() {
        log.debug("Request to get all AutoModMentions");
        return autoModMentionsRepository.findAll();
    }


    /**
     * Get one autoModMentions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AutoModMentions> findOne(Long id) {
        log.debug("Request to get AutoModMentions : {}", id);
        return autoModMentionsRepository.findById(id);
    }

    /**
     * Delete the autoModMentions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AutoModMentions : {}", id);
        autoModMentionsRepository.deleteById(id);
    }
}
