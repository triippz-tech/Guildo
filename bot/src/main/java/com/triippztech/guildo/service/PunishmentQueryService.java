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

import com.triippztech.guildo.domain.Punishment;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.PunishmentRepository;
import com.triippztech.guildo.service.dto.PunishmentCriteria;

/**
 * Service for executing complex queries for {@link Punishment} entities in the database.
 * The main input is a {@link PunishmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Punishment} or a {@link Page} of {@link Punishment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PunishmentQueryService extends QueryService<Punishment> {

    private final Logger log = LoggerFactory.getLogger(PunishmentQueryService.class);

    private final PunishmentRepository punishmentRepository;

    public PunishmentQueryService(PunishmentRepository punishmentRepository) {
        this.punishmentRepository = punishmentRepository;
    }

    /**
     * Return a {@link List} of {@link Punishment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Punishment> findByCriteria(PunishmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Punishment> specification = createSpecification(criteria);
        return punishmentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Punishment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Punishment> findByCriteria(PunishmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Punishment> specification = createSpecification(criteria);
        return punishmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PunishmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Punishment> specification = createSpecification(criteria);
        return punishmentRepository.count(specification);
    }

    /**
     * Function to convert {@link PunishmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Punishment> createSpecification(PunishmentCriteria criteria) {
        Specification<Punishment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Punishment_.id));
            }
            if (criteria.getMaxStrikes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxStrikes(), Punishment_.maxStrikes));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildSpecification(criteria.getAction(), Punishment_.action));
            }
            if (criteria.getPunishmentDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPunishmentDuration(), Punishment_.punishmentDuration));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), Punishment_.guildId));
            }
        }
        return specification;
    }
}
