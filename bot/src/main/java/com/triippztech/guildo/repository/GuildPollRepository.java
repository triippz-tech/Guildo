package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.GuildPoll;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GuildPoll entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildPollRepository extends JpaRepository<GuildPoll, Long>, JpaSpecificationExecutor<GuildPoll> {

}
