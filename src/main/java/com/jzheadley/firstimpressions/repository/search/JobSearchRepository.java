package com.jzheadley.firstimpressions.repository.search;

import com.jzheadley.firstimpressions.domain.Job;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Job entity.
 */
public interface JobSearchRepository extends ElasticsearchRepository<Job, Long> {
}
