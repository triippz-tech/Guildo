package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.GuildPoll;
import com.triippztech.guildo.service.guild.GuildPollService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.GuildPollCriteria;
import com.triippztech.guildo.service.guild.GuildPollQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.GuildPoll}.
 */
@RestController
@RequestMapping("/api")
public class GuildPollResource {

    private final Logger log = LoggerFactory.getLogger(GuildPollResource.class);

    private static final String ENTITY_NAME = "botGuildPoll";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuildPollService guildPollService;

    private final GuildPollQueryService guildPollQueryService;

    public GuildPollResource(GuildPollService guildPollService, GuildPollQueryService guildPollQueryService) {
        this.guildPollService = guildPollService;
        this.guildPollQueryService = guildPollQueryService;
    }

    /**
     * {@code POST  /guild-polls} : Create a new guildPoll.
     *
     * @param guildPoll the guildPoll to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guildPoll, or with status {@code 400 (Bad Request)} if the guildPoll has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guild-polls")
    public ResponseEntity<GuildPoll> createGuildPoll(@Valid @RequestBody GuildPoll guildPoll) throws URISyntaxException {
        log.debug("REST request to save GuildPoll : {}", guildPoll);
        if (guildPoll.getId() != null) {
            throw new BadRequestAlertException("A new guildPoll cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildPoll result = guildPollService.save(guildPoll);
        return ResponseEntity.created(new URI("/api/guild-polls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guild-polls} : Updates an existing guildPoll.
     *
     * @param guildPoll the guildPoll to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guildPoll,
     * or with status {@code 400 (Bad Request)} if the guildPoll is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guildPoll couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guild-polls")
    public ResponseEntity<GuildPoll> updateGuildPoll(@Valid @RequestBody GuildPoll guildPoll) throws URISyntaxException {
        log.debug("REST request to update GuildPoll : {}", guildPoll);
        if (guildPoll.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildPoll result = guildPollService.save(guildPoll);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guildPoll.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guild-polls} : get all the guildPolls.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guildPolls in body.
     */
    @GetMapping("/guild-polls")
    public ResponseEntity<List<GuildPoll>> getAllGuildPolls(GuildPollCriteria criteria) {
        log.debug("REST request to get GuildPolls by criteria: {}", criteria);
        List<GuildPoll> entityList = guildPollQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /guild-polls/count} : count all the guildPolls.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/guild-polls/count")
    public ResponseEntity<Long> countGuildPolls(GuildPollCriteria criteria) {
        log.debug("REST request to count GuildPolls by criteria: {}", criteria);
        return ResponseEntity.ok().body(guildPollQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /guild-polls/:id} : get the "id" guildPoll.
     *
     * @param id the id of the guildPoll to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guildPoll, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guild-polls/{id}")
    public ResponseEntity<GuildPoll> getGuildPoll(@PathVariable Long id) {
        log.debug("REST request to get GuildPoll : {}", id);
        Optional<GuildPoll> guildPoll = guildPollService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildPoll);
    }

    /**
     * {@code DELETE  /guild-polls/:id} : delete the "id" guildPoll.
     *
     * @param id the id of the guildPoll to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guild-polls/{id}")
    public ResponseEntity<Void> deleteGuildPoll(@PathVariable Long id) {
        log.debug("REST request to delete GuildPoll : {}", id);
        guildPollService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
