package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.TempBan;
import com.triippztech.guildo.service.TempBanService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.TempBanCriteria;
import com.triippztech.guildo.service.TempBanQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.TempBan}.
 */
@RestController
@RequestMapping("/api")
public class TempBanResource {

    private final Logger log = LoggerFactory.getLogger(TempBanResource.class);

    private static final String ENTITY_NAME = "botTempBan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TempBanService tempBanService;

    private final TempBanQueryService tempBanQueryService;

    public TempBanResource(TempBanService tempBanService, TempBanQueryService tempBanQueryService) {
        this.tempBanService = tempBanService;
        this.tempBanQueryService = tempBanQueryService;
    }

    /**
     * {@code POST  /temp-bans} : Create a new tempBan.
     *
     * @param tempBan the tempBan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tempBan, or with status {@code 400 (Bad Request)} if the tempBan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/temp-bans")
    public ResponseEntity<TempBan> createTempBan(@Valid @RequestBody TempBan tempBan) throws URISyntaxException {
        log.debug("REST request to save TempBan : {}", tempBan);
        if (tempBan.getId() != null) {
            throw new BadRequestAlertException("A new tempBan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TempBan result = tempBanService.save(tempBan);
        return ResponseEntity.created(new URI("/api/temp-bans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /temp-bans} : Updates an existing tempBan.
     *
     * @param tempBan the tempBan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tempBan,
     * or with status {@code 400 (Bad Request)} if the tempBan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tempBan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/temp-bans")
    public ResponseEntity<TempBan> updateTempBan(@Valid @RequestBody TempBan tempBan) throws URISyntaxException {
        log.debug("REST request to update TempBan : {}", tempBan);
        if (tempBan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TempBan result = tempBanService.save(tempBan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tempBan.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /temp-bans} : get all the tempBans.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tempBans in body.
     */
    @GetMapping("/temp-bans")
    public ResponseEntity<List<TempBan>> getAllTempBans(TempBanCriteria criteria) {
        log.debug("REST request to get TempBans by criteria: {}", criteria);
        List<TempBan> entityList = tempBanQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /temp-bans/count} : count all the tempBans.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/temp-bans/count")
    public ResponseEntity<Long> countTempBans(TempBanCriteria criteria) {
        log.debug("REST request to count TempBans by criteria: {}", criteria);
        return ResponseEntity.ok().body(tempBanQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /temp-bans/:id} : get the "id" tempBan.
     *
     * @param id the id of the tempBan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tempBan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/temp-bans/{id}")
    public ResponseEntity<TempBan> getTempBan(@PathVariable Long id) {
        log.debug("REST request to get TempBan : {}", id);
        Optional<TempBan> tempBan = tempBanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tempBan);
    }

    /**
     * {@code DELETE  /temp-bans/:id} : delete the "id" tempBan.
     *
     * @param id the id of the tempBan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/temp-bans/{id}")
    public ResponseEntity<Void> deleteTempBan(@PathVariable Long id) {
        log.debug("REST request to delete TempBan : {}", id);
        tempBanService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
