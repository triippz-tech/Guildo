package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.DiscordUserProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DiscordUserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscordUserProfileRepository extends JpaRepository<DiscordUserProfile, Long>, JpaSpecificationExecutor<DiscordUserProfile> {

}
