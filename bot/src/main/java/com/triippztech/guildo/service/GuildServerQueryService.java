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

import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.GuildServerRepository;
import com.triippztech.guildo.service.dto.GuildServerCriteria;

/**
 * Service for executing complex queries for {@link GuildServer} entities in the database.
 * The main input is a {@link GuildServerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GuildServer} or a {@link Page} of {@link GuildServer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GuildServerQueryService extends QueryService<GuildServer> {

    private final Logger log = LoggerFactory.getLogger(GuildServerQueryService.class);

    private final GuildServerRepository guildServerRepository;

    public GuildServerQueryService(GuildServerRepository guildServerRepository) {
        this.guildServerRepository = guildServerRepository;
    }

    /**
     * Return a {@link List} of {@link GuildServer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GuildServer> findByCriteria(GuildServerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GuildServer> specification = createSpecification(criteria);
        return guildServerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GuildServer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildServer> findByCriteria(GuildServerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GuildServer> specification = createSpecification(criteria);
        return guildServerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GuildServerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GuildServer> specification = createSpecification(criteria);
        return guildServerRepository.count(specification);
    }

    /**
     * Function to convert {@link GuildServerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GuildServer> createSpecification(GuildServerCriteria criteria) {
        Specification<GuildServer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GuildServer_.id));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), GuildServer_.guildId));
            }
            if (criteria.getGuildName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGuildName(), GuildServer_.guildName));
            }
            if (criteria.getIcon() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIcon(), GuildServer_.icon));
            }
            if (criteria.getOwner() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOwner(), GuildServer_.owner));
            }
            if (criteria.getServerLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getServerLevel(), GuildServer_.serverLevel));
            }
            if (criteria.getGuildProfileId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildProfileId(),
                    root -> root.join(GuildServer_.guildProfile, JoinType.LEFT).get(GuildServerProfile_.id)));
            }
            if (criteria.getApplicationFormId() != null) {
                specification = specification.and(buildSpecification(criteria.getApplicationFormId(),
                    root -> root.join(GuildServer_.applicationForm, JoinType.LEFT).get(GuildApplicationForm_.id)));
            }
            if (criteria.getGuildSettingsId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildSettingsId(),
                    root -> root.join(GuildServer_.guildSettings, JoinType.LEFT).get(GuildServerSettings_.id)));
            }
            if (criteria.getWelcomeMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getWelcomeMessageId(),
                    root -> root.join(GuildServer_.welcomeMessage, JoinType.LEFT).get(WelcomeMessage_.id)));
            }
            if (criteria.getServerPollsId() != null) {
                specification = specification.and(buildSpecification(criteria.getServerPollsId(),
                    root -> root.join(GuildServer_.serverPolls, JoinType.LEFT).get(GuildPoll_.id)));
            }
            if (criteria.getGuildAnnoucementsId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildAnnoucementsId(),
                    root -> root.join(GuildServer_.guildAnnoucements, JoinType.LEFT).get(ScheduledAnnouncement_.id)));
            }
            if (criteria.getGuildEventsId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildEventsId(),
                    root -> root.join(GuildServer_.guildEvents, JoinType.LEFT).get(GuildEvent_.id)));
            }
            if (criteria.getGiveAwaysId() != null) {
                specification = specification.and(buildSpecification(criteria.getGiveAwaysId(),
                    root -> root.join(GuildServer_.giveAways, JoinType.LEFT).get(GiveAway_.id)));
            }
            if (criteria.getModLogItemsId() != null) {
                specification = specification.and(buildSpecification(criteria.getModLogItemsId(),
                    root -> root.join(GuildServer_.modLogItems, JoinType.LEFT).get(ModerationLogItem_.id)));
            }
            if (criteria.getServerLogItemsId() != null) {
                specification = specification.and(buildSpecification(criteria.getServerLogItemsId(),
                    root -> root.join(GuildServer_.serverLogItems, JoinType.LEFT).get(ServerLogItem_.id)));
            }
            if (criteria.getGuildTempBansId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildTempBansId(),
                    root -> root.join(GuildServer_.guildTempBans, JoinType.LEFT).get(TempBan_.id)));
            }
            if (criteria.getMutedUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getMutedUsersId(),
                    root -> root.join(GuildServer_.mutedUsers, JoinType.LEFT).get(Mute_.id)));
            }
            if (criteria.getGuildApplicationsId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildApplicationsId(),
                    root -> root.join(GuildServer_.guildApplications, JoinType.LEFT).get(GuildApplication_.id)));
            }
        }
        return specification;
    }
}
