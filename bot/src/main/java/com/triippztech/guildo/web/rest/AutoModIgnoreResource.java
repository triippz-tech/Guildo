package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.AutoModIgnore;
import com.triippztech.guildo.service.moderation.AutoModIgnoreService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.AutoModIgnoreCriteria;
import com.triippztech.guildo.service.moderation.AutoModIgnoreQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.AutoModIgnore}.
 */
@RestController
@RequestMapping("/api")
public class AutoModIgnoreResource {

    private final Logger log = LoggerFactory.getLogger(AutoModIgnoreResource.class);

    private static final String ENTITY_NAME = "botAutoModIgnore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutoModIgnoreService autoModIgnoreService;

    private final AutoModIgnoreQueryService autoModIgnoreQueryService;

    public AutoModIgnoreResource(AutoModIgnoreService autoModIgnoreService, AutoModIgnoreQueryService autoModIgnoreQueryService) {
        this.autoModIgnoreService = autoModIgnoreService;
        this.autoModIgnoreQueryService = autoModIgnoreQueryService;
    }

    /**
     * {@code POST  /auto-mod-ignores} : Create a new autoModIgnore.
     *
     * @param autoModIgnore the autoModIgnore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autoModIgnore, or with status {@code 400 (Bad Request)} if the autoModIgnore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/auto-mod-ignores")
    public ResponseEntity<AutoModIgnore> createAutoModIgnore(@Valid @RequestBody AutoModIgnore autoModIgnore) throws URISyntaxException {
        log.debug("REST request to save AutoModIgnore : {}", autoModIgnore);
        if (autoModIgnore.getId() != null) {
            throw new BadRequestAlertException("A new autoModIgnore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AutoModIgnore result = autoModIgnoreService.save(autoModIgnore);
        return ResponseEntity.created(new URI("/api/auto-mod-ignores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /auto-mod-ignores} : Updates an existing autoModIgnore.
     *
     * @param autoModIgnore the autoModIgnore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoModIgnore,
     * or with status {@code 400 (Bad Request)} if the autoModIgnore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autoModIgnore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/auto-mod-ignores")
    public ResponseEntity<AutoModIgnore> updateAutoModIgnore(@Valid @RequestBody AutoModIgnore autoModIgnore) throws URISyntaxException {
        log.debug("REST request to update AutoModIgnore : {}", autoModIgnore);
        if (autoModIgnore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AutoModIgnore result = autoModIgnoreService.save(autoModIgnore);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autoModIgnore.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /auto-mod-ignores} : get all the autoModIgnores.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autoModIgnores in body.
     */
    @GetMapping("/auto-mod-ignores")
    public ResponseEntity<List<AutoModIgnore>> getAllAutoModIgnores(AutoModIgnoreCriteria criteria) {
        log.debug("REST request to get AutoModIgnores by criteria: {}", criteria);
        List<AutoModIgnore> entityList = autoModIgnoreQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /auto-mod-ignores/count} : count all the autoModIgnores.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/auto-mod-ignores/count")
    public ResponseEntity<Long> countAutoModIgnores(AutoModIgnoreCriteria criteria) {
        log.debug("REST request to count AutoModIgnores by criteria: {}", criteria);
        return ResponseEntity.ok().body(autoModIgnoreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /auto-mod-ignores/:id} : get the "id" autoModIgnore.
     *
     * @param id the id of the autoModIgnore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autoModIgnore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/auto-mod-ignores/{id}")
    public ResponseEntity<AutoModIgnore> getAutoModIgnore(@PathVariable Long id) {
        log.debug("REST request to get AutoModIgnore : {}", id);
        Optional<AutoModIgnore> autoModIgnore = autoModIgnoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autoModIgnore);
    }

    /**
     * {@code DELETE  /auto-mod-ignores/:id} : delete the "id" autoModIgnore.
     *
     * @param id the id of the autoModIgnore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/auto-mod-ignores/{id}")
    public ResponseEntity<Void> deleteAutoModIgnore(@PathVariable Long id) {
        log.debug("REST request to delete AutoModIgnore : {}", id);
        autoModIgnoreService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
