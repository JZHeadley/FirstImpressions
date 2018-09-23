package com.jzheadley.firstimpressions.repository.search;

import com.jzheadley.firstimpressions.domain.ClothingCompany;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ClothingCompany entity.
 */
public interface ClothingCompanySearchRepository extends ElasticsearchRepository<ClothingCompany, Long> {
}
