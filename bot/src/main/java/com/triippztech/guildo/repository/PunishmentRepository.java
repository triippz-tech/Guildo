package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.Punishment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Punishment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PunishmentRepository extends JpaRepository<Punishment, Long>, JpaSpecificationExecutor<Punishment> {

}
