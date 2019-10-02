package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.AutoModAntiDup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AutoModAntiDup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoModAntiDupRepository extends JpaRepository<AutoModAntiDup, Long>, JpaSpecificationExecutor<AutoModAntiDup> {

}
