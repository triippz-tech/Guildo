package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.GuildServer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the GuildServer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildServerRepository extends JpaRepository<GuildServer, Long>, JpaSpecificationExecutor<GuildServer> {
    Optional<GuildServer> findByGuildId(Long guildId);
}
