package com.banco.cadastro.br.web.rest;

import com.banco.cadastro.br.repository.EntidadeTesteRepository;
import com.banco.cadastro.br.service.EntidadeTesteQueryService;
import com.banco.cadastro.br.service.EntidadeTesteService;
import com.banco.cadastro.br.service.criteria.EntidadeTesteCriteria;
import com.banco.cadastro.br.service.dto.EntidadeTesteDTO;
import com.banco.cadastro.br.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.banco.cadastro.br.domain.EntidadeTeste}.
 */
@RestController
@RequestMapping("/api/entidade-testes")
public class EntidadeTesteResource {

    private static final Logger LOG = LoggerFactory.getLogger(EntidadeTesteResource.class);

    private static final String ENTITY_NAME = "serviceCadastroEntidadeTeste";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntidadeTesteService entidadeTesteService;

    private final EntidadeTesteRepository entidadeTesteRepository;

    private final EntidadeTesteQueryService entidadeTesteQueryService;

    public EntidadeTesteResource(
        EntidadeTesteService entidadeTesteService,
        EntidadeTesteRepository entidadeTesteRepository,
        EntidadeTesteQueryService entidadeTesteQueryService
    ) {
        this.entidadeTesteService = entidadeTesteService;
        this.entidadeTesteRepository = entidadeTesteRepository;
        this.entidadeTesteQueryService = entidadeTesteQueryService;
    }

    /**
     * {@code POST  /entidade-testes} : Create a new entidadeTeste.
     *
     * @param entidadeTesteDTO the entidadeTesteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entidadeTesteDTO, or with status {@code 400 (Bad Request)} if the entidadeTeste has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EntidadeTesteDTO> createEntidadeTeste(@RequestBody EntidadeTesteDTO entidadeTesteDTO) throws URISyntaxException {
        LOG.debug("REST request to save EntidadeTeste : {}", entidadeTesteDTO);
        if (entidadeTesteDTO.getId() != null) {
            throw new BadRequestAlertException("A new entidadeTeste cannot already have an ID", ENTITY_NAME, "idexists");
        }
        entidadeTesteDTO = entidadeTesteService.save(entidadeTesteDTO);
        return ResponseEntity.created(new URI("/api/entidade-testes/" + entidadeTesteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, entidadeTesteDTO.getId().toString()))
            .body(entidadeTesteDTO);
    }

    /**
     * {@code PUT  /entidade-testes/:id} : Updates an existing entidadeTeste.
     *
     * @param id the id of the entidadeTesteDTO to save.
     * @param entidadeTesteDTO the entidadeTesteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entidadeTesteDTO,
     * or with status {@code 400 (Bad Request)} if the entidadeTesteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entidadeTesteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntidadeTesteDTO> updateEntidadeTeste(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntidadeTesteDTO entidadeTesteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EntidadeTeste : {}, {}", id, entidadeTesteDTO);
        if (entidadeTesteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entidadeTesteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entidadeTesteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        entidadeTesteDTO = entidadeTesteService.update(entidadeTesteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entidadeTesteDTO.getId().toString()))
            .body(entidadeTesteDTO);
    }

    /**
     * {@code PATCH  /entidade-testes/:id} : Partial updates given fields of an existing entidadeTeste, field will ignore if it is null
     *
     * @param id the id of the entidadeTesteDTO to save.
     * @param entidadeTesteDTO the entidadeTesteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entidadeTesteDTO,
     * or with status {@code 400 (Bad Request)} if the entidadeTesteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the entidadeTesteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the entidadeTesteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EntidadeTesteDTO> partialUpdateEntidadeTeste(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntidadeTesteDTO entidadeTesteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EntidadeTeste partially : {}, {}", id, entidadeTesteDTO);
        if (entidadeTesteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entidadeTesteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entidadeTesteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EntidadeTesteDTO> result = entidadeTesteService.partialUpdate(entidadeTesteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entidadeTesteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /entidade-testes} : get all the entidadeTestes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entidadeTestes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EntidadeTesteDTO>> getAllEntidadeTestes(
        EntidadeTesteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EntidadeTestes by criteria: {}", criteria);

        Page<EntidadeTesteDTO> page = entidadeTesteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /entidade-testes/count} : count all the entidadeTestes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEntidadeTestes(EntidadeTesteCriteria criteria) {
        LOG.debug("REST request to count EntidadeTestes by criteria: {}", criteria);
        return ResponseEntity.ok().body(entidadeTesteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /entidade-testes/:id} : get the "id" entidadeTeste.
     *
     * @param id the id of the entidadeTesteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entidadeTesteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntidadeTesteDTO> getEntidadeTeste(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EntidadeTeste : {}", id);
        Optional<EntidadeTesteDTO> entidadeTesteDTO = entidadeTesteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entidadeTesteDTO);
    }

    /**
     * {@code DELETE  /entidade-testes/:id} : delete the "id" entidadeTeste.
     *
     * @param id the id of the entidadeTesteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntidadeTeste(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EntidadeTeste : {}", id);
        entidadeTesteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
