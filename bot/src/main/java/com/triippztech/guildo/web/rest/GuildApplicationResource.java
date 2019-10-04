package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.GuildApplication;
import com.triippztech.guildo.service.guild.GuildApplicationService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.GuildApplicationCriteria;
import com.triippztech.guildo.service.guild.GuildApplicationQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.GuildApplication}.
 */
@RestController
@RequestMapping("/api")
public class GuildApplicationResource {

    private final Logger log = LoggerFactory.getLogger(GuildApplicationResource.class);

    private static final String ENTITY_NAME = "botGuildApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuildApplicationService guildApplicationService;

    private final GuildApplicationQueryService guildApplicationQueryService;

    public GuildApplicationResource(GuildApplicationService guildApplicationService, GuildApplicationQueryService guildApplicationQueryService) {
        this.guildApplicationService = guildApplicationService;
        this.guildApplicationQueryService = guildApplicationQueryService;
    }

    /**
     * {@code POST  /guild-applications} : Create a new guildApplication.
     *
     * @param guildApplication the guildApplication to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guildApplication, or with status {@code 400 (Bad Request)} if the guildApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guild-applications")
    public ResponseEntity<GuildApplication> createGuildApplication(@Valid @RequestBody GuildApplication guildApplication) throws URISyntaxException {
        log.debug("REST request to save GuildApplication : {}", guildApplication);
        if (guildApplication.getId() != null) {
            throw new BadRequestAlertException("A new guildApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildApplication result = guildApplicationService.save(guildApplication);
        return ResponseEntity.created(new URI("/api/guild-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guild-applications} : Updates an existing guildApplication.
     *
     * @param guildApplication the guildApplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guildApplication,
     * or with status {@code 400 (Bad Request)} if the guildApplication is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guildApplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guild-applications")
    public ResponseEntity<GuildApplication> updateGuildApplication(@Valid @RequestBody GuildApplication guildApplication) throws URISyntaxException {
        log.debug("REST request to update GuildApplication : {}", guildApplication);
        if (guildApplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildApplication result = guildApplicationService.save(guildApplication);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guildApplication.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guild-applications} : get all the guildApplications.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guildApplications in body.
     */
    @GetMapping("/guild-applications")
    public ResponseEntity<List<GuildApplication>> getAllGuildApplications(GuildApplicationCriteria criteria) {
        log.debug("REST request to get GuildApplications by criteria: {}", criteria);
        List<GuildApplication> entityList = guildApplicationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /guild-applications/count} : count all the guildApplications.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/guild-applications/count")
    public ResponseEntity<Long> countGuildApplications(GuildApplicationCriteria criteria) {
        log.debug("REST request to count GuildApplications by criteria: {}", criteria);
        return ResponseEntity.ok().body(guildApplicationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /guild-applications/:id} : get the "id" guildApplication.
     *
     * @param id the id of the guildApplication to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guildApplication, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guild-applications/{id}")
    public ResponseEntity<GuildApplication> getGuildApplication(@PathVariable Long id) {
        log.debug("REST request to get GuildApplication : {}", id);
        Optional<GuildApplication> guildApplication = guildApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildApplication);
    }

    /**
     * {@code DELETE  /guild-applications/:id} : delete the "id" guildApplication.
     *
     * @param id the id of the guildApplication to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guild-applications/{id}")
    public ResponseEntity<Void> deleteGuildApplication(@PathVariable Long id) {
        log.debug("REST request to delete GuildApplication : {}", id);
        guildApplicationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
