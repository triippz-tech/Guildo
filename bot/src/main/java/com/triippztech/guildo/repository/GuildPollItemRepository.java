package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.GuildPollItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GuildPollItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildPollItemRepository extends JpaRepository<GuildPollItem, Long>, JpaSpecificationExecutor<GuildPollItem> {

}
