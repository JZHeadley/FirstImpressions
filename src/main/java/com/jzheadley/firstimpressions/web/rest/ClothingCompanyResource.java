package com.jzheadley.firstimpressions.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jzheadley.firstimpressions.domain.ClothingCompany;
import com.jzheadley.firstimpressions.repository.ClothingCompanyRepository;
import com.jzheadley.firstimpressions.repository.search.ClothingCompanySearchRepository;
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
 * REST controller for managing ClothingCompany.
 */
@RestController
@RequestMapping("/api")
public class ClothingCompanyResource {

    private final Logger log = LoggerFactory.getLogger(ClothingCompanyResource.class);

    private static final String ENTITY_NAME = "clothingCompany";

    private final ClothingCompanyRepository clothingCompanyRepository;

    private final ClothingCompanySearchRepository clothingCompanySearchRepository;

    public ClothingCompanyResource(ClothingCompanyRepository clothingCompanyRepository, ClothingCompanySearchRepository clothingCompanySearchRepository) {
        this.clothingCompanyRepository = clothingCompanyRepository;
        this.clothingCompanySearchRepository = clothingCompanySearchRepository;
    }

    /**
     * POST  /clothing-companies : Create a new clothingCompany.
     *
     * @param clothingCompany the clothingCompany to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clothingCompany, or with status 400 (Bad Request) if the clothingCompany has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/clothing-companies")
    @Timed
    public ResponseEntity<ClothingCompany> createClothingCompany(@RequestBody ClothingCompany clothingCompany) throws URISyntaxException {
        log.debug("REST request to save ClothingCompany : {}", clothingCompany);
        if (clothingCompany.getId() != null) {
            throw new BadRequestAlertException("A new clothingCompany cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClothingCompany result = clothingCompanyRepository.save(clothingCompany);
        clothingCompanySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/clothing-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clothing-companies : Updates an existing clothingCompany.
     *
     * @param clothingCompany the clothingCompany to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clothingCompany,
     * or with status 400 (Bad Request) if the clothingCompany is not valid,
     * or with status 500 (Internal Server Error) if the clothingCompany couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/clothing-companies")
    @Timed
    public ResponseEntity<ClothingCompany> updateClothingCompany(@RequestBody ClothingCompany clothingCompany) throws URISyntaxException {
        log.debug("REST request to update ClothingCompany : {}", clothingCompany);
        if (clothingCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClothingCompany result = clothingCompanyRepository.save(clothingCompany);
        clothingCompanySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clothingCompany.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clothing-companies : get all the clothingCompanies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clothingCompanies in body
     */
    @GetMapping("/clothing-companies")
    @Timed
    public List<ClothingCompany> getAllClothingCompanies() {
        log.debug("REST request to get all ClothingCompanies");
        return clothingCompanyRepository.findAll();
    }

    /**
     * GET  /clothing-companies/:id : get the "id" clothingCompany.
     *
     * @param id the id of the clothingCompany to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clothingCompany, or with status 404 (Not Found)
     */
    @GetMapping("/clothing-companies/{id}")
    @Timed
    public ResponseEntity<ClothingCompany> getClothingCompany(@PathVariable Long id) {
        log.debug("REST request to get ClothingCompany : {}", id);
        Optional<ClothingCompany> clothingCompany = clothingCompanyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clothingCompany);
    }

    /**
     * DELETE  /clothing-companies/:id : delete the "id" clothingCompany.
     *
     * @param id the id of the clothingCompany to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/clothing-companies/{id}")
    @Timed
    public ResponseEntity<Void> deleteClothingCompany(@PathVariable Long id) {
        log.debug("REST request to delete ClothingCompany : {}", id);

        clothingCompanyRepository.deleteById(id);
        clothingCompanySearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/clothing-companies?query=:query : search for the clothingCompany corresponding
     * to the query.
     *
     * @param query the query of the clothingCompany search
     * @return the result of the search
     */
    @GetMapping("/_search/clothing-companies")
    @Timed
    public List<ClothingCompany> searchClothingCompanies(@RequestParam String query) {
        log.debug("REST request to search ClothingCompanies for query {}", query);
        return StreamSupport
            .stream(clothingCompanySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
