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

import com.triippztech.guildo.domain.TempBan;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.TempBanRepository;
import com.triippztech.guildo.service.dto.TempBanCriteria;

/**
 * Service for executing complex queries for {@link TempBan} entities in the database.
 * The main input is a {@link TempBanCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TempBan} or a {@link Page} of {@link TempBan} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TempBanQueryService extends QueryService<TempBan> {

    private final Logger log = LoggerFactory.getLogger(TempBanQueryService.class);

    private final TempBanRepository tempBanRepository;

    public TempBanQueryService(TempBanRepository tempBanRepository) {
        this.tempBanRepository = tempBanRepository;
    }

    /**
     * Return a {@link List} of {@link TempBan} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TempBan> findByCriteria(TempBanCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TempBan> specification = createSpecification(criteria);
        return tempBanRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TempBan} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TempBan> findByCriteria(TempBanCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TempBan> specification = createSpecification(criteria);
        return tempBanRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TempBanCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TempBan> specification = createSpecification(criteria);
        return tempBanRepository.count(specification);
    }

    /**
     * Function to convert {@link TempBanCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TempBan> createSpecification(TempBanCriteria criteria) {
        Specification<TempBan> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TempBan_.id));
            }
            if (criteria.getReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReason(), TempBan_.reason));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), TempBan_.endTime));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), TempBan_.guildId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), TempBan_.userId));
            }
            if (criteria.getBannedUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getBannedUserId(),
                    root -> root.join(TempBan_.bannedUser, JoinType.LEFT).get(DiscordUser_.id)));
            }
            if (criteria.getTempBanGuildServerId() != null) {
                specification = specification.and(buildSpecification(criteria.getTempBanGuildServerId(),
                    root -> root.join(TempBan_.tempBanGuildServer, JoinType.LEFT).get(GuildServer_.id)));
            }
        }
        return specification;
    }
}
