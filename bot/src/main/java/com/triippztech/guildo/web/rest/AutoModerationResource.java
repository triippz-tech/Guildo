package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.AutoModeration;
import com.triippztech.guildo.service.moderation.AutoModerationService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.AutoModerationCriteria;
import com.triippztech.guildo.service.moderation.AutoModerationQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.AutoModeration}.
 */
@RestController
@RequestMapping("/api")
public class AutoModerationResource {

    private final Logger log = LoggerFactory.getLogger(AutoModerationResource.class);

    private static final String ENTITY_NAME = "botAutoModeration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutoModerationService autoModerationService;

    private final AutoModerationQueryService autoModerationQueryService;

    public AutoModerationResource(AutoModerationService autoModerationService, AutoModerationQueryService autoModerationQueryService) {
        this.autoModerationService = autoModerationService;
        this.autoModerationQueryService = autoModerationQueryService;
    }

    /**
     * {@code POST  /auto-moderations} : Create a new autoModeration.
     *
     * @param autoModeration the autoModeration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autoModeration, or with status {@code 400 (Bad Request)} if the autoModeration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/auto-moderations")
    public ResponseEntity<AutoModeration> createAutoModeration(@Valid @RequestBody AutoModeration autoModeration) throws URISyntaxException {
        log.debug("REST request to save AutoModeration : {}", autoModeration);
        if (autoModeration.getId() != null) {
            throw new BadRequestAlertException("A new autoModeration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AutoModeration result = autoModerationService.save(autoModeration);
        return ResponseEntity.created(new URI("/api/auto-moderations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /auto-moderations} : Updates an existing autoModeration.
     *
     * @param autoModeration the autoModeration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoModeration,
     * or with status {@code 400 (Bad Request)} if the autoModeration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autoModeration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/auto-moderations")
    public ResponseEntity<AutoModeration> updateAutoModeration(@Valid @RequestBody AutoModeration autoModeration) throws URISyntaxException {
        log.debug("REST request to update AutoModeration : {}", autoModeration);
        if (autoModeration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AutoModeration result = autoModerationService.save(autoModeration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autoModeration.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /auto-moderations} : get all the autoModerations.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autoModerations in body.
     */
    @GetMapping("/auto-moderations")
    public ResponseEntity<List<AutoModeration>> getAllAutoModerations(AutoModerationCriteria criteria) {
        log.debug("REST request to get AutoModerations by criteria: {}", criteria);
        List<AutoModeration> entityList = autoModerationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /auto-moderations/count} : count all the autoModerations.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/auto-moderations/count")
    public ResponseEntity<Long> countAutoModerations(AutoModerationCriteria criteria) {
        log.debug("REST request to count AutoModerations by criteria: {}", criteria);
        return ResponseEntity.ok().body(autoModerationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /auto-moderations/:id} : get the "id" autoModeration.
     *
     * @param id the id of the autoModeration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autoModeration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/auto-moderations/{id}")
    public ResponseEntity<AutoModeration> getAutoModeration(@PathVariable Long id) {
        log.debug("REST request to get AutoModeration : {}", id);
        Optional<AutoModeration> autoModeration = autoModerationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autoModeration);
    }

    /**
     * {@code DELETE  /auto-moderations/:id} : delete the "id" autoModeration.
     *
     * @param id the id of the autoModeration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/auto-moderations/{id}")
    public ResponseEntity<Void> deleteAutoModeration(@PathVariable Long id) {
        log.debug("REST request to delete AutoModeration : {}", id);
        autoModerationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
