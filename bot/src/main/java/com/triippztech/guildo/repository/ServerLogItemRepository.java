package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.ServerLogItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServerLogItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServerLogItemRepository extends JpaRepository<ServerLogItem, Long>, JpaSpecificationExecutor<ServerLogItem> {

}
