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

import com.triippztech.guildo.domain.GuildApplication;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.GuildApplicationRepository;
import com.triippztech.guildo.service.dto.GuildApplicationCriteria;

/**
 * Service for executing complex queries for {@link GuildApplication} entities in the database.
 * The main input is a {@link GuildApplicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GuildApplication} or a {@link Page} of {@link GuildApplication} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GuildApplicationQueryService extends QueryService<GuildApplication> {

    private final Logger log = LoggerFactory.getLogger(GuildApplicationQueryService.class);

    private final GuildApplicationRepository guildApplicationRepository;

    public GuildApplicationQueryService(GuildApplicationRepository guildApplicationRepository) {
        this.guildApplicationRepository = guildApplicationRepository;
    }

    /**
     * Return a {@link List} of {@link GuildApplication} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GuildApplication> findByCriteria(GuildApplicationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GuildApplication> specification = createSpecification(criteria);
        return guildApplicationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GuildApplication} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildApplication> findByCriteria(GuildApplicationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GuildApplication> specification = createSpecification(criteria);
        return guildApplicationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GuildApplicationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GuildApplication> specification = createSpecification(criteria);
        return guildApplicationRepository.count(specification);
    }

    /**
     * Function to convert {@link GuildApplicationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GuildApplication> createSpecification(GuildApplicationCriteria criteria) {
        Specification<GuildApplication> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GuildApplication_.id));
            }
            if (criteria.getCharacterName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCharacterName(), GuildApplication_.characterName));
            }
            if (criteria.getCharacterType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCharacterType(), GuildApplication_.characterType));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), GuildApplication_.status));
            }
            if (criteria.getAcceptedById() != null) {
                specification = specification.and(buildSpecification(criteria.getAcceptedById(),
                    root -> root.join(GuildApplication_.acceptedBy, JoinType.LEFT).get(DiscordUser_.id)));
            }
            if (criteria.getAppliedUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getAppliedUserId(),
                    root -> root.join(GuildApplication_.appliedUser, JoinType.LEFT).get(DiscordUser_.id)));
            }
            if (criteria.getGuildServerId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildServerId(),
                    root -> root.join(GuildApplication_.guildServer, JoinType.LEFT).get(GuildServer_.id)));
            }
        }
        return specification;
    }
}
