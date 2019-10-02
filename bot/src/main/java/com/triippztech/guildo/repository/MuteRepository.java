package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.Mute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Mute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MuteRepository extends JpaRepository<Mute, Long>, JpaSpecificationExecutor<Mute> {

}
