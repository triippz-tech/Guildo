package com.triippztech.guildo.service.server;

import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.repository.GuildServerRepository;
import com.triippztech.guildo.utils.Pair;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link GuildServer}.
 */
@Service
@Transactional
public class GuildServerService {

    private final Logger log = LoggerFactory.getLogger(GuildServerService.class);

    private final GuildServerRepository guildServerRepository;

    public GuildServerService(GuildServerRepository guildServerRepository) {
        this.guildServerRepository = guildServerRepository;
    }

    /**
     * Save a guildServer.
     *
     * @param guildServer the entity to save.
     * @return the persisted entity.
     */
    public GuildServer save(GuildServer guildServer) {
        log.debug("Request to save GuildServer : {}", guildServer);
        return guildServerRepository.save(guildServer);
    }

    /**
     * Get all the guildServers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GuildServer> findAll(Pageable pageable) {
        log.debug("Request to get all GuildServers");
        return guildServerRepository.findAll(pageable);
    }


    /**
     * Get one guildServer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GuildServer> findOne(Long id) {
        log.debug("Request to get GuildServer : {}", id);
        return guildServerRepository.findById(id);
    }

    /**
     * Delete the guildServer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GuildServer : {}", id);
        guildServerRepository.deleteById(id);
    }

    public Pair<Boolean, GuildServer> guildServerExists(Long guildId) {
        Optional<GuildServer> guildServer = guildServerRepository.findByGuildId(guildId);
        return guildServer.map(server -> new Pair<>(true, server)).orElseGet(() -> new Pair<>(false, null));
    }

    public GuildServer createGuildServer(Guild guild) {
        Pair<Boolean, GuildServer> guildServerPair = guildServerExists(guild.getIdLong());
        if ( guildServerPair.getKey() ) return guildServerPair.getValue();

        GuildServer guildServer = new GuildServer(
            guild.getIdLong(),
            guild.getName(),
            guild.getOwnerIdLong(),
            guild.getIconUrl());
        save(guildServer);
        return guildServer;
    }

    public GuildServer getGuildServer(Guild guild) {
        Optional<GuildServer> guildServer = guildServerRepository.findByGuildId(guild.getIdLong());
        return guildServer.orElseGet(() -> createGuildServer(guild));
    }

//    public List<User> getTempBannedUsers(Guild guild) {
//        log.debug("Request to get Ban List for Guild: {}", guild.getName());
//        List<User> users = new ArrayList<>();
//
//        for ( Member member : guild.getMembers() ) {
//            DiscordUser user = discordUserService.getDiscordUser( member.getUser() );
//            user.getUserTempBans().forEach( ban -> {
//
//                if ( ban.getGuildId() == guild.getIdLong() )
//                    users.add(guild.getJDA().getUserById(user.getUserId()));
//            });
//        }
//
//        return users;
//    }
}
