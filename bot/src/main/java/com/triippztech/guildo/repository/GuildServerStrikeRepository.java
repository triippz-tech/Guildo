package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServerStrike;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the GuildServerStrike entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildServerStrikeRepository extends JpaRepository<GuildServerStrike, Long>, JpaSpecificationExecutor<GuildServerStrike> {
    Optional<GuildServerStrike> findByUserIdAndGuildId(Long userId, Long guildId);
    List<GuildServerStrike> findAllByDiscordUser(DiscordUser discordUser);
    List<GuildServerStrike> findAllByGuildId(Long guildId);
}
