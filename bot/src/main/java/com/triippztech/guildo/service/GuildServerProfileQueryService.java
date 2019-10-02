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

import com.triippztech.guildo.domain.GuildServerProfile;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.GuildServerProfileRepository;
import com.triippztech.guildo.service.dto.GuildServerProfileCriteria;

/**
 * Service for executing complex queries for {@link GuildServerProfile} entities in the database.
 * The main input is a {@link GuildServerProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GuildServerProfile} or a {@link Page} of {@link GuildServerProfile} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GuildServerProfileQueryService extends QueryService<GuildServerProfile> {

    private final Logger log = LoggerFactory.getLogger(GuildServerProfileQueryService.class);

    private final GuildServerProfileRepository guildServerProfileRepository;

    public GuildServerProfileQueryService(GuildServerProfileRepository guildServerProfileRepository) {
        this.guildServerProfileRepository = guildServerProfileRepository;
    }

    /**
     * Return a {@link List} of {@link GuildServerProfile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GuildServerProfile> findByCriteria(GuildServerProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GuildServerProfile> specification = createSpecification(criteria);
        return guildServerProfileRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GuildServerProfile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildServerProfile> findByCriteria(GuildServerProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GuildServerProfile> specification = createSpecification(criteria);
        return guildServerProfileRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GuildServerProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GuildServerProfile> specification = createSpecification(criteria);
        return guildServerProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link GuildServerProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GuildServerProfile> createSpecification(GuildServerProfileCriteria criteria) {
        Specification<GuildServerProfile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GuildServerProfile_.id));
            }
            if (criteria.getGuildType() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildType(), GuildServerProfile_.guildType));
            }
            if (criteria.getPlayStyle() != null) {
                specification = specification.and(buildSpecification(criteria.getPlayStyle(), GuildServerProfile_.playStyle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), GuildServerProfile_.description));
            }
            if (criteria.getWebsite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWebsite(), GuildServerProfile_.website));
            }
            if (criteria.getDiscordUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDiscordUrl(), GuildServerProfile_.discordUrl));
            }
        }
        return specification;
    }
}
