package com.triippztech.guildo.service.moderation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.triippztech.guildo.domain.AutoModIgnore;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.AutoModIgnoreRepository;
import com.triippztech.guildo.service.dto.AutoModIgnoreCriteria;

/**
 * Service for executing complex queries for {@link AutoModIgnore} entities in the database.
 * The main input is a {@link AutoModIgnoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AutoModIgnore} or a {@link Page} of {@link AutoModIgnore} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AutoModIgnoreQueryService extends QueryService<AutoModIgnore> {

    private final Logger log = LoggerFactory.getLogger(AutoModIgnoreQueryService.class);

    private final AutoModIgnoreRepository autoModIgnoreRepository;

    public AutoModIgnoreQueryService(AutoModIgnoreRepository autoModIgnoreRepository) {
        this.autoModIgnoreRepository = autoModIgnoreRepository;
    }

    /**
     * Return a {@link List} of {@link AutoModIgnore} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AutoModIgnore> findByCriteria(AutoModIgnoreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AutoModIgnore> specification = createSpecification(criteria);
        return autoModIgnoreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AutoModIgnore} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AutoModIgnore> findByCriteria(AutoModIgnoreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AutoModIgnore> specification = createSpecification(criteria);
        return autoModIgnoreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AutoModIgnoreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AutoModIgnore> specification = createSpecification(criteria);
        return autoModIgnoreRepository.count(specification);
    }

    /**
     * Function to convert {@link AutoModIgnoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AutoModIgnore> createSpecification(AutoModIgnoreCriteria criteria) {
        Specification<AutoModIgnore> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AutoModIgnore_.id));
            }
            if (criteria.getRoleId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRoleId(), AutoModIgnore_.roleId));
            }
            if (criteria.getChannelId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChannelId(), AutoModIgnore_.channelId));
            }
        }
        return specification;
    }
}
