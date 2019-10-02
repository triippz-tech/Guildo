package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.service.GuildServerService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.GuildServerCriteria;
import com.triippztech.guildo.service.GuildServerQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.triippztech.guildo.domain.GuildServer}.
 */
@RestController
@RequestMapping("/api")
public class GuildServerResource {

    private final Logger log = LoggerFactory.getLogger(GuildServerResource.class);

    private static final String ENTITY_NAME = "botGuildServer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuildServerService guildServerService;

    private final GuildServerQueryService guildServerQueryService;

    public GuildServerResource(GuildServerService guildServerService, GuildServerQueryService guildServerQueryService) {
        this.guildServerService = guildServerService;
        this.guildServerQueryService = guildServerQueryService;
    }

    /**
     * {@code POST  /guild-servers} : Create a new guildServer.
     *
     * @param guildServer the guildServer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guildServer, or with status {@code 400 (Bad Request)} if the guildServer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guild-servers")
    public ResponseEntity<GuildServer> createGuildServer(@Valid @RequestBody GuildServer guildServer) throws URISyntaxException {
        log.debug("REST request to save GuildServer : {}", guildServer);
        if (guildServer.getId() != null) {
            throw new BadRequestAlertException("A new guildServer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildServer result = guildServerService.save(guildServer);
        return ResponseEntity.created(new URI("/api/guild-servers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guild-servers} : Updates an existing guildServer.
     *
     * @param guildServer the guildServer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guildServer,
     * or with status {@code 400 (Bad Request)} if the guildServer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guildServer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guild-servers")
    public ResponseEntity<GuildServer> updateGuildServer(@Valid @RequestBody GuildServer guildServer) throws URISyntaxException {
        log.debug("REST request to update GuildServer : {}", guildServer);
        if (guildServer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildServer result = guildServerService.save(guildServer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guildServer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guild-servers} : get all the guildServers.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guildServers in body.
     */
    @GetMapping("/guild-servers")
    public ResponseEntity<List<GuildServer>> getAllGuildServers(GuildServerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GuildServers by criteria: {}", criteria);
        Page<GuildServer> page = guildServerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /guild-servers/count} : count all the guildServers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/guild-servers/count")
    public ResponseEntity<Long> countGuildServers(GuildServerCriteria criteria) {
        log.debug("REST request to count GuildServers by criteria: {}", criteria);
        return ResponseEntity.ok().body(guildServerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /guild-servers/:id} : get the "id" guildServer.
     *
     * @param id the id of the guildServer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guildServer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guild-servers/{id}")
    public ResponseEntity<GuildServer> getGuildServer(@PathVariable Long id) {
        log.debug("REST request to get GuildServer : {}", id);
        Optional<GuildServer> guildServer = guildServerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildServer);
    }

    /**
     * {@code DELETE  /guild-servers/:id} : delete the "id" guildServer.
     *
     * @param id the id of the guildServer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guild-servers/{id}")
    public ResponseEntity<Void> deleteGuildServer(@PathVariable Long id) {
        log.debug("REST request to delete GuildServer : {}", id);
        guildServerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
