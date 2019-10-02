package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.AutoModeration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AutoModeration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoModerationRepository extends JpaRepository<AutoModeration, Long>, JpaSpecificationExecutor<AutoModeration> {

}
