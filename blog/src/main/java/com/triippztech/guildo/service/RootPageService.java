package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.RootPage;
import com.triippztech.guildo.repository.RootPageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link RootPage}.
 */
@Service
@Transactional
public class RootPageService {

    private final Logger log = LoggerFactory.getLogger(RootPageService.class);

    private final RootPageRepository rootPageRepository;

    public RootPageService(RootPageRepository rootPageRepository) {
        this.rootPageRepository = rootPageRepository;
    }

    /**
     * Save a rootPage.
     *
     * @param rootPage the entity to save.
     * @return the persisted entity.
     */
    public RootPage save(RootPage rootPage) {
        log.debug("Request to save RootPage : {}", rootPage);
        return rootPageRepository.save(rootPage);
    }

    /**
     * Get all the rootPages.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RootPage> findAll() {
        log.debug("Request to get all RootPages");
        return rootPageRepository.findAll();
    }


    /**
     * Get one rootPage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RootPage> findOne(Long id) {
        log.debug("Request to get RootPage : {}", id);
        return rootPageRepository.findById(id);
    }

    /**
     * Delete the rootPage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RootPage : {}", id);
        rootPageRepository.deleteById(id);
    }
}
