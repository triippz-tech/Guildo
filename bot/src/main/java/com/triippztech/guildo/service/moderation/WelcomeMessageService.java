package com.triippztech.guildo.service.moderation;

import com.triippztech.guildo.domain.WelcomeMessage;
import com.triippztech.guildo.repository.WelcomeMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link WelcomeMessage}.
 */
@Service
@Transactional
public class WelcomeMessageService {

    private final Logger log = LoggerFactory.getLogger(WelcomeMessageService.class);

    private final WelcomeMessageRepository welcomeMessageRepository;

    public WelcomeMessageService(WelcomeMessageRepository welcomeMessageRepository) {
        this.welcomeMessageRepository = welcomeMessageRepository;
    }

    /**
     * Save a welcomeMessage.
     *
     * @param welcomeMessage the entity to save.
     * @return the persisted entity.
     */
    public WelcomeMessage save(WelcomeMessage welcomeMessage) {
        log.debug("Request to save WelcomeMessage : {}", welcomeMessage);
        return welcomeMessageRepository.save(welcomeMessage);
    }

    /**
     * Get all the welcomeMessages.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WelcomeMessage> findAll() {
        log.debug("Request to get all WelcomeMessages");
        return welcomeMessageRepository.findAll();
    }


    /**
     * Get one welcomeMessage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WelcomeMessage> findOne(Long id) {
        log.debug("Request to get WelcomeMessage : {}", id);
        return welcomeMessageRepository.findById(id);
    }

    /**
     * Delete the welcomeMessage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WelcomeMessage : {}", id);
        welcomeMessageRepository.deleteById(id);
    }
}
