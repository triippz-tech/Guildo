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

import com.triippztech.guildo.domain.ScheduledAnnouncement;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.ScheduledAnnouncementRepository;
import com.triippztech.guildo.service.dto.ScheduledAnnouncementCriteria;

/**
 * Service for executing complex queries for {@link ScheduledAnnouncement} entities in the database.
 * The main input is a {@link ScheduledAnnouncementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ScheduledAnnouncement} or a {@link Page} of {@link ScheduledAnnouncement} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScheduledAnnouncementQueryService extends QueryService<ScheduledAnnouncement> {

    private final Logger log = LoggerFactory.getLogger(ScheduledAnnouncementQueryService.class);

    private final ScheduledAnnouncementRepository scheduledAnnouncementRepository;

    public ScheduledAnnouncementQueryService(ScheduledAnnouncementRepository scheduledAnnouncementRepository) {
        this.scheduledAnnouncementRepository = scheduledAnnouncementRepository;
    }

    /**
     * Return a {@link List} of {@link ScheduledAnnouncement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ScheduledAnnouncement> findByCriteria(ScheduledAnnouncementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ScheduledAnnouncement> specification = createSpecification(criteria);
        return scheduledAnnouncementRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ScheduledAnnouncement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ScheduledAnnouncement> findByCriteria(ScheduledAnnouncementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ScheduledAnnouncement> specification = createSpecification(criteria);
        return scheduledAnnouncementRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ScheduledAnnouncementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ScheduledAnnouncement> specification = createSpecification(criteria);
        return scheduledAnnouncementRepository.count(specification);
    }

    /**
     * Function to convert {@link ScheduledAnnouncementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ScheduledAnnouncement> createSpecification(ScheduledAnnouncementCriteria criteria) {
        Specification<ScheduledAnnouncement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ScheduledAnnouncement_.id));
            }
            if (criteria.getAnnoucementTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnnoucementTitle(), ScheduledAnnouncement_.annoucementTitle));
            }
            if (criteria.getAnnoucementImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnnoucementImgUrl(), ScheduledAnnouncement_.annoucementImgUrl));
            }
            if (criteria.getAnnoucementFire() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnnoucementFire(), ScheduledAnnouncement_.annoucementFire));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), ScheduledAnnouncement_.guildId));
            }
            if (criteria.getAnnouceGuildId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnnouceGuildId(),
                    root -> root.join(ScheduledAnnouncement_.annouceGuild, JoinType.LEFT).get(GuildServer_.id)));
            }
        }
        return specification;
    }
}
