package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.GuildEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GuildEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildEventRepository extends JpaRepository<GuildEvent, Long>, JpaSpecificationExecutor<GuildEvent> {

}
