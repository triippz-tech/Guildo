package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.GuildServerStrike;
import com.triippztech.guildo.service.guild.GuildServerStrikeService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.GuildServerStrikeCriteria;
import com.triippztech.guildo.service.guild.GuildServerStrikeQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.triippztech.guildo.domain.GuildServerStrike}.
 */
@RestController
@RequestMapping("/api")
public class GuildServerStrikeResource {

    private final Logger log = LoggerFactory.getLogger(GuildServerStrikeResource.class);

    private static final String ENTITY_NAME = "botGuildServerStrike";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuildServerStrikeService guildServerStrikeService;

    private final GuildServerStrikeQueryService guildServerStrikeQueryService;

    public GuildServerStrikeResource(GuildServerStrikeService guildServerStrikeService, GuildServerStrikeQueryService guildServerStrikeQueryService) {
        this.guildServerStrikeService = guildServerStrikeService;
        this.guildServerStrikeQueryService = guildServerStrikeQueryService;
    }

    /**
     * {@code POST  /guild-server-strikes} : Create a new guildServerStrike.
     *
     * @param guildServerStrike the guildServerStrike to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guildServerStrike, or with status {@code 400 (Bad Request)} if the guildServerStrike has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guild-server-strikes")
    public ResponseEntity<GuildServerStrike> createGuildServerStrike(@Valid @RequestBody GuildServerStrike guildServerStrike) throws URISyntaxException {
        log.debug("REST request to save GuildServerStrike : {}", guildServerStrike);
        if (guildServerStrike.getId() != null) {
            throw new BadRequestAlertException("A new guildServerStrike cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(guildServerStrike.getDiscordUser())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        GuildServerStrike result = guildServerStrikeService.save(guildServerStrike);
        return ResponseEntity.created(new URI("/api/guild-server-strikes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guild-server-strikes} : Updates an existing guildServerStrike.
     *
     * @param guildServerStrike the guildServerStrike to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guildServerStrike,
     * or with status {@code 400 (Bad Request)} if the guildServerStrike is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guildServerStrike couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guild-server-strikes")
    public ResponseEntity<GuildServerStrike> updateGuildServerStrike(@Valid @RequestBody GuildServerStrike guildServerStrike) throws URISyntaxException {
        log.debug("REST request to update GuildServerStrike : {}", guildServerStrike);
        if (guildServerStrike.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildServerStrike result = guildServerStrikeService.save(guildServerStrike);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guildServerStrike.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guild-server-strikes} : get all the guildServerStrikes.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guildServerStrikes in body.
     */
    @GetMapping("/guild-server-strikes")
    public ResponseEntity<List<GuildServerStrike>> getAllGuildServerStrikes(GuildServerStrikeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GuildServerStrikes by criteria: {}", criteria);
        Page<GuildServerStrike> page = guildServerStrikeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /guild-server-strikes/count} : count all the guildServerStrikes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/guild-server-strikes/count")
    public ResponseEntity<Long> countGuildServerStrikes(GuildServerStrikeCriteria criteria) {
        log.debug("REST request to count GuildServerStrikes by criteria: {}", criteria);
        return ResponseEntity.ok().body(guildServerStrikeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /guild-server-strikes/:id} : get the "id" guildServerStrike.
     *
     * @param id the id of the guildServerStrike to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guildServerStrike, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guild-server-strikes/{id}")
    public ResponseEntity<GuildServerStrike> getGuildServerStrike(@PathVariable Long id) {
        log.debug("REST request to get GuildServerStrike : {}", id);
        Optional<GuildServerStrike> guildServerStrike = guildServerStrikeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildServerStrike);
    }

    /**
     * {@code DELETE  /guild-server-strikes/:id} : delete the "id" guildServerStrike.
     *
     * @param id the id of the guildServerStrike to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guild-server-strikes/{id}")
    public ResponseEntity<Void> deleteGuildServerStrike(@PathVariable Long id) {
        log.debug("REST request to delete GuildServerStrike : {}", id);
        guildServerStrikeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
