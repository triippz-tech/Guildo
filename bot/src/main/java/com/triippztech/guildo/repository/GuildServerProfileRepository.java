package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.GuildServerProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GuildServerProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildServerProfileRepository extends JpaRepository<GuildServerProfile, Long>, JpaSpecificationExecutor<GuildServerProfile> {

}
