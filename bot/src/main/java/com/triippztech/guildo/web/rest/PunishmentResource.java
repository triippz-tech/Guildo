package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.Punishment;
import com.triippztech.guildo.service.moderation.PunishmentService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.PunishmentCriteria;
import com.triippztech.guildo.service.moderation.PunishmentQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.Punishment}.
 */
@RestController
@RequestMapping("/api")
public class PunishmentResource {

    private final Logger log = LoggerFactory.getLogger(PunishmentResource.class);

    private static final String ENTITY_NAME = "botPunishment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PunishmentService punishmentService;

    private final PunishmentQueryService punishmentQueryService;

    public PunishmentResource(PunishmentService punishmentService, PunishmentQueryService punishmentQueryService) {
        this.punishmentService = punishmentService;
        this.punishmentQueryService = punishmentQueryService;
    }

    /**
     * {@code POST  /punishments} : Create a new punishment.
     *
     * @param punishment the punishment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new punishment, or with status {@code 400 (Bad Request)} if the punishment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/punishments")
    public ResponseEntity<Punishment> createPunishment(@Valid @RequestBody Punishment punishment) throws URISyntaxException {
        log.debug("REST request to save Punishment : {}", punishment);
        if (punishment.getId() != null) {
            throw new BadRequestAlertException("A new punishment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Punishment result = punishmentService.save(punishment);
        return ResponseEntity.created(new URI("/api/punishments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /punishments} : Updates an existing punishment.
     *
     * @param punishment the punishment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated punishment,
     * or with status {@code 400 (Bad Request)} if the punishment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the punishment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/punishments")
    public ResponseEntity<Punishment> updatePunishment(@Valid @RequestBody Punishment punishment) throws URISyntaxException {
        log.debug("REST request to update Punishment : {}", punishment);
        if (punishment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Punishment result = punishmentService.save(punishment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, punishment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /punishments} : get all the punishments.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of punishments in body.
     */
    @GetMapping("/punishments")
    public ResponseEntity<List<Punishment>> getAllPunishments(PunishmentCriteria criteria) {
        log.debug("REST request to get Punishments by criteria: {}", criteria);
        List<Punishment> entityList = punishmentQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /punishments/count} : count all the punishments.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/punishments/count")
    public ResponseEntity<Long> countPunishments(PunishmentCriteria criteria) {
        log.debug("REST request to count Punishments by criteria: {}", criteria);
        return ResponseEntity.ok().body(punishmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /punishments/:id} : get the "id" punishment.
     *
     * @param id the id of the punishment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the punishment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/punishments/{id}")
    public ResponseEntity<Punishment> getPunishment(@PathVariable Long id) {
        log.debug("REST request to get Punishment : {}", id);
        Optional<Punishment> punishment = punishmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(punishment);
    }

    /**
     * {@code DELETE  /punishments/:id} : delete the "id" punishment.
     *
     * @param id the id of the punishment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/punishments/{id}")
    public ResponseEntity<Void> deletePunishment(@PathVariable Long id) {
        log.debug("REST request to delete Punishment : {}", id);
        punishmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
