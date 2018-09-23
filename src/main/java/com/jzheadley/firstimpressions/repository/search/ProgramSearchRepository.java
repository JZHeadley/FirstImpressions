package com.jzheadley.firstimpressions.repository.search;

import com.jzheadley.firstimpressions.domain.Program;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Program entity.
 */
public interface ProgramSearchRepository extends ElasticsearchRepository<Program, Long> {
}
