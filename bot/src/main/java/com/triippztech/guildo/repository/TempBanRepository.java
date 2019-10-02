package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.domain.TempBan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the TempBan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TempBanRepository extends JpaRepository<TempBan, Long>, JpaSpecificationExecutor<TempBan> {
    List<TempBan> findAllByEndTimeIsLessThan(Instant now);
    List<TempBan> findAllByEndTimeIsGreaterThan(Instant now);
    List<TempBan> findAllByBannedUser(DiscordUser discordUser);
    List<TempBan> findAllByTempBanGuildServer(GuildServer guildServer);
    Optional<TempBan> findByUserIdAndGuildId(Long userId, Long guildId);
}
