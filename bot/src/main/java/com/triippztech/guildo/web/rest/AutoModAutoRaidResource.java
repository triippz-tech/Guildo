package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.AutoModAutoRaid;
import com.triippztech.guildo.service.moderation.AutoModAutoRaidService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.AutoModAutoRaidCriteria;
import com.triippztech.guildo.service.moderation.AutoModAutoRaidQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.AutoModAutoRaid}.
 */
@RestController
@RequestMapping("/api")
public class AutoModAutoRaidResource {

    private final Logger log = LoggerFactory.getLogger(AutoModAutoRaidResource.class);

    private static final String ENTITY_NAME = "botAutoModAutoRaid";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutoModAutoRaidService autoModAutoRaidService;

    private final AutoModAutoRaidQueryService autoModAutoRaidQueryService;

    public AutoModAutoRaidResource(AutoModAutoRaidService autoModAutoRaidService, AutoModAutoRaidQueryService autoModAutoRaidQueryService) {
        this.autoModAutoRaidService = autoModAutoRaidService;
        this.autoModAutoRaidQueryService = autoModAutoRaidQueryService;
    }

    /**
     * {@code POST  /auto-mod-auto-raids} : Create a new autoModAutoRaid.
     *
     * @param autoModAutoRaid the autoModAutoRaid to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autoModAutoRaid, or with status {@code 400 (Bad Request)} if the autoModAutoRaid has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/auto-mod-auto-raids")
    public ResponseEntity<AutoModAutoRaid> createAutoModAutoRaid(@Valid @RequestBody AutoModAutoRaid autoModAutoRaid) throws URISyntaxException {
        log.debug("REST request to save AutoModAutoRaid : {}", autoModAutoRaid);
        if (autoModAutoRaid.getId() != null) {
            throw new BadRequestAlertException("A new autoModAutoRaid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AutoModAutoRaid result = autoModAutoRaidService.save(autoModAutoRaid);
        return ResponseEntity.created(new URI("/api/auto-mod-auto-raids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /auto-mod-auto-raids} : Updates an existing autoModAutoRaid.
     *
     * @param autoModAutoRaid the autoModAutoRaid to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoModAutoRaid,
     * or with status {@code 400 (Bad Request)} if the autoModAutoRaid is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autoModAutoRaid couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/auto-mod-auto-raids")
    public ResponseEntity<AutoModAutoRaid> updateAutoModAutoRaid(@Valid @RequestBody AutoModAutoRaid autoModAutoRaid) throws URISyntaxException {
        log.debug("REST request to update AutoModAutoRaid : {}", autoModAutoRaid);
        if (autoModAutoRaid.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AutoModAutoRaid result = autoModAutoRaidService.save(autoModAutoRaid);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autoModAutoRaid.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /auto-mod-auto-raids} : get all the autoModAutoRaids.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autoModAutoRaids in body.
     */
    @GetMapping("/auto-mod-auto-raids")
    public ResponseEntity<List<AutoModAutoRaid>> getAllAutoModAutoRaids(AutoModAutoRaidCriteria criteria) {
        log.debug("REST request to get AutoModAutoRaids by criteria: {}", criteria);
        List<AutoModAutoRaid> entityList = autoModAutoRaidQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /auto-mod-auto-raids/count} : count all the autoModAutoRaids.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/auto-mod-auto-raids/count")
    public ResponseEntity<Long> countAutoModAutoRaids(AutoModAutoRaidCriteria criteria) {
        log.debug("REST request to count AutoModAutoRaids by criteria: {}", criteria);
        return ResponseEntity.ok().body(autoModAutoRaidQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /auto-mod-auto-raids/:id} : get the "id" autoModAutoRaid.
     *
     * @param id the id of the autoModAutoRaid to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autoModAutoRaid, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/auto-mod-auto-raids/{id}")
    public ResponseEntity<AutoModAutoRaid> getAutoModAutoRaid(@PathVariable Long id) {
        log.debug("REST request to get AutoModAutoRaid : {}", id);
        Optional<AutoModAutoRaid> autoModAutoRaid = autoModAutoRaidService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autoModAutoRaid);
    }

    /**
     * {@code DELETE  /auto-mod-auto-raids/:id} : delete the "id" autoModAutoRaid.
     *
     * @param id the id of the autoModAutoRaid to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/auto-mod-auto-raids/{id}")
    public ResponseEntity<Void> deleteAutoModAutoRaid(@PathVariable Long id) {
        log.debug("REST request to delete AutoModAutoRaid : {}", id);
        autoModAutoRaidService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
