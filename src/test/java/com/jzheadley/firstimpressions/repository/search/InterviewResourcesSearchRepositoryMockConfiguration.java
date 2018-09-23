package com.jzheadley.firstimpressions.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of InterviewResourcesSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class InterviewResourcesSearchRepositoryMockConfiguration {

    @MockBean
    private InterviewResourcesSearchRepository mockInterviewResourcesSearchRepository;

}
