package com.jzheadley.firstimpressions.repository.search;

import com.jzheadley.firstimpressions.domain.InterviewResources;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InterviewResources entity.
 */
public interface InterviewResourcesSearchRepository extends ElasticsearchRepository<InterviewResources, Long> {
}
