package com.triippztech.guildo.service.moderation;

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

import com.triippztech.guildo.domain.ModerationLogItem;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.ModerationLogItemRepository;
import com.triippztech.guildo.service.dto.ModerationLogItemCriteria;

/**
 * Service for executing complex queries for {@link ModerationLogItem} entities in the database.
 * The main input is a {@link ModerationLogItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ModerationLogItem} or a {@link Page} of {@link ModerationLogItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ModerationLogItemQueryService extends QueryService<ModerationLogItem> {

    private final Logger log = LoggerFactory.getLogger(ModerationLogItemQueryService.class);

    private final ModerationLogItemRepository moderationLogItemRepository;

    public ModerationLogItemQueryService(ModerationLogItemRepository moderationLogItemRepository) {
        this.moderationLogItemRepository = moderationLogItemRepository;
    }

    /**
     * Return a {@link List} of {@link ModerationLogItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ModerationLogItem> findByCriteria(ModerationLogItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ModerationLogItem> specification = createSpecification(criteria);
        return moderationLogItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ModerationLogItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ModerationLogItem> findByCriteria(ModerationLogItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ModerationLogItem> specification = createSpecification(criteria);
        return moderationLogItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ModerationLogItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ModerationLogItem> specification = createSpecification(criteria);
        return moderationLogItemRepository.count(specification);
    }

    /**
     * Function to convert {@link ModerationLogItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ModerationLogItem> createSpecification(ModerationLogItemCriteria criteria) {
        Specification<ModerationLogItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ModerationLogItem_.id));
            }
            if (criteria.getChannelId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChannelId(), ModerationLogItem_.channelId));
            }
            if (criteria.getChannelName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChannelName(), ModerationLogItem_.channelName));
            }
            if (criteria.getIssuedById() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssuedById(), ModerationLogItem_.issuedById));
            }
            if (criteria.getIssuedByName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssuedByName(), ModerationLogItem_.issuedByName));
            }
            if (criteria.getIssuedToId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssuedToId(), ModerationLogItem_.issuedToId));
            }
            if (criteria.getIssuedToName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssuedToName(), ModerationLogItem_.issuedToName));
            }
            if (criteria.getReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReason(), ModerationLogItem_.reason));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), ModerationLogItem_.time));
            }
            if (criteria.getModerationAction() != null) {
                specification = specification.and(buildSpecification(criteria.getModerationAction(), ModerationLogItem_.moderationAction));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), ModerationLogItem_.guildId));
            }
            if (criteria.getModItemGuildServerId() != null) {
                specification = specification.and(buildSpecification(criteria.getModItemGuildServerId(),
                    root -> root.join(ModerationLogItem_.modItemGuildServer, JoinType.LEFT).get(GuildServer_.id)));
            }
        }
        return specification;
    }
}
