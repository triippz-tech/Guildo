package com.triippztech.guildo.service.guild;

import com.triippztech.guildo.domain.GuildPollItem;
import com.triippztech.guildo.repository.GuildPollItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GuildPollItem}.
 */
@Service
@Transactional
public class GuildPollItemService {

    private final Logger log = LoggerFactory.getLogger(GuildPollItemService.class);

    private final GuildPollItemRepository guildPollItemRepository;

    public GuildPollItemService(GuildPollItemRepository guildPollItemRepository) {
        this.guildPollItemRepository = guildPollItemRepository;
    }

    /**
     * Save a guildPollItem.
     *
     * @param guildPollItem the entity to save.
     * @return the persisted entity.
     */
    public GuildPollItem save(GuildPollItem guildPollItem) {
        log.debug("Request to save GuildPollItem : {}", guildPollItem);
        return guildPollItemRepository.save(guildPollItem);
    }

    /**
     * Get all the guildPollItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GuildPollItem> findAll() {
        log.debug("Request to get all GuildPollItems");
        return guildPollItemRepository.findAll();
    }


    /**
     * Get one guildPollItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GuildPollItem> findOne(Long id) {
        log.debug("Request to get GuildPollItem : {}", id);
        return guildPollItemRepository.findById(id);
    }

    /**
     * Delete the guildPollItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GuildPollItem : {}", id);
        guildPollItemRepository.deleteById(id);
    }
}
