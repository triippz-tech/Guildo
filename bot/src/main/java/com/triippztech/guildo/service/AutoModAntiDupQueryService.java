package com.triippztech.guildo.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.triippztech.guildo.domain.AutoModAntiDup;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.AutoModAntiDupRepository;
import com.triippztech.guildo.service.dto.AutoModAntiDupCriteria;

/**
 * Service for executing complex queries for {@link AutoModAntiDup} entities in the database.
 * The main input is a {@link AutoModAntiDupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AutoModAntiDup} or a {@link Page} of {@link AutoModAntiDup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AutoModAntiDupQueryService extends QueryService<AutoModAntiDup> {

    private final Logger log = LoggerFactory.getLogger(AutoModAntiDupQueryService.class);

    private final AutoModAntiDupRepository autoModAntiDupRepository;

    public AutoModAntiDupQueryService(AutoModAntiDupRepository autoModAntiDupRepository) {
        this.autoModAntiDupRepository = autoModAntiDupRepository;
    }

    /**
     * Return a {@link List} of {@link AutoModAntiDup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AutoModAntiDup> findByCriteria(AutoModAntiDupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AutoModAntiDup> specification = createSpecification(criteria);
        return autoModAntiDupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AutoModAntiDup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AutoModAntiDup> findByCriteria(AutoModAntiDupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AutoModAntiDup> specification = createSpecification(criteria);
        return autoModAntiDupRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AutoModAntiDupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AutoModAntiDup> specification = createSpecification(criteria);
        return autoModAntiDupRepository.count(specification);
    }

    /**
     * Function to convert {@link AutoModAntiDupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AutoModAntiDup> createSpecification(AutoModAntiDupCriteria criteria) {
        Specification<AutoModAntiDup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AutoModAntiDup_.id));
            }
            if (criteria.getDeleteThreshold() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeleteThreshold(), AutoModAntiDup_.deleteThreshold));
            }
            if (criteria.getDupsToPunish() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDupsToPunish(), AutoModAntiDup_.dupsToPunish));
            }
        }
        return specification;
    }
}
