package com.jzheadley.firstimpressions.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ClothingCompanySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ClothingCompanySearchRepositoryMockConfiguration {

    @MockBean
    private ClothingCompanySearchRepository mockClothingCompanySearchRepository;

}
