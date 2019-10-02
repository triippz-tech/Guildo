package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.AutoModAutoRaid;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AutoModAutoRaid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoModAutoRaidRepository extends JpaRepository<AutoModAutoRaid, Long>, JpaSpecificationExecutor<AutoModAutoRaid> {

}
