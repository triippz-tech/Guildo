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

import com.triippztech.guildo.domain.GuildEvent;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.GuildEventRepository;
import com.triippztech.guildo.service.dto.GuildEventCriteria;

/**
 * Service for executing complex queries for {@link GuildEvent} entities in the database.
 * The main input is a {@link GuildEventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GuildEvent} or a {@link Page} of {@link GuildEvent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GuildEventQueryService extends QueryService<GuildEvent> {

    private final Logger log = LoggerFactory.getLogger(GuildEventQueryService.class);

    private final GuildEventRepository guildEventRepository;

    public GuildEventQueryService(GuildEventRepository guildEventRepository) {
        this.guildEventRepository = guildEventRepository;
    }

    /**
     * Return a {@link List} of {@link GuildEvent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GuildEvent> findByCriteria(GuildEventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GuildEvent> specification = createSpecification(criteria);
        return guildEventRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GuildEvent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildEvent> findByCriteria(GuildEventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GuildEvent> specification = createSpecification(criteria);
        return guildEventRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GuildEventCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GuildEvent> specification = createSpecification(criteria);
        return guildEventRepository.count(specification);
    }

    /**
     * Function to convert {@link GuildEventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GuildEvent> createSpecification(GuildEventCriteria criteria) {
        Specification<GuildEvent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GuildEvent_.id));
            }
            if (criteria.getEventName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventName(), GuildEvent_.eventName));
            }
            if (criteria.getEventImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventImageUrl(), GuildEvent_.eventImageUrl));
            }
            if (criteria.getEventStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventStart(), GuildEvent_.eventStart));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), GuildEvent_.guildId));
            }
            if (criteria.getEventGuildId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventGuildId(),
                    root -> root.join(GuildEvent_.eventGuild, JoinType.LEFT).get(GuildServer_.id)));
            }
        }
        return specification;
    }
}
