package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.GuildServerSettings;
import com.triippztech.guildo.service.server.GuildServerSettingsService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.GuildServerSettingsCriteria;
import com.triippztech.guildo.service.server.GuildServerSettingsQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.GuildServerSettings}.
 */
@RestController
@RequestMapping("/api")
public class GuildServerSettingsResource {

    private final Logger log = LoggerFactory.getLogger(GuildServerSettingsResource.class);

    private static final String ENTITY_NAME = "botGuildServerSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuildServerSettingsService guildServerSettingsService;

    private final GuildServerSettingsQueryService guildServerSettingsQueryService;

    public GuildServerSettingsResource(GuildServerSettingsService guildServerSettingsService, GuildServerSettingsQueryService guildServerSettingsQueryService) {
        this.guildServerSettingsService = guildServerSettingsService;
        this.guildServerSettingsQueryService = guildServerSettingsQueryService;
    }

    /**
     * {@code POST  /guild-server-settings} : Create a new guildServerSettings.
     *
     * @param guildServerSettings the guildServerSettings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guildServerSettings, or with status {@code 400 (Bad Request)} if the guildServerSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guild-server-settings")
    public ResponseEntity<GuildServerSettings> createGuildServerSettings(@Valid @RequestBody GuildServerSettings guildServerSettings) throws URISyntaxException {
        log.debug("REST request to save GuildServerSettings : {}", guildServerSettings);
        if (guildServerSettings.getId() != null) {
            throw new BadRequestAlertException("A new guildServerSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildServerSettings result = guildServerSettingsService.save(guildServerSettings);
        return ResponseEntity.created(new URI("/api/guild-server-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guild-server-settings} : Updates an existing guildServerSettings.
     *
     * @param guildServerSettings the guildServerSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guildServerSettings,
     * or with status {@code 400 (Bad Request)} if the guildServerSettings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guildServerSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guild-server-settings")
    public ResponseEntity<GuildServerSettings> updateGuildServerSettings(@Valid @RequestBody GuildServerSettings guildServerSettings) throws URISyntaxException {
        log.debug("REST request to update GuildServerSettings : {}", guildServerSettings);
        if (guildServerSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildServerSettings result = guildServerSettingsService.save(guildServerSettings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guildServerSettings.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guild-server-settings} : get all the guildServerSettings.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guildServerSettings in body.
     */
    @GetMapping("/guild-server-settings")
    public ResponseEntity<List<GuildServerSettings>> getAllGuildServerSettings(GuildServerSettingsCriteria criteria) {
        log.debug("REST request to get GuildServerSettings by criteria: {}", criteria);
        List<GuildServerSettings> entityList = guildServerSettingsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /guild-server-settings/count} : count all the guildServerSettings.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/guild-server-settings/count")
    public ResponseEntity<Long> countGuildServerSettings(GuildServerSettingsCriteria criteria) {
        log.debug("REST request to count GuildServerSettings by criteria: {}", criteria);
        return ResponseEntity.ok().body(guildServerSettingsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /guild-server-settings/:id} : get the "id" guildServerSettings.
     *
     * @param id the id of the guildServerSettings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guildServerSettings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guild-server-settings/{id}")
    public ResponseEntity<GuildServerSettings> getGuildServerSettings(@PathVariable Long id) {
        log.debug("REST request to get GuildServerSettings : {}", id);
        Optional<GuildServerSettings> guildServerSettings = guildServerSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildServerSettings);
    }

    /**
     * {@code DELETE  /guild-server-settings/:id} : delete the "id" guildServerSettings.
     *
     * @param id the id of the guildServerSettings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guild-server-settings/{id}")
    public ResponseEntity<Void> deleteGuildServerSettings(@PathVariable Long id) {
        log.debug("REST request to delete GuildServerSettings : {}", id);
        guildServerSettingsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
