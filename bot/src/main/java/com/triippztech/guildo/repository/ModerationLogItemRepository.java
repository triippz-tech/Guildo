package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.ModerationLogItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ModerationLogItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModerationLogItemRepository extends JpaRepository<ModerationLogItem, Long>, JpaSpecificationExecutor<ModerationLogItem> {

}
