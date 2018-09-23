package com.jzheadley.firstimpressions.repository.search;

import com.jzheadley.firstimpressions.domain.OnlineTraining;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OnlineTraining entity.
 */
public interface OnlineTrainingSearchRepository extends ElasticsearchRepository<OnlineTraining, Long> {
}
