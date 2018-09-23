package com.jzheadley.firstimpressions.repository;

import com.jzheadley.firstimpressions.domain.OnlineTraining;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OnlineTraining entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OnlineTrainingRepository extends JpaRepository<OnlineTraining, Long> {

}
