package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.AutoModIgnore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AutoModIgnore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoModIgnoreRepository extends JpaRepository<AutoModIgnore, Long>, JpaSpecificationExecutor<AutoModIgnore> {

}
