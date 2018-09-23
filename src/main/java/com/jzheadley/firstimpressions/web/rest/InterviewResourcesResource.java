package com.jzheadley.firstimpressions.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jzheadley.firstimpressions.domain.InterviewResources;
import com.jzheadley.firstimpressions.repository.InterviewResourcesRepository;
import com.jzheadley.firstimpressions.repository.search.InterviewResourcesSearchRepository;
import com.jzheadley.firstimpressions.web.rest.errors.BadRequestAlertException;
import com.jzheadley.firstimpressions.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing InterviewResources.
 */
@RestController
@RequestMapping("/api")
public class InterviewResourcesResource {

    private final Logger log = LoggerFactory.getLogger(InterviewResourcesResource.class);

    private static final String ENTITY_NAME = "interviewResources";

    private final InterviewResourcesRepository interviewResourcesRepository;

    private final InterviewResourcesSearchRepository interviewResourcesSearchRepository;

    public InterviewResourcesResource(InterviewResourcesRepository interviewResourcesRepository, InterviewResourcesSearchRepository interviewResourcesSearchRepository) {
        this.interviewResourcesRepository = interviewResourcesRepository;
        this.interviewResourcesSearchRepository = interviewResourcesSearchRepository;
    }

    /**
     * POST  /interview-resources : Create a new interviewResources.
     *
     * @param interviewResources the interviewResources to create
     * @return the ResponseEntity with status 201 (Created) and with body the new interviewResources, or with status 400 (Bad Request) if the interviewResources has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/interview-resources")
    @Timed
    public ResponseEntity<InterviewResources> createInterviewResources(@RequestBody InterviewResources interviewResources) throws URISyntaxException {
        log.debug("REST request to save InterviewResources : {}", interviewResources);
        if (interviewResources.getId() != null) {
            throw new BadRequestAlertException("A new interviewResources cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterviewResources result = interviewResourcesRepository.save(interviewResources);
        interviewResourcesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/interview-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /interview-resources : Updates an existing interviewResources.
     *
     * @param interviewResources the interviewResources to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated interviewResources,
     * or with status 400 (Bad Request) if the interviewResources is not valid,
     * or with status 500 (Internal Server Error) if the interviewResources couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/interview-resources")
    @Timed
    public ResponseEntity<InterviewResources> updateInterviewResources(@RequestBody InterviewResources interviewResources) throws URISyntaxException {
        log.debug("REST request to update InterviewResources : {}", interviewResources);
        if (interviewResources.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InterviewResources result = interviewResourcesRepository.save(interviewResources);
        interviewResourcesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, interviewResources.getId().toString()))
            .body(result);
    }

    /**
     * GET  /interview-resources : get all the interviewResources.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of interviewResources in body
     */
    @GetMapping("/interview-resources")
    @Timed
    public List<InterviewResources> getAllInterviewResources() {
        log.debug("REST request to get all InterviewResources");
        return interviewResourcesRepository.findAll();
    }

    /**
     * GET  /interview-resources/:id : get the "id" interviewResources.
     *
     * @param id the id of the interviewResources to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the interviewResources, or with status 404 (Not Found)
     */
    @GetMapping("/interview-resources/{id}")
    @Timed
    public ResponseEntity<InterviewResources> getInterviewResources(@PathVariable Long id) {
        log.debug("REST request to get InterviewResources : {}", id);
        Optional<InterviewResources> interviewResources = interviewResourcesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(interviewResources);
    }

    /**
     * DELETE  /interview-resources/:id : delete the "id" interviewResources.
     *
     * @param id the id of the interviewResources to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/interview-resources/{id}")
    @Timed
    public ResponseEntity<Void> deleteInterviewResources(@PathVariable Long id) {
        log.debug("REST request to delete InterviewResources : {}", id);

        interviewResourcesRepository.deleteById(id);
        interviewResourcesSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/interview-resources?query=:query : search for the interviewResources corresponding
     * to the query.
     *
     * @param query the query of the interviewResources search
     * @return the result of the search
     */
    @GetMapping("/_search/interview-resources")
    @Timed
    public List<InterviewResources> searchInterviewResources(@RequestParam String query) {
        log.debug("REST request to search InterviewResources for query {}", query);
        return StreamSupport
            .stream(interviewResourcesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
