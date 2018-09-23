package com.jzheadley.firstimpressions.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jzheadley.firstimpressions.domain.OnlineTraining;
import com.jzheadley.firstimpressions.repository.OnlineTrainingRepository;
import com.jzheadley.firstimpressions.repository.search.OnlineTrainingSearchRepository;
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
 * REST controller for managing OnlineTraining.
 */
@RestController
@RequestMapping("/api")
public class OnlineTrainingResource {

    private final Logger log = LoggerFactory.getLogger(OnlineTrainingResource.class);

    private static final String ENTITY_NAME = "onlineTraining";

    private final OnlineTrainingRepository onlineTrainingRepository;

    private final OnlineTrainingSearchRepository onlineTrainingSearchRepository;

    public OnlineTrainingResource(OnlineTrainingRepository onlineTrainingRepository, OnlineTrainingSearchRepository onlineTrainingSearchRepository) {
        this.onlineTrainingRepository = onlineTrainingRepository;
        this.onlineTrainingSearchRepository = onlineTrainingSearchRepository;
    }

    /**
     * POST  /online-trainings : Create a new onlineTraining.
     *
     * @param onlineTraining the onlineTraining to create
     * @return the ResponseEntity with status 201 (Created) and with body the new onlineTraining, or with status 400 (Bad Request) if the onlineTraining has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/online-trainings")
    @Timed
    public ResponseEntity<OnlineTraining> createOnlineTraining(@RequestBody OnlineTraining onlineTraining) throws URISyntaxException {
        log.debug("REST request to save OnlineTraining : {}", onlineTraining);
        if (onlineTraining.getId() != null) {
            throw new BadRequestAlertException("A new onlineTraining cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OnlineTraining result = onlineTrainingRepository.save(onlineTraining);
        onlineTrainingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/online-trainings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /online-trainings : Updates an existing onlineTraining.
     *
     * @param onlineTraining the onlineTraining to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated onlineTraining,
     * or with status 400 (Bad Request) if the onlineTraining is not valid,
     * or with status 500 (Internal Server Error) if the onlineTraining couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/online-trainings")
    @Timed
    public ResponseEntity<OnlineTraining> updateOnlineTraining(@RequestBody OnlineTraining onlineTraining) throws URISyntaxException {
        log.debug("REST request to update OnlineTraining : {}", onlineTraining);
        if (onlineTraining.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OnlineTraining result = onlineTrainingRepository.save(onlineTraining);
        onlineTrainingSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, onlineTraining.getId().toString()))
            .body(result);
    }

    /**
     * GET  /online-trainings : get all the onlineTrainings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of onlineTrainings in body
     */
    @GetMapping("/online-trainings")
    @Timed
    public List<OnlineTraining> getAllOnlineTrainings() {
        log.debug("REST request to get all OnlineTrainings");
        return onlineTrainingRepository.findAll();
    }

    /**
     * GET  /online-trainings/:id : get the "id" onlineTraining.
     *
     * @param id the id of the onlineTraining to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the onlineTraining, or with status 404 (Not Found)
     */
    @GetMapping("/online-trainings/{id}")
    @Timed
    public ResponseEntity<OnlineTraining> getOnlineTraining(@PathVariable Long id) {
        log.debug("REST request to get OnlineTraining : {}", id);
        Optional<OnlineTraining> onlineTraining = onlineTrainingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(onlineTraining);
    }

    /**
     * DELETE  /online-trainings/:id : delete the "id" onlineTraining.
     *
     * @param id the id of the onlineTraining to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/online-trainings/{id}")
    @Timed
    public ResponseEntity<Void> deleteOnlineTraining(@PathVariable Long id) {
        log.debug("REST request to delete OnlineTraining : {}", id);

        onlineTrainingRepository.deleteById(id);
        onlineTrainingSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/online-trainings?query=:query : search for the onlineTraining corresponding
     * to the query.
     *
     * @param query the query of the onlineTraining search
     * @return the result of the search
     */
    @GetMapping("/_search/online-trainings")
    @Timed
    public List<OnlineTraining> searchOnlineTrainings(@RequestParam String query) {
        log.debug("REST request to search OnlineTrainings for query {}", query);
        return StreamSupport
            .stream(onlineTrainingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
