package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.AutoModAntiDup;
import com.triippztech.guildo.service.moderation.AutoModAntiDupService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.AutoModAntiDupCriteria;
import com.triippztech.guildo.service.moderation.AutoModAntiDupQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.AutoModAntiDup}.
 */
@RestController
@RequestMapping("/api")
public class AutoModAntiDupResource {

    private final Logger log = LoggerFactory.getLogger(AutoModAntiDupResource.class);

    private static final String ENTITY_NAME = "botAutoModAntiDup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutoModAntiDupService autoModAntiDupService;

    private final AutoModAntiDupQueryService autoModAntiDupQueryService;

    public AutoModAntiDupResource(AutoModAntiDupService autoModAntiDupService, AutoModAntiDupQueryService autoModAntiDupQueryService) {
        this.autoModAntiDupService = autoModAntiDupService;
        this.autoModAntiDupQueryService = autoModAntiDupQueryService;
    }

    /**
     * {@code POST  /auto-mod-anti-dups} : Create a new autoModAntiDup.
     *
     * @param autoModAntiDup the autoModAntiDup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autoModAntiDup, or with status {@code 400 (Bad Request)} if the autoModAntiDup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/auto-mod-anti-dups")
    public ResponseEntity<AutoModAntiDup> createAutoModAntiDup(@Valid @RequestBody AutoModAntiDup autoModAntiDup) throws URISyntaxException {
        log.debug("REST request to save AutoModAntiDup : {}", autoModAntiDup);
        if (autoModAntiDup.getId() != null) {
            throw new BadRequestAlertException("A new autoModAntiDup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AutoModAntiDup result = autoModAntiDupService.save(autoModAntiDup);
        return ResponseEntity.created(new URI("/api/auto-mod-anti-dups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /auto-mod-anti-dups} : Updates an existing autoModAntiDup.
     *
     * @param autoModAntiDup the autoModAntiDup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoModAntiDup,
     * or with status {@code 400 (Bad Request)} if the autoModAntiDup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autoModAntiDup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/auto-mod-anti-dups")
    public ResponseEntity<AutoModAntiDup> updateAutoModAntiDup(@Valid @RequestBody AutoModAntiDup autoModAntiDup) throws URISyntaxException {
        log.debug("REST request to update AutoModAntiDup : {}", autoModAntiDup);
        if (autoModAntiDup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AutoModAntiDup result = autoModAntiDupService.save(autoModAntiDup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autoModAntiDup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /auto-mod-anti-dups} : get all the autoModAntiDups.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autoModAntiDups in body.
     */
    @GetMapping("/auto-mod-anti-dups")
    public ResponseEntity<List<AutoModAntiDup>> getAllAutoModAntiDups(AutoModAntiDupCriteria criteria) {
        log.debug("REST request to get AutoModAntiDups by criteria: {}", criteria);
        List<AutoModAntiDup> entityList = autoModAntiDupQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /auto-mod-anti-dups/count} : count all the autoModAntiDups.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/auto-mod-anti-dups/count")
    public ResponseEntity<Long> countAutoModAntiDups(AutoModAntiDupCriteria criteria) {
        log.debug("REST request to count AutoModAntiDups by criteria: {}", criteria);
        return ResponseEntity.ok().body(autoModAntiDupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /auto-mod-anti-dups/:id} : get the "id" autoModAntiDup.
     *
     * @param id the id of the autoModAntiDup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autoModAntiDup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/auto-mod-anti-dups/{id}")
    public ResponseEntity<AutoModAntiDup> getAutoModAntiDup(@PathVariable Long id) {
        log.debug("REST request to get AutoModAntiDup : {}", id);
        Optional<AutoModAntiDup> autoModAntiDup = autoModAntiDupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autoModAntiDup);
    }

    /**
     * {@code DELETE  /auto-mod-anti-dups/:id} : delete the "id" autoModAntiDup.
     *
     * @param id the id of the autoModAntiDup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/auto-mod-anti-dups/{id}")
    public ResponseEntity<Void> deleteAutoModAntiDup(@PathVariable Long id) {
        log.debug("REST request to delete AutoModAntiDup : {}", id);
        autoModAntiDupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
