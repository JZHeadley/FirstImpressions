package com.jzheadley.firstimpressions.repository.search;

import com.jzheadley.firstimpressions.domain.Location;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Location entity.
 */
public interface LocationSearchRepository extends ElasticsearchRepository<Location, Long> {
}
