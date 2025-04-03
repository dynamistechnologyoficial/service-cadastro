package com.banco.cadastro.br.web.rest;

import com.banco.cadastro.br.repository.ArqMetadataRepository;
import com.banco.cadastro.br.service.ArqMetadataQueryService;
import com.banco.cadastro.br.service.ArqMetadataService;
import com.banco.cadastro.br.service.criteria.ArqMetadataCriteria;
import com.banco.cadastro.br.service.dto.ArqMetadataDTO;
import com.banco.cadastro.br.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.banco.cadastro.br.domain.ArqMetadata}.
 */
@RestController
@RequestMapping("/api/arq-metadata")
public class ArqMetadataResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArqMetadataResource.class);

    private static final String ENTITY_NAME = "serviceCadastroArqMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArqMetadataService arqMetadataService;

    private final ArqMetadataRepository arqMetadataRepository;

    private final ArqMetadataQueryService arqMetadataQueryService;

    public ArqMetadataResource(
        ArqMetadataService arqMetadataService,
        ArqMetadataRepository arqMetadataRepository,
        ArqMetadataQueryService arqMetadataQueryService
    ) {
        this.arqMetadataService = arqMetadataService;
        this.arqMetadataRepository = arqMetadataRepository;
        this.arqMetadataQueryService = arqMetadataQueryService;
    }

    /**
     * {@code POST  /arq-metadata} : Create a new arqMetadata.
     *
     * @param arqMetadataDTO the arqMetadataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arqMetadataDTO, or with status {@code 400 (Bad Request)} if the arqMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArqMetadataDTO> createArqMetadata(@Valid @RequestBody ArqMetadataDTO arqMetadataDTO) throws URISyntaxException {
        LOG.debug("REST request to save ArqMetadata : {}", arqMetadataDTO);
        if (arqMetadataDTO.getId() != null) {
            throw new BadRequestAlertException("A new arqMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        arqMetadataDTO = arqMetadataService.save(arqMetadataDTO);
        return ResponseEntity.created(new URI("/api/arq-metadata/" + arqMetadataDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, arqMetadataDTO.getId().toString()))
            .body(arqMetadataDTO);
    }

    /**
     * {@code PUT  /arq-metadata/:id} : Updates an existing arqMetadata.
     *
     * @param id the id of the arqMetadataDTO to save.
     * @param arqMetadataDTO the arqMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arqMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the arqMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arqMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArqMetadataDTO> updateArqMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArqMetadataDTO arqMetadataDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ArqMetadata : {}, {}", id, arqMetadataDTO);
        if (arqMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arqMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arqMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        arqMetadataDTO = arqMetadataService.update(arqMetadataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arqMetadataDTO.getId().toString()))
            .body(arqMetadataDTO);
    }

    /**
     * {@code PATCH  /arq-metadata/:id} : Partial updates given fields of an existing arqMetadata, field will ignore if it is null
     *
     * @param id the id of the arqMetadataDTO to save.
     * @param arqMetadataDTO the arqMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arqMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the arqMetadataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the arqMetadataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the arqMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArqMetadataDTO> partialUpdateArqMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArqMetadataDTO arqMetadataDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ArqMetadata partially : {}, {}", id, arqMetadataDTO);
        if (arqMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arqMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arqMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArqMetadataDTO> result = arqMetadataService.partialUpdate(arqMetadataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arqMetadataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /arq-metadata} : get all the arqMetadata.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arqMetadata in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArqMetadataDTO>> getAllArqMetadata(
        ArqMetadataCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ArqMetadata by criteria: {}", criteria);

        Page<ArqMetadataDTO> page = arqMetadataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arq-metadata/count} : count all the arqMetadata.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countArqMetadata(ArqMetadataCriteria criteria) {
        LOG.debug("REST request to count ArqMetadata by criteria: {}", criteria);
        return ResponseEntity.ok().body(arqMetadataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /arq-metadata/:id} : get the "id" arqMetadata.
     *
     * @param id the id of the arqMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arqMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArqMetadataDTO> getArqMetadata(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ArqMetadata : {}", id);
        Optional<ArqMetadataDTO> arqMetadataDTO = arqMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arqMetadataDTO);
    }

    /**
     * {@code DELETE  /arq-metadata/:id} : delete the "id" arqMetadata.
     *
     * @param id the id of the arqMetadataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArqMetadata(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ArqMetadata : {}", id);
        arqMetadataService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
