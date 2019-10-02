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

import com.triippztech.guildo.domain.ServerLogItem;
import com.triippztech.guildo.domain.*; // for static metamodels
import com.triippztech.guildo.repository.ServerLogItemRepository;
import com.triippztech.guildo.service.dto.ServerLogItemCriteria;

/**
 * Service for executing complex queries for {@link ServerLogItem} entities in the database.
 * The main input is a {@link ServerLogItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ServerLogItem} or a {@link Page} of {@link ServerLogItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServerLogItemQueryService extends QueryService<ServerLogItem> {

    private final Logger log = LoggerFactory.getLogger(ServerLogItemQueryService.class);

    private final ServerLogItemRepository serverLogItemRepository;

    public ServerLogItemQueryService(ServerLogItemRepository serverLogItemRepository) {
        this.serverLogItemRepository = serverLogItemRepository;
    }

    /**
     * Return a {@link List} of {@link ServerLogItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ServerLogItem> findByCriteria(ServerLogItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ServerLogItem> specification = createSpecification(criteria);
        return serverLogItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ServerLogItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServerLogItem> findByCriteria(ServerLogItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServerLogItem> specification = createSpecification(criteria);
        return serverLogItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServerLogItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ServerLogItem> specification = createSpecification(criteria);
        return serverLogItemRepository.count(specification);
    }

    /**
     * Function to convert {@link ServerLogItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServerLogItem> createSpecification(ServerLogItemCriteria criteria) {
        Specification<ServerLogItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ServerLogItem_.id));
            }
            if (criteria.getActivity() != null) {
                specification = specification.and(buildSpecification(criteria.getActivity(), ServerLogItem_.activity));
            }
            if (criteria.getChannelId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChannelId(), ServerLogItem_.channelId));
            }
            if (criteria.getChannelName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChannelName(), ServerLogItem_.channelName));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), ServerLogItem_.time));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), ServerLogItem_.userId));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), ServerLogItem_.userName));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), ServerLogItem_.guildId));
            }
            if (criteria.getServerItemGuildServerId() != null) {
                specification = specification.and(buildSpecification(criteria.getServerItemGuildServerId(),
                    root -> root.join(ServerLogItem_.serverItemGuildServer, JoinType.LEFT).get(GuildServer_.id)));
            }
        }
        return specification;
    }
}
