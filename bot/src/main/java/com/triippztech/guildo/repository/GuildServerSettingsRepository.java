package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.GuildServerSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GuildServerSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildServerSettingsRepository extends JpaRepository<GuildServerSettings, Long>, JpaSpecificationExecutor<GuildServerSettings> {

}
