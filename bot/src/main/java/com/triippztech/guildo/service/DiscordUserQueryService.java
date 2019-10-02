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

import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.DiscordUserRepository;
import com.triippztech.guildo.service.dto.DiscordUserCriteria;

/**
 * Service for executing complex queries for {@link DiscordUser} entities in the database.
 * The main input is a {@link DiscordUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DiscordUser} or a {@link Page} of {@link DiscordUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DiscordUserQueryService extends QueryService<DiscordUser> {

    private final Logger log = LoggerFactory.getLogger(DiscordUserQueryService.class);

    private final DiscordUserRepository discordUserRepository;

    public DiscordUserQueryService(DiscordUserRepository discordUserRepository) {
        this.discordUserRepository = discordUserRepository;
    }

    /**
     * Return a {@link List} of {@link DiscordUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DiscordUser> findByCriteria(DiscordUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DiscordUser> specification = createSpecification(criteria);
        return discordUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DiscordUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DiscordUser> findByCriteria(DiscordUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DiscordUser> specification = createSpecification(criteria);
        return discordUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DiscordUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DiscordUser> specification = createSpecification(criteria);
        return discordUserRepository.count(specification);
    }

    /**
     * Function to convert {@link DiscordUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DiscordUser> createSpecification(DiscordUserCriteria criteria) {
        Specification<DiscordUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DiscordUser_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), DiscordUser_.userId));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), DiscordUser_.userName));
            }
            if (criteria.getIcon() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIcon(), DiscordUser_.icon));
            }
            if (criteria.getCommandsIssued() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommandsIssued(), DiscordUser_.commandsIssued));
            }
            if (criteria.getBlacklisted() != null) {
                specification = specification.and(buildSpecification(criteria.getBlacklisted(), DiscordUser_.blacklisted));
            }
            if (criteria.getUserLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getUserLevel(), DiscordUser_.userLevel));
            }
            if (criteria.getUserProfileId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserProfileId(),
                    root -> root.join(DiscordUser_.userProfile, JoinType.LEFT).get(DiscordUserProfile_.id)));
            }
            if (criteria.getUserTempBansId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserTempBansId(),
                    root -> root.join(DiscordUser_.userTempBans, JoinType.LEFT).get(TempBan_.id)));
            }
            if (criteria.getUserMutesId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserMutesId(),
                    root -> root.join(DiscordUser_.userMutes, JoinType.LEFT).get(Mute_.id)));
            }
            if (criteria.getUserApplicationsId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserApplicationsId(),
                    root -> root.join(DiscordUser_.userApplications, JoinType.LEFT).get(GuildApplication_.id)));
            }
        }
        return specification;
    }
}
