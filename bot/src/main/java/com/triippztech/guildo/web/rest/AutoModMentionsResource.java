package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.AutoModMentions;
import com.triippztech.guildo.service.moderation.AutoModMentionsService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.AutoModMentionsCriteria;
import com.triippztech.guildo.service.moderation.AutoModMentionsQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.AutoModMentions}.
 */
@RestController
@RequestMapping("/api")
public class AutoModMentionsResource {

    private final Logger log = LoggerFactory.getLogger(AutoModMentionsResource.class);

    private static final String ENTITY_NAME = "botAutoModMentions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutoModMentionsService autoModMentionsService;

    private final AutoModMentionsQueryService autoModMentionsQueryService;

    public AutoModMentionsResource(AutoModMentionsService autoModMentionsService, AutoModMentionsQueryService autoModMentionsQueryService) {
        this.autoModMentionsService = autoModMentionsService;
        this.autoModMentionsQueryService = autoModMentionsQueryService;
    }

    /**
     * {@code POST  /auto-mod-mentions} : Create a new autoModMentions.
     *
     * @param autoModMentions the autoModMentions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autoModMentions, or with status {@code 400 (Bad Request)} if the autoModMentions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/auto-mod-mentions")
    public ResponseEntity<AutoModMentions> createAutoModMentions(@Valid @RequestBody AutoModMentions autoModMentions) throws URISyntaxException {
        log.debug("REST request to save AutoModMentions : {}", autoModMentions);
        if (autoModMentions.getId() != null) {
            throw new BadRequestAlertException("A new autoModMentions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AutoModMentions result = autoModMentionsService.save(autoModMentions);
        return ResponseEntity.created(new URI("/api/auto-mod-mentions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /auto-mod-mentions} : Updates an existing autoModMentions.
     *
     * @param autoModMentions the autoModMentions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoModMentions,
     * or with status {@code 400 (Bad Request)} if the autoModMentions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autoModMentions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/auto-mod-mentions")
    public ResponseEntity<AutoModMentions> updateAutoModMentions(@Valid @RequestBody AutoModMentions autoModMentions) throws URISyntaxException {
        log.debug("REST request to update AutoModMentions : {}", autoModMentions);
        if (autoModMentions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AutoModMentions result = autoModMentionsService.save(autoModMentions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autoModMentions.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /auto-mod-mentions} : get all the autoModMentions.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autoModMentions in body.
     */
    @GetMapping("/auto-mod-mentions")
    public ResponseEntity<List<AutoModMentions>> getAllAutoModMentions(AutoModMentionsCriteria criteria) {
        log.debug("REST request to get AutoModMentions by criteria: {}", criteria);
        List<AutoModMentions> entityList = autoModMentionsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /auto-mod-mentions/count} : count all the autoModMentions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/auto-mod-mentions/count")
    public ResponseEntity<Long> countAutoModMentions(AutoModMentionsCriteria criteria) {
        log.debug("REST request to count AutoModMentions by criteria: {}", criteria);
        return ResponseEntity.ok().body(autoModMentionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /auto-mod-mentions/:id} : get the "id" autoModMentions.
     *
     * @param id the id of the autoModMentions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autoModMentions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/auto-mod-mentions/{id}")
    public ResponseEntity<AutoModMentions> getAutoModMentions(@PathVariable Long id) {
        log.debug("REST request to get AutoModMentions : {}", id);
        Optional<AutoModMentions> autoModMentions = autoModMentionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autoModMentions);
    }

    /**
     * {@code DELETE  /auto-mod-mentions/:id} : delete the "id" autoModMentions.
     *
     * @param id the id of the autoModMentions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/auto-mod-mentions/{id}")
    public ResponseEntity<Void> deleteAutoModMentions(@PathVariable Long id) {
        log.debug("REST request to delete AutoModMentions : {}", id);
        autoModMentionsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
