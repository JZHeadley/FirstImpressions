package com.jzheadley.firstimpressions.repository;

import com.jzheadley.firstimpressions.domain.Job;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

}
