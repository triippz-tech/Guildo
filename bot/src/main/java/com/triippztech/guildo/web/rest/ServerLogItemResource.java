package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.ServerLogItem;
import com.triippztech.guildo.service.ServerLogItemService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.ServerLogItemCriteria;
import com.triippztech.guildo.service.ServerLogItemQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.ServerLogItem}.
 */
@RestController
@RequestMapping("/api")
public class ServerLogItemResource {

    private final Logger log = LoggerFactory.getLogger(ServerLogItemResource.class);

    private static final String ENTITY_NAME = "botServerLogItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServerLogItemService serverLogItemService;

    private final ServerLogItemQueryService serverLogItemQueryService;

    public ServerLogItemResource(ServerLogItemService serverLogItemService, ServerLogItemQueryService serverLogItemQueryService) {
        this.serverLogItemService = serverLogItemService;
        this.serverLogItemQueryService = serverLogItemQueryService;
    }

    /**
     * {@code POST  /server-log-items} : Create a new serverLogItem.
     *
     * @param serverLogItem the serverLogItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serverLogItem, or with status {@code 400 (Bad Request)} if the serverLogItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/server-log-items")
    public ResponseEntity<ServerLogItem> createServerLogItem(@Valid @RequestBody ServerLogItem serverLogItem) throws URISyntaxException {
        log.debug("REST request to save ServerLogItem : {}", serverLogItem);
        if (serverLogItem.getId() != null) {
            throw new BadRequestAlertException("A new serverLogItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServerLogItem result = serverLogItemService.save(serverLogItem);
        return ResponseEntity.created(new URI("/api/server-log-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /server-log-items} : Updates an existing serverLogItem.
     *
     * @param serverLogItem the serverLogItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serverLogItem,
     * or with status {@code 400 (Bad Request)} if the serverLogItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serverLogItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/server-log-items")
    public ResponseEntity<ServerLogItem> updateServerLogItem(@Valid @RequestBody ServerLogItem serverLogItem) throws URISyntaxException {
        log.debug("REST request to update ServerLogItem : {}", serverLogItem);
        if (serverLogItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServerLogItem result = serverLogItemService.save(serverLogItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serverLogItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /server-log-items} : get all the serverLogItems.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serverLogItems in body.
     */
    @GetMapping("/server-log-items")
    public ResponseEntity<List<ServerLogItem>> getAllServerLogItems(ServerLogItemCriteria criteria) {
        log.debug("REST request to get ServerLogItems by criteria: {}", criteria);
        List<ServerLogItem> entityList = serverLogItemQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /server-log-items/count} : count all the serverLogItems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/server-log-items/count")
    public ResponseEntity<Long> countServerLogItems(ServerLogItemCriteria criteria) {
        log.debug("REST request to count ServerLogItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(serverLogItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /server-log-items/:id} : get the "id" serverLogItem.
     *
     * @param id the id of the serverLogItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serverLogItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/server-log-items/{id}")
    public ResponseEntity<ServerLogItem> getServerLogItem(@PathVariable Long id) {
        log.debug("REST request to get ServerLogItem : {}", id);
        Optional<ServerLogItem> serverLogItem = serverLogItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serverLogItem);
    }

    /**
     * {@code DELETE  /server-log-items/:id} : delete the "id" serverLogItem.
     *
     * @param id the id of the serverLogItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/server-log-items/{id}")
    public ResponseEntity<Void> deleteServerLogItem(@PathVariable Long id) {
        log.debug("REST request to delete ServerLogItem : {}", id);
        serverLogItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
