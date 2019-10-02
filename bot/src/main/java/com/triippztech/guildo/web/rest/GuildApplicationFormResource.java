package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.GuildApplicationForm;
import com.triippztech.guildo.service.GuildApplicationFormService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;
import com.triippztech.guildo.service.dto.GuildApplicationFormCriteria;
import com.triippztech.guildo.service.GuildApplicationFormQueryService;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.GuildApplicationForm}.
 */
@RestController
@RequestMapping("/api")
public class GuildApplicationFormResource {

    private final Logger log = LoggerFactory.getLogger(GuildApplicationFormResource.class);

    private static final String ENTITY_NAME = "botGuildApplicationForm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuildApplicationFormService guildApplicationFormService;

    private final GuildApplicationFormQueryService guildApplicationFormQueryService;

    public GuildApplicationFormResource(GuildApplicationFormService guildApplicationFormService, GuildApplicationFormQueryService guildApplicationFormQueryService) {
        this.guildApplicationFormService = guildApplicationFormService;
        this.guildApplicationFormQueryService = guildApplicationFormQueryService;
    }

    /**
     * {@code POST  /guild-application-forms} : Create a new guildApplicationForm.
     *
     * @param guildApplicationForm the guildApplicationForm to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guildApplicationForm, or with status {@code 400 (Bad Request)} if the guildApplicationForm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guild-application-forms")
    public ResponseEntity<GuildApplicationForm> createGuildApplicationForm(@Valid @RequestBody GuildApplicationForm guildApplicationForm) throws URISyntaxException {
        log.debug("REST request to save GuildApplicationForm : {}", guildApplicationForm);
        if (guildApplicationForm.getId() != null) {
            throw new BadRequestAlertException("A new guildApplicationForm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildApplicationForm result = guildApplicationFormService.save(guildApplicationForm);
        return ResponseEntity.created(new URI("/api/guild-application-forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guild-application-forms} : Updates an existing guildApplicationForm.
     *
     * @param guildApplicationForm the guildApplicationForm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guildApplicationForm,
     * or with status {@code 400 (Bad Request)} if the guildApplicationForm is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guildApplicationForm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guild-application-forms")
    public ResponseEntity<GuildApplicationForm> updateGuildApplicationForm(@Valid @RequestBody GuildApplicationForm guildApplicationForm) throws URISyntaxException {
        log.debug("REST request to update GuildApplicationForm : {}", guildApplicationForm);
        if (guildApplicationForm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildApplicationForm result = guildApplicationFormService.save(guildApplicationForm);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guildApplicationForm.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guild-application-forms} : get all the guildApplicationForms.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guildApplicationForms in body.
     */
    @GetMapping("/guild-application-forms")
    public ResponseEntity<List<GuildApplicationForm>> getAllGuildApplicationForms(GuildApplicationFormCriteria criteria) {
        log.debug("REST request to get GuildApplicationForms by criteria: {}", criteria);
        List<GuildApplicationForm> entityList = guildApplicationFormQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /guild-application-forms/count} : count all the guildApplicationForms.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/guild-application-forms/count")
    public ResponseEntity<Long> countGuildApplicationForms(GuildApplicationFormCriteria criteria) {
        log.debug("REST request to count GuildApplicationForms by criteria: {}", criteria);
        return ResponseEntity.ok().body(guildApplicationFormQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /guild-application-forms/:id} : get the "id" guildApplicationForm.
     *
     * @param id the id of the guildApplicationForm to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guildApplicationForm, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guild-application-forms/{id}")
    public ResponseEntity<GuildApplicationForm> getGuildApplicationForm(@PathVariable Long id) {
        log.debug("REST request to get GuildApplicationForm : {}", id);
        Optional<GuildApplicationForm> guildApplicationForm = guildApplicationFormService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildApplicationForm);
    }

    /**
     * {@code DELETE  /guild-application-forms/:id} : delete the "id" guildApplicationForm.
     *
     * @param id the id of the guildApplicationForm to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guild-application-forms/{id}")
    public ResponseEntity<Void> deleteGuildApplicationForm(@PathVariable Long id) {
        log.debug("REST request to delete GuildApplicationForm : {}", id);
        guildApplicationFormService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
