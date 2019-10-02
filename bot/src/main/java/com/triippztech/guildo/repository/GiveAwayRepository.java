package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.GiveAway;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GiveAway entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GiveAwayRepository extends JpaRepository<GiveAway, Long>, JpaSpecificationExecutor<GiveAway> {

}
