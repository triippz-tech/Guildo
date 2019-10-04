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

import com.triippztech.guildo.domain.AutoModAutoRaid;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.AutoModAutoRaidRepository;
import com.triippztech.guildo.service.dto.AutoModAutoRaidCriteria;

/**
 * Service for executing complex queries for {@link AutoModAutoRaid} entities in the database.
 * The main input is a {@link AutoModAutoRaidCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AutoModAutoRaid} or a {@link Page} of {@link AutoModAutoRaid} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AutoModAutoRaidQueryService extends QueryService<AutoModAutoRaid> {

    private final Logger log = LoggerFactory.getLogger(AutoModAutoRaidQueryService.class);

    private final AutoModAutoRaidRepository autoModAutoRaidRepository;

    public AutoModAutoRaidQueryService(AutoModAutoRaidRepository autoModAutoRaidRepository) {
        this.autoModAutoRaidRepository = autoModAutoRaidRepository;
    }

    /**
     * Return a {@link List} of {@link AutoModAutoRaid} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AutoModAutoRaid> findByCriteria(AutoModAutoRaidCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AutoModAutoRaid> specification = createSpecification(criteria);
        return autoModAutoRaidRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AutoModAutoRaid} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AutoModAutoRaid> findByCriteria(AutoModAutoRaidCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AutoModAutoRaid> specification = createSpecification(criteria);
        return autoModAutoRaidRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AutoModAutoRaidCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AutoModAutoRaid> specification = createSpecification(criteria);
        return autoModAutoRaidRepository.count(specification);
    }

    /**
     * Function to convert {@link AutoModAutoRaidCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AutoModAutoRaid> createSpecification(AutoModAutoRaidCriteria criteria) {
        Specification<AutoModAutoRaid> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AutoModAutoRaid_.id));
            }
            if (criteria.getAutoRaidEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getAutoRaidEnabled(), AutoModAutoRaid_.autoRaidEnabled));
            }
            if (criteria.getAutoRaidTimeThreshold() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAutoRaidTimeThreshold(), AutoModAutoRaid_.autoRaidTimeThreshold));
            }
        }
        return specification;
    }
}
