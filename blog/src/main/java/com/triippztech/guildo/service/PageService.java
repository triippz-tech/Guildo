package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.Page;
import com.triippztech.guildo.repository.PageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Page}.
 */
@Service
@Transactional
public class PageService {

    private final Logger log = LoggerFactory.getLogger(PageService.class);

    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    /**
     * Save a page.
     *
     * @param page the entity to save.
     * @return the persisted entity.
     */
    public Page save(Page page) {
        log.debug("Request to save Page : {}", page);
        return pageRepository.save(page);
    }

    /**
     * Get all the pages.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Page> findAll() {
        log.debug("Request to get all Pages");
        return pageRepository.findAll();
    }


    /**
     * Get one page by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Page> findOne(Long id) {
        log.debug("Request to get Page : {}", id);
        return pageRepository.findById(id);
    }

    /**
     * Delete the page by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Page : {}", id);
        pageRepository.deleteById(id);
    }
}
