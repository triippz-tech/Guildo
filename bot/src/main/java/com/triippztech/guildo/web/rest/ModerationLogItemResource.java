package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.ModerationLogItem;
import com.triippztech.guildo.service.ModerationLogItemService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.ModerationLogItemCriteria;
import com.triippztech.guildo.service.ModerationLogItemQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.ModerationLogItem}.
 */
@RestController
@RequestMapping("/api")
public class ModerationLogItemResource {

    private final Logger log = LoggerFactory.getLogger(ModerationLogItemResource.class);

    private static final String ENTITY_NAME = "botModerationLogItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModerationLogItemService moderationLogItemService;

    private final ModerationLogItemQueryService moderationLogItemQueryService;

    public ModerationLogItemResource(ModerationLogItemService moderationLogItemService, ModerationLogItemQueryService moderationLogItemQueryService) {
        this.moderationLogItemService = moderationLogItemService;
        this.moderationLogItemQueryService = moderationLogItemQueryService;
    }

    /**
     * {@code POST  /moderation-log-items} : Create a new moderationLogItem.
     *
     * @param moderationLogItem the moderationLogItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moderationLogItem, or with status {@code 400 (Bad Request)} if the moderationLogItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/moderation-log-items")
    public ResponseEntity<ModerationLogItem> createModerationLogItem(@Valid @RequestBody ModerationLogItem moderationLogItem) throws URISyntaxException {
        log.debug("REST request to save ModerationLogItem : {}", moderationLogItem);
        if (moderationLogItem.getId() != null) {
            throw new BadRequestAlertException("A new moderationLogItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ModerationLogItem result = moderationLogItemService.save(moderationLogItem);
        return ResponseEntity.created(new URI("/api/moderation-log-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /moderation-log-items} : Updates an existing moderationLogItem.
     *
     * @param moderationLogItem the moderationLogItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moderationLogItem,
     * or with status {@code 400 (Bad Request)} if the moderationLogItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moderationLogItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/moderation-log-items")
    public ResponseEntity<ModerationLogItem> updateModerationLogItem(@Valid @RequestBody ModerationLogItem moderationLogItem) throws URISyntaxException {
        log.debug("REST request to update ModerationLogItem : {}", moderationLogItem);
        if (moderationLogItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ModerationLogItem result = moderationLogItemService.save(moderationLogItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moderationLogItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /moderation-log-items} : get all the moderationLogItems.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moderationLogItems in body.
     */
    @GetMapping("/moderation-log-items")
    public ResponseEntity<List<ModerationLogItem>> getAllModerationLogItems(ModerationLogItemCriteria criteria) {
        log.debug("REST request to get ModerationLogItems by criteria: {}", criteria);
        List<ModerationLogItem> entityList = moderationLogItemQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /moderation-log-items/count} : count all the moderationLogItems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/moderation-log-items/count")
    public ResponseEntity<Long> countModerationLogItems(ModerationLogItemCriteria criteria) {
        log.debug("REST request to count ModerationLogItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(moderationLogItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /moderation-log-items/:id} : get the "id" moderationLogItem.
     *
     * @param id the id of the moderationLogItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moderationLogItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/moderation-log-items/{id}")
    public ResponseEntity<ModerationLogItem> getModerationLogItem(@PathVariable Long id) {
        log.debug("REST request to get ModerationLogItem : {}", id);
        Optional<ModerationLogItem> moderationLogItem = moderationLogItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moderationLogItem);
    }

    /**
     * {@code DELETE  /moderation-log-items/:id} : delete the "id" moderationLogItem.
     *
     * @param id the id of the moderationLogItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/moderation-log-items/{id}")
    public ResponseEntity<Void> deleteModerationLogItem(@PathVariable Long id) {
        log.debug("REST request to delete ModerationLogItem : {}", id);
        moderationLogItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
