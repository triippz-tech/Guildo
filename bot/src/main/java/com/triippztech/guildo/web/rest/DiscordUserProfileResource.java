package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.DiscordUserProfile;
import com.triippztech.guildo.service.user.DiscordUserProfileService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.DiscordUserProfileCriteria;
import com.triippztech.guildo.service.user.DiscordUserProfileQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.DiscordUserProfile}.
 */
@RestController
@RequestMapping("/api")
public class DiscordUserProfileResource {

    private final Logger log = LoggerFactory.getLogger(DiscordUserProfileResource.class);

    private static final String ENTITY_NAME = "botDiscordUserProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DiscordUserProfileService discordUserProfileService;

    private final DiscordUserProfileQueryService discordUserProfileQueryService;

    public DiscordUserProfileResource(DiscordUserProfileService discordUserProfileService, DiscordUserProfileQueryService discordUserProfileQueryService) {
        this.discordUserProfileService = discordUserProfileService;
        this.discordUserProfileQueryService = discordUserProfileQueryService;
    }

    /**
     * {@code POST  /discord-user-profiles} : Create a new discordUserProfile.
     *
     * @param discordUserProfile the discordUserProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new discordUserProfile, or with status {@code 400 (Bad Request)} if the discordUserProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/discord-user-profiles")
    public ResponseEntity<DiscordUserProfile> createDiscordUserProfile(@RequestBody DiscordUserProfile discordUserProfile) throws URISyntaxException {
        log.debug("REST request to save DiscordUserProfile : {}", discordUserProfile);
        if (discordUserProfile.getId() != null) {
            throw new BadRequestAlertException("A new discordUserProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DiscordUserProfile result = discordUserProfileService.save(discordUserProfile);
        return ResponseEntity.created(new URI("/api/discord-user-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /discord-user-profiles} : Updates an existing discordUserProfile.
     *
     * @param discordUserProfile the discordUserProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated discordUserProfile,
     * or with status {@code 400 (Bad Request)} if the discordUserProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the discordUserProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/discord-user-profiles")
    public ResponseEntity<DiscordUserProfile> updateDiscordUserProfile(@RequestBody DiscordUserProfile discordUserProfile) throws URISyntaxException {
        log.debug("REST request to update DiscordUserProfile : {}", discordUserProfile);
        if (discordUserProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DiscordUserProfile result = discordUserProfileService.save(discordUserProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, discordUserProfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /discord-user-profiles} : get all the discordUserProfiles.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of discordUserProfiles in body.
     */
    @GetMapping("/discord-user-profiles")
    public ResponseEntity<List<DiscordUserProfile>> getAllDiscordUserProfiles(DiscordUserProfileCriteria criteria) {
        log.debug("REST request to get DiscordUserProfiles by criteria: {}", criteria);
        List<DiscordUserProfile> entityList = discordUserProfileQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /discord-user-profiles/count} : count all the discordUserProfiles.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/discord-user-profiles/count")
    public ResponseEntity<Long> countDiscordUserProfiles(DiscordUserProfileCriteria criteria) {
        log.debug("REST request to count DiscordUserProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(discordUserProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /discord-user-profiles/:id} : get the "id" discordUserProfile.
     *
     * @param id the id of the discordUserProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the discordUserProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/discord-user-profiles/{id}")
    public ResponseEntity<DiscordUserProfile> getDiscordUserProfile(@PathVariable Long id) {
        log.debug("REST request to get DiscordUserProfile : {}", id);
        Optional<DiscordUserProfile> discordUserProfile = discordUserProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(discordUserProfile);
    }

    /**
     * {@code DELETE  /discord-user-profiles/:id} : delete the "id" discordUserProfile.
     *
     * @param id the id of the discordUserProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/discord-user-profiles/{id}")
    public ResponseEntity<Void> deleteDiscordUserProfile(@PathVariable Long id) {
        log.debug("REST request to delete DiscordUserProfile : {}", id);
        discordUserProfileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
