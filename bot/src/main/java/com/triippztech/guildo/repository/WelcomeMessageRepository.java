package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.WelcomeMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WelcomeMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WelcomeMessageRepository extends JpaRepository<WelcomeMessage, Long>, JpaSpecificationExecutor<WelcomeMessage> {

}
