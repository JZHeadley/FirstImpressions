package com.jzheadley.firstimpressions.repository;

import com.jzheadley.firstimpressions.domain.InterviewResources;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InterviewResources entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterviewResourcesRepository extends JpaRepository<InterviewResources, Long> {

}
