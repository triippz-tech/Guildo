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

import com.triippztech.guildo.domain.AutoModMentions;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.AutoModMentionsRepository;
import com.triippztech.guildo.service.dto.AutoModMentionsCriteria;

/**
 * Service for executing complex queries for {@link AutoModMentions} entities in the database.
 * The main input is a {@link AutoModMentionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AutoModMentions} or a {@link Page} of {@link AutoModMentions} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AutoModMentionsQueryService extends QueryService<AutoModMentions> {

    private final Logger log = LoggerFactory.getLogger(AutoModMentionsQueryService.class);

    private final AutoModMentionsRepository autoModMentionsRepository;

    public AutoModMentionsQueryService(AutoModMentionsRepository autoModMentionsRepository) {
        this.autoModMentionsRepository = autoModMentionsRepository;
    }

    /**
     * Return a {@link List} of {@link AutoModMentions} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AutoModMentions> findByCriteria(AutoModMentionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AutoModMentions> specification = createSpecification(criteria);
        return autoModMentionsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AutoModMentions} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AutoModMentions> findByCriteria(AutoModMentionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AutoModMentions> specification = createSpecification(criteria);
        return autoModMentionsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AutoModMentionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AutoModMentions> specification = createSpecification(criteria);
        return autoModMentionsRepository.count(specification);
    }

    /**
     * Function to convert {@link AutoModMentionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AutoModMentions> createSpecification(AutoModMentionsCriteria criteria) {
        Specification<AutoModMentions> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AutoModMentions_.id));
            }
            if (criteria.getMaxMentions() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxMentions(), AutoModMentions_.maxMentions));
            }
            if (criteria.getMaxMsgLines() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxMsgLines(), AutoModMentions_.maxMsgLines));
            }
            if (criteria.getMaxRoleMentions() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxRoleMentions(), AutoModMentions_.maxRoleMentions));
            }
        }
        return specification;
    }
}
