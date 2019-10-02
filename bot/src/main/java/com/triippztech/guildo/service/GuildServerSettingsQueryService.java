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

import com.triippztech.guildo.domain.GuildServerSettings;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.GuildServerSettingsRepository;
import com.triippztech.guildo.service.dto.GuildServerSettingsCriteria;

/**
 * Service for executing complex queries for {@link GuildServerSettings} entities in the database.
 * The main input is a {@link GuildServerSettingsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GuildServerSettings} or a {@link Page} of {@link GuildServerSettings} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GuildServerSettingsQueryService extends QueryService<GuildServerSettings> {

    private final Logger log = LoggerFactory.getLogger(GuildServerSettingsQueryService.class);

    private final GuildServerSettingsRepository guildServerSettingsRepository;

    public GuildServerSettingsQueryService(GuildServerSettingsRepository guildServerSettingsRepository) {
        this.guildServerSettingsRepository = guildServerSettingsRepository;
    }

    /**
     * Return a {@link List} of {@link GuildServerSettings} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GuildServerSettings> findByCriteria(GuildServerSettingsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GuildServerSettings> specification = createSpecification(criteria);
        return guildServerSettingsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GuildServerSettings} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildServerSettings> findByCriteria(GuildServerSettingsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GuildServerSettings> specification = createSpecification(criteria);
        return guildServerSettingsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GuildServerSettingsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GuildServerSettings> specification = createSpecification(criteria);
        return guildServerSettingsRepository.count(specification);
    }

    /**
     * Function to convert {@link GuildServerSettingsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GuildServerSettings> createSpecification(GuildServerSettingsCriteria criteria) {
        Specification<GuildServerSettings> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GuildServerSettings_.id));
            }
            if (criteria.getPrefix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrefix(), GuildServerSettings_.prefix));
            }
            if (criteria.getTimezone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTimezone(), GuildServerSettings_.timezone));
            }
            if (criteria.getRaidModeEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getRaidModeEnabled(), GuildServerSettings_.raidModeEnabled));
            }
            if (criteria.getRaidModeReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRaidModeReason(), GuildServerSettings_.raidModeReason));
            }
            if (criteria.getMaxStrikes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxStrikes(), GuildServerSettings_.maxStrikes));
            }
            if (criteria.getAcceptingApplications() != null) {
                specification = specification.and(buildSpecification(criteria.getAcceptingApplications(), GuildServerSettings_.acceptingApplications));
            }
            if (criteria.getAutoModConfigId() != null) {
                specification = specification.and(buildSpecification(criteria.getAutoModConfigId(),
                    root -> root.join(GuildServerSettings_.autoModConfig, JoinType.LEFT).get(AutoModeration_.id)));
            }
            if (criteria.getPunishmentConfigId() != null) {
                specification = specification.and(buildSpecification(criteria.getPunishmentConfigId(),
                    root -> root.join(GuildServerSettings_.punishmentConfig, JoinType.LEFT).get(Punishment_.id)));
            }
        }
        return specification;
    }
}
