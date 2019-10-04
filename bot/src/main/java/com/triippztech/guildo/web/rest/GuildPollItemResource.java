package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.GuildPollItem;
import com.triippztech.guildo.service.guild.GuildPollItemService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.GuildPollItemCriteria;
import com.triippztech.guildo.service.guild.GuildPollItemQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.GuildPollItem}.
 */
@RestController
@RequestMapping("/api")
public class GuildPollItemResource {

    private final Logger log = LoggerFactory.getLogger(GuildPollItemResource.class);

    private static final String ENTITY_NAME = "botGuildPollItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuildPollItemService guildPollItemService;

    private final GuildPollItemQueryService guildPollItemQueryService;

    public GuildPollItemResource(GuildPollItemService guildPollItemService, GuildPollItemQueryService guildPollItemQueryService) {
        this.guildPollItemService = guildPollItemService;
        this.guildPollItemQueryService = guildPollItemQueryService;
    }

    /**
     * {@code POST  /guild-poll-items} : Create a new guildPollItem.
     *
     * @param guildPollItem the guildPollItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guildPollItem, or with status {@code 400 (Bad Request)} if the guildPollItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guild-poll-items")
    public ResponseEntity<GuildPollItem> createGuildPollItem(@Valid @RequestBody GuildPollItem guildPollItem) throws URISyntaxException {
        log.debug("REST request to save GuildPollItem : {}", guildPollItem);
        if (guildPollItem.getId() != null) {
            throw new BadRequestAlertException("A new guildPollItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildPollItem result = guildPollItemService.save(guildPollItem);
        return ResponseEntity.created(new URI("/api/guild-poll-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guild-poll-items} : Updates an existing guildPollItem.
     *
     * @param guildPollItem the guildPollItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guildPollItem,
     * or with status {@code 400 (Bad Request)} if the guildPollItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guildPollItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guild-poll-items")
    public ResponseEntity<GuildPollItem> updateGuildPollItem(@Valid @RequestBody GuildPollItem guildPollItem) throws URISyntaxException {
        log.debug("REST request to update GuildPollItem : {}", guildPollItem);
        if (guildPollItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildPollItem result = guildPollItemService.save(guildPollItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guildPollItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guild-poll-items} : get all the guildPollItems.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guildPollItems in body.
     */
    @GetMapping("/guild-poll-items")
    public ResponseEntity<List<GuildPollItem>> getAllGuildPollItems(GuildPollItemCriteria criteria) {
        log.debug("REST request to get GuildPollItems by criteria: {}", criteria);
        List<GuildPollItem> entityList = guildPollItemQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /guild-poll-items/count} : count all the guildPollItems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/guild-poll-items/count")
    public ResponseEntity<Long> countGuildPollItems(GuildPollItemCriteria criteria) {
        log.debug("REST request to count GuildPollItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(guildPollItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /guild-poll-items/:id} : get the "id" guildPollItem.
     *
     * @param id the id of the guildPollItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guildPollItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guild-poll-items/{id}")
    public ResponseEntity<GuildPollItem> getGuildPollItem(@PathVariable Long id) {
        log.debug("REST request to get GuildPollItem : {}", id);
        Optional<GuildPollItem> guildPollItem = guildPollItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildPollItem);
    }

    /**
     * {@code DELETE  /guild-poll-items/:id} : delete the "id" guildPollItem.
     *
     * @param id the id of the guildPollItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guild-poll-items/{id}")
    public ResponseEntity<Void> deleteGuildPollItem(@PathVariable Long id) {
        log.debug("REST request to delete GuildPollItem : {}", id);
        guildPollItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
