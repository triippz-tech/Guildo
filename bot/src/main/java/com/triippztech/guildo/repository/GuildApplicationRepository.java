package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.GuildApplication;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GuildApplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildApplicationRepository extends JpaRepository<GuildApplication, Long>, JpaSpecificationExecutor<GuildApplication> {

}
