package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.AutoModMentions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AutoModMentions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoModMentionsRepository extends JpaRepository<AutoModMentions, Long>, JpaSpecificationExecutor<AutoModMentions> {

}
