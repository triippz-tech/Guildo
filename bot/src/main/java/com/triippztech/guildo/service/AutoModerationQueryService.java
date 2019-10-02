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

import com.triippztech.guildo.domain.AutoModeration;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.AutoModerationRepository;
import com.triippztech.guildo.service.dto.AutoModerationCriteria;

/**
 * Service for executing complex queries for {@link AutoModeration} entities in the database.
 * The main input is a {@link AutoModerationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AutoModeration} or a {@link Page} of {@link AutoModeration} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AutoModerationQueryService extends QueryService<AutoModeration> {

    private final Logger log = LoggerFactory.getLogger(AutoModerationQueryService.class);

    private final AutoModerationRepository autoModerationRepository;

    public AutoModerationQueryService(AutoModerationRepository autoModerationRepository) {
        this.autoModerationRepository = autoModerationRepository;
    }

    /**
     * Return a {@link List} of {@link AutoModeration} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AutoModeration> findByCriteria(AutoModerationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AutoModeration> specification = createSpecification(criteria);
        return autoModerationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AutoModeration} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AutoModeration> findByCriteria(AutoModerationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AutoModeration> specification = createSpecification(criteria);
        return autoModerationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AutoModerationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AutoModeration> specification = createSpecification(criteria);
        return autoModerationRepository.count(specification);
    }

    /**
     * Function to convert {@link AutoModerationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AutoModeration> createSpecification(AutoModerationCriteria criteria) {
        Specification<AutoModeration> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AutoModeration_.id));
            }
            if (criteria.getInviteStrikes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInviteStrikes(), AutoModeration_.inviteStrikes));
            }
            if (criteria.getCopyPastaStrikes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCopyPastaStrikes(), AutoModeration_.copyPastaStrikes));
            }
            if (criteria.getEveryoneMentionStrikes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEveryoneMentionStrikes(), AutoModeration_.everyoneMentionStrikes));
            }
            if (criteria.getReferralStrikes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReferralStrikes(), AutoModeration_.referralStrikes));
            }
            if (criteria.getDuplicateStrikes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuplicateStrikes(), AutoModeration_.duplicateStrikes));
            }
            if (criteria.getResolveUrls() != null) {
                specification = specification.and(buildSpecification(criteria.getResolveUrls(), AutoModeration_.resolveUrls));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), AutoModeration_.enabled));
            }
            if (criteria.getIgnoreConfigId() != null) {
                specification = specification.and(buildSpecification(criteria.getIgnoreConfigId(),
                    root -> root.join(AutoModeration_.ignoreConfig, JoinType.LEFT).get(AutoModIgnore_.id)));
            }
            if (criteria.getMentionConfigId() != null) {
                specification = specification.and(buildSpecification(criteria.getMentionConfigId(),
                    root -> root.join(AutoModeration_.mentionConfig, JoinType.LEFT).get(AutoModMentions_.id)));
            }
            if (criteria.getAntiDupConfigId() != null) {
                specification = specification.and(buildSpecification(criteria.getAntiDupConfigId(),
                    root -> root.join(AutoModeration_.antiDupConfig, JoinType.LEFT).get(AutoModAntiDup_.id)));
            }
            if (criteria.getAutoRaidConfigId() != null) {
                specification = specification.and(buildSpecification(criteria.getAutoRaidConfigId(),
                    root -> root.join(AutoModeration_.autoRaidConfig, JoinType.LEFT).get(AutoModAutoRaid_.id)));
            }
        }
        return specification;
    }
}
