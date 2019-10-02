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

import com.triippztech.guildo.domain.Mute;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.MuteRepository;
import com.triippztech.guildo.service.dto.MuteCriteria;

/**
 * Service for executing complex queries for {@link Mute} entities in the database.
 * The main input is a {@link MuteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Mute} or a {@link Page} of {@link Mute} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MuteQueryService extends QueryService<Mute> {

    private final Logger log = LoggerFactory.getLogger(MuteQueryService.class);

    private final MuteRepository muteRepository;

    public MuteQueryService(MuteRepository muteRepository) {
        this.muteRepository = muteRepository;
    }

    /**
     * Return a {@link List} of {@link Mute} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Mute> findByCriteria(MuteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Mute> specification = createSpecification(criteria);
        return muteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Mute} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Mute> findByCriteria(MuteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mute> specification = createSpecification(criteria);
        return muteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MuteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Mute> specification = createSpecification(criteria);
        return muteRepository.count(specification);
    }

    /**
     * Function to convert {@link MuteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mute> createSpecification(MuteCriteria criteria) {
        Specification<Mute> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Mute_.id));
            }
            if (criteria.getReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReason(), Mute_.reason));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndTime(), Mute_.endTime));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), Mute_.guildId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), Mute_.userId));
            }
            if (criteria.getMutedUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getMutedUserId(),
                    root -> root.join(Mute_.mutedUser, JoinType.LEFT).get(DiscordUser_.id)));
            }
            if (criteria.getMutedGuildServerId() != null) {
                specification = specification.and(buildSpecification(criteria.getMutedGuildServerId(),
                    root -> root.join(Mute_.mutedGuildServer, JoinType.LEFT).get(GuildServer_.id)));
            }
        }
        return specification;
    }
}
