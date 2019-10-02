package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.ScheduledAnnouncement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ScheduledAnnouncement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduledAnnouncementRepository extends JpaRepository<ScheduledAnnouncement, Long>, JpaSpecificationExecutor<ScheduledAnnouncement> {

}
