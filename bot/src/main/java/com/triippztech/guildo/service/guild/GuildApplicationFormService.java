package com.triippztech.guildo.service.guild;

import com.triippztech.guildo.domain.GuildApplicationForm;
import com.triippztech.guildo.repository.GuildApplicationFormRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GuildApplicationForm}.
 */
@Service
@Transactional
public class GuildApplicationFormService {

    private final Logger log = LoggerFactory.getLogger(GuildApplicationFormService.class);

    private final GuildApplicationFormRepository guildApplicationFormRepository;

    public GuildApplicationFormService(GuildApplicationFormRepository guildApplicationFormRepository) {
        this.guildApplicationFormRepository = guildApplicationFormRepository;
    }

    /**
     * Save a guildApplicationForm.
     *
     * @param guildApplicationForm the entity to save.
     * @return the persisted entity.
     */
    public GuildApplicationForm save(GuildApplicationForm guildApplicationForm) {
        log.debug("Request to save GuildApplicationForm : {}", guildApplicationForm);
        return guildApplicationFormRepository.save(guildApplicationForm);
    }

    /**
     * Get all the guildApplicationForms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GuildApplicationForm> findAll() {
        log.debug("Request to get all GuildApplicationForms");
        return guildApplicationFormRepository.findAll();
    }


    /**
     * Get one guildApplicationForm by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GuildApplicationForm> findOne(Long id) {
        log.debug("Request to get GuildApplicationForm : {}", id);
        return guildApplicationFormRepository.findById(id);
    }

    /**
     * Delete the guildApplicationForm by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GuildApplicationForm : {}", id);
        guildApplicationFormRepository.deleteById(id);
    }
}
