package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.Mute;
import com.triippztech.guildo.service.moderation.MuteService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.MuteCriteria;
import com.triippztech.guildo.service.moderation.MuteQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.Mute}.
 */
@RestController
@RequestMapping("/api")
public class MuteResource {

    private final Logger log = LoggerFactory.getLogger(MuteResource.class);

    private static final String ENTITY_NAME = "botMute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MuteService muteService;

    private final MuteQueryService muteQueryService;

    public MuteResource(MuteService muteService, MuteQueryService muteQueryService) {
        this.muteService = muteService;
        this.muteQueryService = muteQueryService;
    }

    /**
     * {@code POST  /mutes} : Create a new mute.
     *
     * @param mute the mute to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mute, or with status {@code 400 (Bad Request)} if the mute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mutes")
    public ResponseEntity<Mute> createMute(@Valid @RequestBody Mute mute) throws URISyntaxException {
        log.debug("REST request to save Mute : {}", mute);
        if (mute.getId() != null) {
            throw new BadRequestAlertException("A new mute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mute result = muteService.save(mute);
        return ResponseEntity.created(new URI("/api/mutes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mutes} : Updates an existing mute.
     *
     * @param mute the mute to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mute,
     * or with status {@code 400 (Bad Request)} if the mute is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mute couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mutes")
    public ResponseEntity<Mute> updateMute(@Valid @RequestBody Mute mute) throws URISyntaxException {
        log.debug("REST request to update Mute : {}", mute);
        if (mute.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Mute result = muteService.save(mute);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mute.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mutes} : get all the mutes.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mutes in body.
     */
    @GetMapping("/mutes")
    public ResponseEntity<List<Mute>> getAllMutes(MuteCriteria criteria) {
        log.debug("REST request to get Mutes by criteria: {}", criteria);
        List<Mute> entityList = muteQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /mutes/count} : count all the mutes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/mutes/count")
    public ResponseEntity<Long> countMutes(MuteCriteria criteria) {
        log.debug("REST request to count Mutes by criteria: {}", criteria);
        return ResponseEntity.ok().body(muteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mutes/:id} : get the "id" mute.
     *
     * @param id the id of the mute to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mute, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mutes/{id}")
    public ResponseEntity<Mute> getMute(@PathVariable Long id) {
        log.debug("REST request to get Mute : {}", id);
        Optional<Mute> mute = muteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mute);
    }

    /**
     * {@code DELETE  /mutes/:id} : delete the "id" mute.
     *
     * @param id the id of the mute to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mutes/{id}")
    public ResponseEntity<Void> deleteMute(@PathVariable Long id) {
        log.debug("REST request to delete Mute : {}", id);
        muteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
