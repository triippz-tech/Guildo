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

import com.triippztech.guildo.domain.GuildPollItem;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.GuildPollItemRepository;
import com.triippztech.guildo.service.dto.GuildPollItemCriteria;

/**
 * Service for executing complex queries for {@link GuildPollItem} entities in the database.
 * The main input is a {@link GuildPollItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GuildPollItem} or a {@link Page} of {@link GuildPollItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GuildPollItemQueryService extends QueryService<GuildPollItem> {

    private final Logger log = LoggerFactory.getLogger(GuildPollItemQueryService.class);

    private final GuildPollItemRepository guildPollItemRepository;

    public GuildPollItemQueryService(GuildPollItemRepository guildPollItemRepository) {
        this.guildPollItemRepository = guildPollItemRepository;
    }

    /**
     * Return a {@link List} of {@link GuildPollItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GuildPollItem> findByCriteria(GuildPollItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GuildPollItem> specification = createSpecification(criteria);
        return guildPollItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GuildPollItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildPollItem> findByCriteria(GuildPollItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GuildPollItem> specification = createSpecification(criteria);
        return guildPollItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GuildPollItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GuildPollItem> specification = createSpecification(criteria);
        return guildPollItemRepository.count(specification);
    }

    /**
     * Function to convert {@link GuildPollItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GuildPollItem> createSpecification(GuildPollItemCriteria criteria) {
        Specification<GuildPollItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GuildPollItem_.id));
            }
            if (criteria.getItemName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemName(), GuildPollItem_.itemName));
            }
            if (criteria.getVotes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVotes(), GuildPollItem_.votes));
            }
        }
        return specification;
    }
}
