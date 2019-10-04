package com.triippztech.guildo.service.moderation;

import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.domain.ModerationLogItem;
import com.triippztech.guildo.domain.enumeration.PunishmentType;
import com.triippztech.guildo.exception.command.EmptyListException;
import com.triippztech.guildo.repository.ModerationLogItemRepository;
import com.triippztech.guildo.service.server.GuildServerService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link ModerationLogItem}.
 */
@Service
@Transactional
public class ModerationLogItemService {

    private final Logger log = LoggerFactory.getLogger(ModerationLogItemService.class);

    private final ModerationLogItemRepository moderationLogItemRepository;
    private final GuildServerService guildServerService;

    public ModerationLogItemService(ModerationLogItemRepository moderationLogItemRepository, GuildServerService guildServerService) {
        this.moderationLogItemRepository = moderationLogItemRepository;
        this.guildServerService = guildServerService;
    }

    /**
     * Save a moderationLogItem.
     *
     * @param moderationLogItem the entity to save.
     * @return the persisted entity.
     */
    public ModerationLogItem save(ModerationLogItem moderationLogItem) {
        log.debug("Request to save ModerationLogItem : {}", moderationLogItem);
        return moderationLogItemRepository.save(moderationLogItem);
    }

    /**
     * Get all the moderationLogItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ModerationLogItem> findAll() {
        log.debug("Request to get all ModerationLogItems");
        return moderationLogItemRepository.findAll();
    }


    /**
     * Get one moderationLogItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModerationLogItem> findOne(Long id) {
        log.debug("Request to get ModerationLogItem : {}", id);
        return moderationLogItemRepository.findById(id);
    }

    /**
     * Delete the moderationLogItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ModerationLogItem : {}", id);
        moderationLogItemRepository.deleteById(id);
    }

    @SuppressWarnings("Duplicates")
    public ModerationLogItem createItem(GuildServer guildServer, Member moderator, DiscordUser discordUser, PunishmentType punishmentType,
                                        String reason, Long channelId, String channelName)
    {
        ModerationLogItem moderationLogItem = new ModerationLogItem();
        moderationLogItem.setModerationAction(punishmentType);
        moderationLogItem.setChannelId(channelId);
        moderationLogItem.setChannelName(channelName);
        moderationLogItem.setIssuedById(moderator.getIdLong());
        moderationLogItem.setIssuedByName(moderator.getEffectiveName());
        moderationLogItem.setIssuedToId(discordUser.getUserId());
        moderationLogItem.setIssuedToName(discordUser.getUserName());
        moderationLogItem.setTime(Instant.now());
        moderationLogItem.setReason(reason);
        moderationLogItem.setGuildId(guildServer.getGuildId());
        moderationLogItem.setModItemGuildServer(guildServer);
        save(moderationLogItem);
        return moderationLogItem;
    }

    @SuppressWarnings("Duplicates")
    public void createBotGeneratedItem( GuildServer guildServer, DiscordUser discordUser, PunishmentType punishmentType,
                                       String reason) {
        ModerationLogItem moderationLogItem = new ModerationLogItem();
        moderationLogItem.setModerationAction(punishmentType);
        moderationLogItem.setChannelId(0L);
        moderationLogItem.setChannelName("");
        moderationLogItem.setIssuedById(0L);
        moderationLogItem.setIssuedByName("GuildoBot");
        moderationLogItem.setIssuedToId(discordUser.getUserId());
        moderationLogItem.setIssuedToName(discordUser.getUserName());
        moderationLogItem.setTime(Instant.now());
        moderationLogItem.setReason(reason);
        moderationLogItem.setGuildId(guildServer.getGuildId());
        moderationLogItem.setModItemGuildServer(guildServer);
        save(moderationLogItem);
    }

    public byte[] getAllLogItems (Guild guild, GuildServerService guildServerService ) throws EmptyListException, IOException {
        GuildServer guildServer = guildServerService.getGuildServer( guild );
        Set<ModerationLogItem> logItems = guildServer.getModLogItems();
        if ( logItems.isEmpty() ) throw new EmptyListException("This guild's moderation log is empty");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(ModerationLogItem.logHeader().getBytes());
        for ( ModerationLogItem item : logItems ) {
            stream.write(item.formatFileStr().getBytes());
        }

        return stream.toByteArray();
    }
}
