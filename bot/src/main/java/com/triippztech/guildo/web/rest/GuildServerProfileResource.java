package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.GuildServerProfile;
import com.triippztech.guildo.service.server.GuildServerProfileService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.GuildServerProfileCriteria;
import com.triippztech.guildo.service.server.GuildServerProfileQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.triippztech.guildo.domain.GuildServerProfile}.
 */
@RestController
@RequestMapping("/api")
public class GuildServerProfileResource {

    private final Logger log = LoggerFactory.getLogger(GuildServerProfileResource.class);

    private static final String ENTITY_NAME = "botGuildServerProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuildServerProfileService guildServerProfileService;

    private final GuildServerProfileQueryService guildServerProfileQueryService;

    public GuildServerProfileResource(GuildServerProfileService guildServerProfileService, GuildServerProfileQueryService guildServerProfileQueryService) {
        this.guildServerProfileService = guildServerProfileService;
        this.guildServerProfileQueryService = guildServerProfileQueryService;
    }

    /**
     * {@code POST  /guild-server-profiles} : Create a new guildServerProfile.
     *
     * @param guildServerProfile the guildServerProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guildServerProfile, or with status {@code 400 (Bad Request)} if the guildServerProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guild-server-profiles")
    public ResponseEntity<GuildServerProfile> createGuildServerProfile(@RequestBody GuildServerProfile guildServerProfile) throws URISyntaxException {
        log.debug("REST request to save GuildServerProfile : {}", guildServerProfile);
        if (guildServerProfile.getId() != null) {
            throw new BadRequestAlertException("A new guildServerProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildServerProfile result = guildServerProfileService.save(guildServerProfile);
        return ResponseEntity.created(new URI("/api/guild-server-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guild-server-profiles} : Updates an existing guildServerProfile.
     *
     * @param guildServerProfile the guildServerProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guildServerProfile,
     * or with status {@code 400 (Bad Request)} if the guildServerProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guildServerProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guild-server-profiles")
    public ResponseEntity<GuildServerProfile> updateGuildServerProfile(@RequestBody GuildServerProfile guildServerProfile) throws URISyntaxException {
        log.debug("REST request to update GuildServerProfile : {}", guildServerProfile);
        if (guildServerProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildServerProfile result = guildServerProfileService.save(guildServerProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guildServerProfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guild-server-profiles} : get all the guildServerProfiles.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guildServerProfiles in body.
     */
    @GetMapping("/guild-server-profiles")
    public ResponseEntity<List<GuildServerProfile>> getAllGuildServerProfiles(GuildServerProfileCriteria criteria) {
        log.debug("REST request to get GuildServerProfiles by criteria: {}", criteria);
        List<GuildServerProfile> entityList = guildServerProfileQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /guild-server-profiles/count} : count all the guildServerProfiles.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/guild-server-profiles/count")
    public ResponseEntity<Long> countGuildServerProfiles(GuildServerProfileCriteria criteria) {
        log.debug("REST request to count GuildServerProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(guildServerProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /guild-server-profiles/:id} : get the "id" guildServerProfile.
     *
     * @param id the id of the guildServerProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guildServerProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guild-server-profiles/{id}")
    public ResponseEntity<GuildServerProfile> getGuildServerProfile(@PathVariable Long id) {
        log.debug("REST request to get GuildServerProfile : {}", id);
        Optional<GuildServerProfile> guildServerProfile = guildServerProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildServerProfile);
    }

    /**
     * {@code DELETE  /guild-server-profiles/:id} : delete the "id" guildServerProfile.
     *
     * @param id the id of the guildServerProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guild-server-profiles/{id}")
    public ResponseEntity<Void> deleteGuildServerProfile(@PathVariable Long id) {
        log.debug("REST request to delete GuildServerProfile : {}", id);
        guildServerProfileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
