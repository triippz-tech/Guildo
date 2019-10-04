package com.triippztech.guildo.service.guild;

import com.triippztech.guildo.domain.ScheduledAnnouncement;
import com.triippztech.guildo.repository.ScheduledAnnouncementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ScheduledAnnouncement}.
 */
@Service
@Transactional
public class ScheduledAnnouncementService {

    private final Logger log = LoggerFactory.getLogger(ScheduledAnnouncementService.class);

    private final ScheduledAnnouncementRepository scheduledAnnouncementRepository;

    public ScheduledAnnouncementService(ScheduledAnnouncementRepository scheduledAnnouncementRepository) {
        this.scheduledAnnouncementRepository = scheduledAnnouncementRepository;
    }

    /**
     * Save a scheduledAnnouncement.
     *
     * @param scheduledAnnouncement the entity to save.
     * @return the persisted entity.
     */
    public ScheduledAnnouncement save(ScheduledAnnouncement scheduledAnnouncement) {
        log.debug("Request to save ScheduledAnnouncement : {}", scheduledAnnouncement);
        return scheduledAnnouncementRepository.save(scheduledAnnouncement);
    }

    /**
     * Get all the scheduledAnnouncements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ScheduledAnnouncement> findAll() {
        log.debug("Request to get all ScheduledAnnouncements");
        return scheduledAnnouncementRepository.findAll();
    }


    /**
     * Get one scheduledAnnouncement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ScheduledAnnouncement> findOne(Long id) {
        log.debug("Request to get ScheduledAnnouncement : {}", id);
        return scheduledAnnouncementRepository.findById(id);
    }

    /**
     * Delete the scheduledAnnouncement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ScheduledAnnouncement : {}", id);
        scheduledAnnouncementRepository.deleteById(id);
    }
}
