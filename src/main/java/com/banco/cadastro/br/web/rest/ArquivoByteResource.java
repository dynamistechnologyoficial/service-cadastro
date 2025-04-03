package com.banco.cadastro.br.web.rest;

import com.banco.cadastro.br.repository.ArquivoByteRepository;
import com.banco.cadastro.br.service.ArquivoByteQueryService;
import com.banco.cadastro.br.service.ArquivoByteService;
import com.banco.cadastro.br.service.criteria.ArquivoByteCriteria;
import com.banco.cadastro.br.service.dto.ArquivoByteDTO;
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
 * REST controller for managing {@link com.banco.cadastro.br.domain.ArquivoByte}.
 */
@RestController
@RequestMapping("/api/arquivo-bytes")
public class ArquivoByteResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArquivoByteResource.class);

    private static final String ENTITY_NAME = "serviceCadastroArquivoByte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArquivoByteService arquivoByteService;

    private final ArquivoByteRepository arquivoByteRepository;

    private final ArquivoByteQueryService arquivoByteQueryService;

    public ArquivoByteResource(
        ArquivoByteService arquivoByteService,
        ArquivoByteRepository arquivoByteRepository,
        ArquivoByteQueryService arquivoByteQueryService
    ) {
        this.arquivoByteService = arquivoByteService;
        this.arquivoByteRepository = arquivoByteRepository;
        this.arquivoByteQueryService = arquivoByteQueryService;
    }

    /**
     * {@code POST  /arquivo-bytes} : Create a new arquivoByte.
     *
     * @param arquivoByteDTO the arquivoByteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arquivoByteDTO, or with status {@code 400 (Bad Request)} if the arquivoByte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArquivoByteDTO> createArquivoByte(@Valid @RequestBody ArquivoByteDTO arquivoByteDTO) throws URISyntaxException {
        LOG.debug("REST request to save ArquivoByte : {}", arquivoByteDTO);
        if (arquivoByteDTO.getId() == null) {
        	 throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        arquivoByteDTO = arquivoByteService.save(arquivoByteDTO);
        return ResponseEntity.created(new URI("/api/arquivo-bytes/" + arquivoByteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, arquivoByteDTO.getId().toString()))
            .body(arquivoByteDTO);
    }

    /**
     * {@code PUT  /arquivo-bytes/:id} : Updates an existing arquivoByte.
     *
     * @param id the id of the arquivoByteDTO to save.
     * @param arquivoByteDTO the arquivoByteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivoByteDTO,
     * or with status {@code 400 (Bad Request)} if the arquivoByteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arquivoByteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArquivoByteDTO> updateArquivoByte(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArquivoByteDTO arquivoByteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ArquivoByte : {}, {}", id, arquivoByteDTO);
        if (arquivoByteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arquivoByteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arquivoByteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        arquivoByteDTO = arquivoByteService.update(arquivoByteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoByteDTO.getId().toString()))
            .body(arquivoByteDTO);
    }

    /**
     * {@code PATCH  /arquivo-bytes/:id} : Partial updates given fields of an existing arquivoByte, field will ignore if it is null
     *
     * @param id the id of the arquivoByteDTO to save.
     * @param arquivoByteDTO the arquivoByteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivoByteDTO,
     * or with status {@code 400 (Bad Request)} if the arquivoByteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the arquivoByteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the arquivoByteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArquivoByteDTO> partialUpdateArquivoByte(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArquivoByteDTO arquivoByteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ArquivoByte partially : {}, {}", id, arquivoByteDTO);
        if (arquivoByteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arquivoByteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arquivoByteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArquivoByteDTO> result = arquivoByteService.partialUpdate(arquivoByteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoByteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /arquivo-bytes} : get all the arquivoBytes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arquivoBytes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArquivoByteDTO>> getAllArquivoBytes(
        ArquivoByteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ArquivoBytes by criteria: {}", criteria);

        Page<ArquivoByteDTO> page = arquivoByteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arquivo-bytes/count} : count all the arquivoBytes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countArquivoBytes(ArquivoByteCriteria criteria) {
        LOG.debug("REST request to count ArquivoBytes by criteria: {}", criteria);
        return ResponseEntity.ok().body(arquivoByteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /arquivo-bytes/:id} : get the "id" arquivoByte.
     *
     * @param id the id of the arquivoByteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arquivoByteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArquivoByteDTO> getArquivoByte(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ArquivoByte : {}", id);
        Optional<ArquivoByteDTO> arquivoByteDTO = arquivoByteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arquivoByteDTO);
    }

    /**
     * {@code DELETE  /arquivo-bytes/:id} : delete the "id" arquivoByte.
     *
     * @param id the id of the arquivoByteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArquivoByte(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ArquivoByte : {}", id);
        arquivoByteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
