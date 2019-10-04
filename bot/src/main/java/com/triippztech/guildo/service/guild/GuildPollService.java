package com.triippztech.guildo.service.guild;

import com.triippztech.guildo.domain.GuildPoll;
import com.triippztech.guildo.repository.GuildPollRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GuildPoll}.
 */
@Service
@Transactional
public class GuildPollService {

    private final Logger log = LoggerFactory.getLogger(GuildPollService.class);

    private final GuildPollRepository guildPollRepository;

    public GuildPollService(GuildPollRepository guildPollRepository) {
        this.guildPollRepository = guildPollRepository;
    }

    /**
     * Save a guildPoll.
     *
     * @param guildPoll the entity to save.
     * @return the persisted entity.
     */
    public GuildPoll save(GuildPoll guildPoll) {
        log.debug("Request to save GuildPoll : {}", guildPoll);
        return guildPollRepository.save(guildPoll);
    }

    /**
     * Get all the guildPolls.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GuildPoll> findAll() {
        log.debug("Request to get all GuildPolls");
        return guildPollRepository.findAll();
    }


    /**
     * Get one guildPoll by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GuildPoll> findOne(Long id) {
        log.debug("Request to get GuildPoll : {}", id);
        return guildPollRepository.findById(id);
    }

    /**
     * Delete the guildPoll by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GuildPoll : {}", id);
        guildPollRepository.deleteById(id);
    }
}
