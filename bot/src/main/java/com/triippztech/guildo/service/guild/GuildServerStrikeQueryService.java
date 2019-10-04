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

import com.triippztech.guildo.domain.GuildServerStrike;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.GuildServerStrikeRepository;
import com.triippztech.guildo.service.dto.GuildServerStrikeCriteria;

/**
 * Service for executing complex queries for {@link GuildServerStrike} entities in the database.
 * The main input is a {@link GuildServerStrikeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GuildServerStrike} or a {@link Page} of {@link GuildServerStrike} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GuildServerStrikeQueryService extends QueryService<GuildServerStrike> {

    private final Logger log = LoggerFactory.getLogger(GuildServerStrikeQueryService.class);

    private final GuildServerStrikeRepository guildServerStrikeRepository;

    public GuildServerStrikeQueryService(GuildServerStrikeRepository guildServerStrikeRepository) {
        this.guildServerStrikeRepository = guildServerStrikeRepository;
    }

    /**
     * Return a {@link List} of {@link GuildServerStrike} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GuildServerStrike> findByCriteria(GuildServerStrikeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GuildServerStrike> specification = createSpecification(criteria);
        return guildServerStrikeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GuildServerStrike} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildServerStrike> findByCriteria(GuildServerStrikeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GuildServerStrike> specification = createSpecification(criteria);
        return guildServerStrikeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GuildServerStrikeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GuildServerStrike> specification = createSpecification(criteria);
        return guildServerStrikeRepository.count(specification);
    }

    /**
     * Function to convert {@link GuildServerStrikeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GuildServerStrike> createSpecification(GuildServerStrikeCriteria criteria) {
        Specification<GuildServerStrike> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GuildServerStrike_.id));
            }
            if (criteria.getCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCount(), GuildServerStrike_.count));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), GuildServerStrike_.userId));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), GuildServerStrike_.guildId));
            }
            if (criteria.getDiscordUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscordUserId(),
                    root -> root.join(GuildServerStrike_.discordUser, JoinType.LEFT).get(DiscordUser_.id)));
            }
        }
        return specification;
    }
}
