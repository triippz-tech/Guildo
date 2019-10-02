package com.triippztech.guildo.repository;
import com.triippztech.guildo.domain.RootPage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RootPage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RootPageRepository extends JpaRepository<RootPage, Long> {

}
