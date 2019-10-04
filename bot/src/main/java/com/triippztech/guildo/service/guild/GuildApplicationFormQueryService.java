package com.triippztech.guildo.service.guild;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.triippztech.guildo.domain.GuildApplicationForm;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.GuildApplicationFormRepository;
import com.triippztech.guildo.service.dto.GuildApplicationFormCriteria;

/**
 * Service for executing complex queries for {@link GuildApplicationForm} entities in the database.
 * The main input is a {@link GuildApplicationFormCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GuildApplicationForm} or a {@link Page} of {@link GuildApplicationForm} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GuildApplicationFormQueryService extends QueryService<GuildApplicationForm> {

    private final Logger log = LoggerFactory.getLogger(GuildApplicationFormQueryService.class);

    private final GuildApplicationFormRepository guildApplicationFormRepository;

    public GuildApplicationFormQueryService(GuildApplicationFormRepository guildApplicationFormRepository) {
        this.guildApplicationFormRepository = guildApplicationFormRepository;
    }

    /**
     * Return a {@link List} of {@link GuildApplicationForm} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GuildApplicationForm> findByCriteria(GuildApplicationFormCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GuildApplicationForm> specification = createSpecification(criteria);
        return guildApplicationFormRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GuildApplicationForm} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildApplicationForm> findByCriteria(GuildApplicationFormCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GuildApplicationForm> specification = createSpecification(criteria);
        return guildApplicationFormRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GuildApplicationFormCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GuildApplicationForm> specification = createSpecification(criteria);
        return guildApplicationFormRepository.count(specification);
    }

    /**
     * Function to convert {@link GuildApplicationFormCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GuildApplicationForm> createSpecification(GuildApplicationFormCriteria criteria) {
        Specification<GuildApplicationForm> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GuildApplicationForm_.id));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), GuildApplicationForm_.guildId));
            }
        }
        return specification;
    }
}
