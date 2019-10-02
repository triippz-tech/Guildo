package com.triippztech.guildo.service;

import com.triippztech.guildo.domain.ServerLogItem;
import com.triippztech.guildo.repository.ServerLogItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ServerLogItem}.
 */
@Service
@Transactional
public class ServerLogItemService {

    private final Logger log = LoggerFactory.getLogger(ServerLogItemService.class);

    private final ServerLogItemRepository serverLogItemRepository;

    public ServerLogItemService(ServerLogItemRepository serverLogItemRepository) {
        this.serverLogItemRepository = serverLogItemRepository;
    }

    /**
     * Save a serverLogItem.
     *
     * @param serverLogItem the entity to save.
     * @return the persisted entity.
     */
    public ServerLogItem save(ServerLogItem serverLogItem) {
        log.debug("Request to save ServerLogItem : {}", serverLogItem);
        return serverLogItemRepository.save(serverLogItem);
    }

    /**
     * Get all the serverLogItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ServerLogItem> findAll() {
        log.debug("Request to get all ServerLogItems");
        return serverLogItemRepository.findAll();
    }


    /**
     * Get one serverLogItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServerLogItem> findOne(Long id) {
        log.debug("Request to get ServerLogItem : {}", id);
        return serverLogItemRepository.findById(id);
    }

    /**
     * Delete the serverLogItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ServerLogItem : {}", id);
        serverLogItemRepository.deleteById(id);
    }
}
