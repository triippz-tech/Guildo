package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.domain.RootPage;
import com.triippztech.guildo.service.RootPageService;
import com.triippztech.guildo.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.triippztech.guildo.domain.RootPage}.
 */
@RestController
@RequestMapping("/api")
public class RootPageResource {

    private final Logger log = LoggerFactory.getLogger(RootPageResource.class);

    private static final String ENTITY_NAME = "blogRootPage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RootPageService rootPageService;

    public RootPageResource(RootPageService rootPageService) {
        this.rootPageService = rootPageService;
    }

    /**
     * {@code POST  /root-pages} : Create a new rootPage.
     *
     * @param rootPage the rootPage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rootPage, or with status {@code 400 (Bad Request)} if the rootPage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/root-pages")
    public ResponseEntity<RootPage> createRootPage(@Valid @RequestBody RootPage rootPage) throws URISyntaxException {
        log.debug("REST request to save RootPage : {}", rootPage);
        if (rootPage.getId() != null) {
            throw new BadRequestAlertException("A new rootPage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RootPage result = rootPageService.save(rootPage);
        return ResponseEntity.created(new URI("/api/root-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /root-pages} : Updates an existing rootPage.
     *
     * @param rootPage the rootPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rootPage,
     * or with status {@code 400 (Bad Request)} if the rootPage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rootPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/root-pages")
    public ResponseEntity<RootPage> updateRootPage(@Valid @RequestBody RootPage rootPage) throws URISyntaxException {
        log.debug("REST request to update RootPage : {}", rootPage);
        if (rootPage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RootPage result = rootPageService.save(rootPage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rootPage.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /root-pages} : get all the rootPages.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rootPages in body.
     */
    @GetMapping("/root-pages")
    public List<RootPage> getAllRootPages() {
        log.debug("REST request to get all RootPages");
        return rootPageService.findAll();
    }

    /**
     * {@code GET  /root-pages/:id} : get the "id" rootPage.
     *
     * @param id the id of the rootPage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rootPage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/root-pages/{id}")
    public ResponseEntity<RootPage> getRootPage(@PathVariable Long id) {
        log.debug("REST request to get RootPage : {}", id);
        Optional<RootPage> rootPage = rootPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rootPage);
    }

    /**
     * {@code DELETE  /root-pages/:id} : delete the "id" rootPage.
     *
     * @param id the id of the rootPage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/root-pages/{id}")
    public ResponseEntity<Void> deleteRootPage(@PathVariable Long id) {
        log.debug("REST request to delete RootPage : {}", id);
        rootPageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
