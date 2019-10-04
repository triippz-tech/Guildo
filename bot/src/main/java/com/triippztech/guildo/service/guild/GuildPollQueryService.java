package com.triippztech.guildo.service.guild;

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

import com.triippztech.guildo.domain.GuildPoll;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.GuildPollRepository;
import com.triippztech.guildo.service.dto.GuildPollCriteria;

/**
 * Service for executing complex queries for {@link GuildPoll} entities in the database.
 * The main input is a {@link GuildPollCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GuildPoll} or a {@link Page} of {@link GuildPoll} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GuildPollQueryService extends QueryService<GuildPoll> {

    private final Logger log = LoggerFactory.getLogger(GuildPollQueryService.class);

    private final GuildPollRepository guildPollRepository;

    public GuildPollQueryService(GuildPollRepository guildPollRepository) {
        this.guildPollRepository = guildPollRepository;
    }

    /**
     * Return a {@link List} of {@link GuildPoll} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GuildPoll> findByCriteria(GuildPollCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GuildPoll> specification = createSpecification(criteria);
        return guildPollRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GuildPoll} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildPoll> findByCriteria(GuildPollCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GuildPoll> specification = createSpecification(criteria);
        return guildPollRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GuildPollCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GuildPoll> specification = createSpecification(criteria);
        return guildPollRepository.count(specification);
    }

    /**
     * Function to convert {@link GuildPollCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GuildPoll> createSpecification(GuildPollCriteria criteria) {
        Specification<GuildPoll> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GuildPoll_.id));
            }
            if (criteria.getPollName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPollName(), GuildPoll_.pollName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), GuildPoll_.description));
            }
            if (criteria.getTextChannelId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTextChannelId(), GuildPoll_.textChannelId));
            }
            if (criteria.getFinishTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFinishTime(), GuildPoll_.finishTime));
            }
            if (criteria.getCompleted() != null) {
                specification = specification.and(buildSpecification(criteria.getCompleted(), GuildPoll_.completed));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), GuildPoll_.guildId));
            }
            if (criteria.getPollItemsId() != null) {
                specification = specification.and(buildSpecification(criteria.getPollItemsId(),
                    root -> root.join(GuildPoll_.pollItems, JoinType.LEFT).get(GuildPollItem_.id)));
            }
            if (criteria.getPollServerId() != null) {
                specification = specification.and(buildSpecification(criteria.getPollServerId(),
                    root -> root.join(GuildPoll_.pollServer, JoinType.LEFT).get(GuildServer_.id)));
            }
        }
        return specification;
    }
}
