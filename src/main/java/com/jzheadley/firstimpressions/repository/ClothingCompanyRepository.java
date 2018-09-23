package com.jzheadley.firstimpressions.repository;

import com.jzheadley.firstimpressions.domain.ClothingCompany;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ClothingCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClothingCompanyRepository extends JpaRepository<ClothingCompany, Long> {

}
