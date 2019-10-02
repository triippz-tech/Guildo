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

import com.triippztech.guildo.domain.DiscordUserProfile;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.DiscordUserProfileRepository;
import com.triippztech.guildo.service.dto.DiscordUserProfileCriteria;

/**
 * Service for executing complex queries for {@link DiscordUserProfile} entities in the database.
 * The main input is a {@link DiscordUserProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DiscordUserProfile} or a {@link Page} of {@link DiscordUserProfile} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DiscordUserProfileQueryService extends QueryService<DiscordUserProfile> {

    private final Logger log = LoggerFactory.getLogger(DiscordUserProfileQueryService.class);

    private final DiscordUserProfileRepository discordUserProfileRepository;

    public DiscordUserProfileQueryService(DiscordUserProfileRepository discordUserProfileRepository) {
        this.discordUserProfileRepository = discordUserProfileRepository;
    }

    /**
     * Return a {@link List} of {@link DiscordUserProfile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DiscordUserProfile> findByCriteria(DiscordUserProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DiscordUserProfile> specification = createSpecification(criteria);
        return discordUserProfileRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DiscordUserProfile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DiscordUserProfile> findByCriteria(DiscordUserProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DiscordUserProfile> specification = createSpecification(criteria);
        return discordUserProfileRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DiscordUserProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DiscordUserProfile> specification = createSpecification(criteria);
        return discordUserProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link DiscordUserProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DiscordUserProfile> createSpecification(DiscordUserProfileCriteria criteria) {
        Specification<DiscordUserProfile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DiscordUserProfile_.id));
            }
            if (criteria.getFavoriteGame() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFavoriteGame(), DiscordUserProfile_.favoriteGame));
            }
            if (criteria.getProfilePhoto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfilePhoto(), DiscordUserProfile_.profilePhoto));
            }
            if (criteria.getTwitterUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTwitterUrl(), DiscordUserProfile_.twitterUrl));
            }
            if (criteria.getTwitchUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTwitchUrl(), DiscordUserProfile_.twitchUrl));
            }
            if (criteria.getYoutubeUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getYoutubeUrl(), DiscordUserProfile_.youtubeUrl));
            }
            if (criteria.getFacebookUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFacebookUrl(), DiscordUserProfile_.facebookUrl));
            }
            if (criteria.getHitboxUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHitboxUrl(), DiscordUserProfile_.hitboxUrl));
            }
            if (criteria.getBeamUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBeamUrl(), DiscordUserProfile_.beamUrl));
            }
        }
        return specification;
    }
}
