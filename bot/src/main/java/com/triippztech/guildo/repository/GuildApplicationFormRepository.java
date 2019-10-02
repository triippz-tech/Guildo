package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.GuildApplicationForm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GuildApplicationForm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildApplicationFormRepository extends JpaRepository<GuildApplicationForm, Long>, JpaSpecificationExecutor<GuildApplicationForm> {

}
