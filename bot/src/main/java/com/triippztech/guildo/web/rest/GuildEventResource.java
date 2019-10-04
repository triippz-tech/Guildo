package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.GuildEvent;
import com.triippztech.guildo.service.guild.GuildEventService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.GuildEventCriteria;
import com.triippztech.guildo.service.guild.GuildEventQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.GuildEvent}.
 */
@RestController
@RequestMapping("/api")
public class GuildEventResource {

    private final Logger log = LoggerFactory.getLogger(GuildEventResource.class);

    private static final String ENTITY_NAME = "botGuildEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuildEventService guildEventService;

    private final GuildEventQueryService guildEventQueryService;

    public GuildEventResource(GuildEventService guildEventService, GuildEventQueryService guildEventQueryService) {
        this.guildEventService = guildEventService;
        this.guildEventQueryService = guildEventQueryService;
    }

    /**
     * {@code POST  /guild-events} : Create a new guildEvent.
     *
     * @param guildEvent the guildEvent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guildEvent, or with status {@code 400 (Bad Request)} if the guildEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guild-events")
    public ResponseEntity<GuildEvent> createGuildEvent(@Valid @RequestBody GuildEvent guildEvent) throws URISyntaxException {
        log.debug("REST request to save GuildEvent : {}", guildEvent);
        if (guildEvent.getId() != null) {
            throw new BadRequestAlertException("A new guildEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildEvent result = guildEventService.save(guildEvent);
        return ResponseEntity.created(new URI("/api/guild-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guild-events} : Updates an existing guildEvent.
     *
     * @param guildEvent the guildEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guildEvent,
     * or with status {@code 400 (Bad Request)} if the guildEvent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guildEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guild-events")
    public ResponseEntity<GuildEvent> updateGuildEvent(@Valid @RequestBody GuildEvent guildEvent) throws URISyntaxException {
        log.debug("REST request to update GuildEvent : {}", guildEvent);
        if (guildEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildEvent result = guildEventService.save(guildEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guildEvent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guild-events} : get all the guildEvents.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guildEvents in body.
     */
    @GetMapping("/guild-events")
    public ResponseEntity<List<GuildEvent>> getAllGuildEvents(GuildEventCriteria criteria) {
        log.debug("REST request to get GuildEvents by criteria: {}", criteria);
        List<GuildEvent> entityList = guildEventQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /guild-events/count} : count all the guildEvents.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/guild-events/count")
    public ResponseEntity<Long> countGuildEvents(GuildEventCriteria criteria) {
        log.debug("REST request to count GuildEvents by criteria: {}", criteria);
        return ResponseEntity.ok().body(guildEventQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /guild-events/:id} : get the "id" guildEvent.
     *
     * @param id the id of the guildEvent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guildEvent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guild-events/{id}")
    public ResponseEntity<GuildEvent> getGuildEvent(@PathVariable Long id) {
        log.debug("REST request to get GuildEvent : {}", id);
        Optional<GuildEvent> guildEvent = guildEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildEvent);
    }

    /**
     * {@code DELETE  /guild-events/:id} : delete the "id" guildEvent.
     *
     * @param id the id of the guildEvent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guild-events/{id}")
    public ResponseEntity<Void> deleteGuildEvent(@PathVariable Long id) {
        log.debug("REST request to delete GuildEvent : {}", id);
        guildEventService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
