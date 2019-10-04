package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.ScheduledAnnouncement;
import com.triippztech.guildo.service.guild.ScheduledAnnouncementService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.ScheduledAnnouncementCriteria;
import com.triippztech.guildo.service.guild.ScheduledAnnouncementQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.triippztech.guildo.domain.ScheduledAnnouncement}.
 */
@RestController
@RequestMapping("/api")
public class ScheduledAnnouncementResource {

    private final Logger log = LoggerFactory.getLogger(ScheduledAnnouncementResource.class);

    private static final String ENTITY_NAME = "botScheduledAnnouncement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduledAnnouncementService scheduledAnnouncementService;

    private final ScheduledAnnouncementQueryService scheduledAnnouncementQueryService;

    public ScheduledAnnouncementResource(ScheduledAnnouncementService scheduledAnnouncementService, ScheduledAnnouncementQueryService scheduledAnnouncementQueryService) {
        this.scheduledAnnouncementService = scheduledAnnouncementService;
        this.scheduledAnnouncementQueryService = scheduledAnnouncementQueryService;
    }

    /**
     * {@code POST  /scheduled-announcements} : Create a new scheduledAnnouncement.
     *
     * @param scheduledAnnouncement the scheduledAnnouncement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduledAnnouncement, or with status {@code 400 (Bad Request)} if the scheduledAnnouncement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scheduled-announcements")
    public ResponseEntity<ScheduledAnnouncement> createScheduledAnnouncement(@Valid @RequestBody ScheduledAnnouncement scheduledAnnouncement) throws URISyntaxException {
        log.debug("REST request to save ScheduledAnnouncement : {}", scheduledAnnouncement);
        if (scheduledAnnouncement.getId() != null) {
            throw new BadRequestAlertException("A new scheduledAnnouncement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScheduledAnnouncement result = scheduledAnnouncementService.save(scheduledAnnouncement);
        return ResponseEntity.created(new URI("/api/scheduled-announcements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scheduled-announcements} : Updates an existing scheduledAnnouncement.
     *
     * @param scheduledAnnouncement the scheduledAnnouncement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduledAnnouncement,
     * or with status {@code 400 (Bad Request)} if the scheduledAnnouncement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduledAnnouncement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scheduled-announcements")
    public ResponseEntity<ScheduledAnnouncement> updateScheduledAnnouncement(@Valid @RequestBody ScheduledAnnouncement scheduledAnnouncement) throws URISyntaxException {
        log.debug("REST request to update ScheduledAnnouncement : {}", scheduledAnnouncement);
        if (scheduledAnnouncement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ScheduledAnnouncement result = scheduledAnnouncementService.save(scheduledAnnouncement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scheduledAnnouncement.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /scheduled-announcements} : get all the scheduledAnnouncements.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduledAnnouncements in body.
     */
    @GetMapping("/scheduled-announcements")
    public ResponseEntity<List<ScheduledAnnouncement>> getAllScheduledAnnouncements(ScheduledAnnouncementCriteria criteria) {
        log.debug("REST request to get ScheduledAnnouncements by criteria: {}", criteria);
        List<ScheduledAnnouncement> entityList = scheduledAnnouncementQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /scheduled-announcements/count} : count all the scheduledAnnouncements.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/scheduled-announcements/count")
    public ResponseEntity<Long> countScheduledAnnouncements(ScheduledAnnouncementCriteria criteria) {
        log.debug("REST request to count ScheduledAnnouncements by criteria: {}", criteria);
        return ResponseEntity.ok().body(scheduledAnnouncementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /scheduled-announcements/:id} : get the "id" scheduledAnnouncement.
     *
     * @param id the id of the scheduledAnnouncement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduledAnnouncement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scheduled-announcements/{id}")
    public ResponseEntity<ScheduledAnnouncement> getScheduledAnnouncement(@PathVariable Long id) {
        log.debug("REST request to get ScheduledAnnouncement : {}", id);
        Optional<ScheduledAnnouncement> scheduledAnnouncement = scheduledAnnouncementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scheduledAnnouncement);
    }

    /**
     * {@code DELETE  /scheduled-announcements/:id} : delete the "id" scheduledAnnouncement.
     *
     * @param id the id of the scheduledAnnouncement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scheduled-announcements/{id}")
    public ResponseEntity<Void> deleteScheduledAnnouncement(@PathVariable Long id) {
        log.debug("REST request to delete ScheduledAnnouncement : {}", id);
        scheduledAnnouncementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
