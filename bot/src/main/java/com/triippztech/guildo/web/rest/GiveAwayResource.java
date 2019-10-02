package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.GiveAway;
import com.triippztech.guildo.service.GiveAwayService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.GiveAwayCriteria;
import com.triippztech.guildo.service.GiveAwayQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.GiveAway}.
 */
@RestController
@RequestMapping("/api")
public class GiveAwayResource {

    private final Logger log = LoggerFactory.getLogger(GiveAwayResource.class);

    private static final String ENTITY_NAME = "botGiveAway";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GiveAwayService giveAwayService;

    private final GiveAwayQueryService giveAwayQueryService;

    public GiveAwayResource(GiveAwayService giveAwayService, GiveAwayQueryService giveAwayQueryService) {
        this.giveAwayService = giveAwayService;
        this.giveAwayQueryService = giveAwayQueryService;
    }

    /**
     * {@code POST  /give-aways} : Create a new giveAway.
     *
     * @param giveAway the giveAway to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new giveAway, or with status {@code 400 (Bad Request)} if the giveAway has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/give-aways")
    public ResponseEntity<GiveAway> createGiveAway(@Valid @RequestBody GiveAway giveAway) throws URISyntaxException {
        log.debug("REST request to save GiveAway : {}", giveAway);
        if (giveAway.getId() != null) {
            throw new BadRequestAlertException("A new giveAway cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GiveAway result = giveAwayService.save(giveAway);
        return ResponseEntity.created(new URI("/api/give-aways/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /give-aways} : Updates an existing giveAway.
     *
     * @param giveAway the giveAway to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated giveAway,
     * or with status {@code 400 (Bad Request)} if the giveAway is not valid,
     * or with status {@code 500 (Internal Server Error)} if the giveAway couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/give-aways")
    public ResponseEntity<GiveAway> updateGiveAway(@Valid @RequestBody GiveAway giveAway) throws URISyntaxException {
        log.debug("REST request to update GiveAway : {}", giveAway);
        if (giveAway.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GiveAway result = giveAwayService.save(giveAway);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, giveAway.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /give-aways} : get all the giveAways.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of giveAways in body.
     */
    @GetMapping("/give-aways")
    public ResponseEntity<List<GiveAway>> getAllGiveAways(GiveAwayCriteria criteria) {
        log.debug("REST request to get GiveAways by criteria: {}", criteria);
        List<GiveAway> entityList = giveAwayQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /give-aways/count} : count all the giveAways.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/give-aways/count")
    public ResponseEntity<Long> countGiveAways(GiveAwayCriteria criteria) {
        log.debug("REST request to count GiveAways by criteria: {}", criteria);
        return ResponseEntity.ok().body(giveAwayQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /give-aways/:id} : get the "id" giveAway.
     *
     * @param id the id of the giveAway to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the giveAway, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/give-aways/{id}")
    public ResponseEntity<GiveAway> getGiveAway(@PathVariable Long id) {
        log.debug("REST request to get GiveAway : {}", id);
        Optional<GiveAway> giveAway = giveAwayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(giveAway);
    }

    /**
     * {@code DELETE  /give-aways/:id} : delete the "id" giveAway.
     *
     * @param id the id of the giveAway to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/give-aways/{id}")
    public ResponseEntity<Void> deleteGiveAway(@PathVariable Long id) {
        log.debug("REST request to delete GiveAway : {}", id);
        giveAwayService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
